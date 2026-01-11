package me.passwordmanager.network.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.MainApplication;
import me.passwordmanager.database.Entry;

@Getter
public class GetPasswordRequest implements IRequest {

    public String url;

    @Override
    public IResponse getResponse() {
        var database = MainApplication.getInstance().getDatabase();
        if(database == null) return new Response();
        synchronized (database) {
            Entry entry = database.getEntries().stream()
                    .filter(e -> e.getUrl().equalsIgnoreCase(url))
                    .findAny().orElse(null);
            if(entry == null) return new Response();
            return new Response(true, entry.getUsername(), entry.getPassword());
        }
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
