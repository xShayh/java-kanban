package adapters;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Duration;

public class DurationTypeAdapter implements JsonSerializer<Duration>, JsonDeserializer<Duration> {

    @Override
    public JsonElement serialize(Duration duration, Type sourceType, JsonSerializationContext context) {
        return new JsonPrimitive(duration.getSeconds());
    }

    @Override
    public Duration deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        return Duration.ofSeconds(json.getAsLong());
    }
}