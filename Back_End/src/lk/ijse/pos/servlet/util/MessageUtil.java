package lk.ijse.pos.servlet.util;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class MessageUtil {
    public JsonObjectBuilder buildJsonObject(String state, String message, Object data) {

        JsonObjectBuilder obj = Json.createObjectBuilder();

        obj.add("state", state);
        obj.add("message", message);

        if (data instanceof JsonArrayBuilder) {

            obj.add("data", (JsonArrayBuilder) data);

        } else if (data instanceof JsonObjectBuilder) {

            obj.add("data", ((JsonObjectBuilder) data).build());

        } else {

            obj.add("data", "");

        }

        return obj;

    }
}
