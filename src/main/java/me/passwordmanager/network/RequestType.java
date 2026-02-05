package me.passwordmanager.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.network.requests.CreateEntryRequest;
import me.passwordmanager.network.requests.GetPasswordRequest;
import me.passwordmanager.network.requests.IRequest;

/**
 * The enum containing all request types, for extensibility, new request types can easily be added
 */
@AllArgsConstructor
public enum RequestType {
    GET_PASSWORD(GetPasswordRequest.class),
    CREATE_ENTRY(CreateEntryRequest.class)
    ;

    @Getter
    private final Class<? extends IRequest> clazz;
}
