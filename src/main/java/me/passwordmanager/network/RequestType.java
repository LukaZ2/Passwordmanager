package me.passwordmanager.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.network.requests.CreateEntryRequest;
import me.passwordmanager.network.requests.GetPasswordRequest;
import me.passwordmanager.network.requests.IRequest;

@AllArgsConstructor
public enum RequestType {
    GET_PASSWORD(GetPasswordRequest.class),
    CREATE_ENTRY(CreateEntryRequest.class)
    ;

    @Getter
    private final Class<? extends IRequest> clazz;
}
