package convinience;

import com.google.gson.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Type;

public class GsonUtil {
    public static final Gson CUSTOM_GSON;

    static {
        CUSTOM_GSON = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                    @Override
                    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                })
                .create();
    }
}
