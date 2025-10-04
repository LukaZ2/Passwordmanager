package me.passwordmanager.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.network.requests.IRequest;

@AllArgsConstructor
public enum RequestType {
    GET_PASSWORD(null);

    @Getter
    private final Class<? extends IRequest> clazz;
}
