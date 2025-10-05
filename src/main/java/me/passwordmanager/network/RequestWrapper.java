package me.passwordmanager.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.passwordmanager.network.requests.IRequest;

@AllArgsConstructor
@Getter
public class RequestWrapper {
    //Enum to specify request type. This will be represented as a String in Json.
    private RequestType type;

    //Request body
    private IRequest body;

}
