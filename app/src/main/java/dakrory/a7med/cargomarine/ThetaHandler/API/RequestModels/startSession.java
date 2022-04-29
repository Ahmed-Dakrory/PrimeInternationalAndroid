package dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels;

import com.google.gson.JsonObject;

public class startSession {
    final String name;
    final JsonObject parameters;

    public startSession(String name, JsonObject parameters) {
        this.name = name;
        this.parameters = parameters;
    }
}
