package serializers;

import com.google.gson.*;
import model.*;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskDeserializer implements JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonElement json, Type sourceType, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int id = jsonObject.get("id").getAsInt();
        TaskType type = TaskType.valueOf(jsonObject.get("type").getAsString());
        String name = jsonObject.get("name").getAsString();
        TaskStatus taskStatus = TaskStatus.valueOf(jsonObject.get("status").getAsString());
        String description = jsonObject.get("description").getAsString();
        Duration duration = context.deserialize(jsonObject.get("duration"), Duration.class);
        LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());

        if (type == TaskType.TASK) {
            return new Task(name, description, taskStatus, id, duration, startTime);
        } else if (type == TaskType.EPIC) {
            return new Epic(name, description, id, Duration.ZERO, startTime);
        } else if (type == TaskType.SUBTASK) {
            int epicId = jsonObject.get("epicId").getAsInt();
            return new Subtask(name, description, taskStatus, id, duration, startTime, epicId);
        }
        throw new JsonParseException("Unknown task type: " + type);
    }
}
