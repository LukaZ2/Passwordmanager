package me.passwordmanager.network;

import com.google.gson.*;
import me.passwordmanager.network.requests.IRequest;

import java.lang.reflect.Type;

public class RequestDeserializer implements JsonDeserializer<RequestWrapper> {
    @Override
    public RequestWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        RequestType requestType = RequestType.valueOf(jsonObject.get("type").getAsString());
        IRequest request = new Gson().fromJson(jsonObject.get("body").getAsJsonObject(), requestType.getClazz());
        return new RequestWrapper(requestType, request);
    }
}
