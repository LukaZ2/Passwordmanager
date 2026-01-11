package me.passwordmanager.network.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.MainApplication;
import me.passwordmanager.database.Entry;

import java.net.URI;
import java.net.URISyntaxException;

@Getter
public class GetPasswordRequest implements IRequest {

    public String url;

    @Override
    public IResponse getResponse() {
        var database = MainApplication.getInstance().getDatabase();
        if(database == null) return new Response();
        synchronized (database) {
            Entry entry = database.getEntries().stream()
                    .filter(e -> domainsMatch(e.getUrl(), url))
                    .findAny().orElse(null);
            if(entry == null) return new Response();
            return new Response(true, entry.getUsername(), entry.getPassword());
        }
    }
    public static boolean domainsMatch(String url1, String url2) {
        try {
            String host1 = normalizeHost(new URI(url1).getHost());
            String host2 = normalizeHost(new URI(url2).getHost());
            if (host1 == null || host2 == null) {
                return false;
            }
            return host1.equals(host2);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    private static String normalizeHost(String host) {
        if (host == null) return null;
        host = host.toLowerCase();
        if (host.startsWith("www.")) {
            host = host.substring(4);
        }
        return host;
    }
    @AllArgsConstructor
    public static class Response implements IResponse {
        public boolean found;
        public String username;
        public String password;

        Response() {
            this.found = false;
            this.username = null;
            this.password = null;
        }
    }
}
