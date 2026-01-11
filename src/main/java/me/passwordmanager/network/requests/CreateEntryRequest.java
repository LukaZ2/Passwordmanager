package me.passwordmanager.network.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

public class CreateEntryRequest implements IRequest {

    public String username;
    public String password;
    public String url;

    @Override
    public IResponse getResponse() {
        //TODO: finish this
        return new Response(false);
    }

    @AllArgsConstructor
    public static class Response implements IResponse {
        public boolean success;
    }
}
