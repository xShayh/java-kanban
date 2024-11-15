package serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Subtask;
import model.Task;

import java.lang.reflect.Type;


public class TaskSerializer implements JsonSerializer<Task> {
    @Override
    public JsonElement serialize(Task task, Type sourceType, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", task.getId());
        jsonObject.addProperty("type", task.getType().toString());
        jsonObject.addProperty("name", task.getName());
        jsonObject.addProperty("status", task.getStatus().toString());
        jsonObject.addProperty("description", task.getDescription());
        jsonObject.addProperty("duration", task.getDuration().toString());
        jsonObject.addProperty("startTime", task.getStartTime().toString());
        if (task instanceof Subtask subtask) {
            jsonObject.addProperty("epicId", subtask.getEpicId());
        }
        return jsonObject;
    }
}