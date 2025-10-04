package me.passwordmanager.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.network.requests.IRequest;

@AllArgsConstructor
@Getter
public class RequestWrapper {

    private RequestType type;
    private IRequest body;

}
