import adapters.DurationTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Test;
import serializers.TaskDeserializer;
import serializers.TaskSerializer;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskSerializationTest {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(Task.class, new TaskSerializer())
            .registerTypeAdapter(Task.class, new TaskDeserializer())
            .create();

    @Test
    public void testTaskSerialization() {
        Task task = new Task("Task 1", "Description 1", TaskStatus.NEW, 1, Duration.ofSeconds(5), LocalDateTime.now());
        String json = gson.toJson(task);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"name\":\"Task 1\""));
        assertTrue(json.contains("\"status\":\"NEW\""));
        assertTrue(json.contains("\"description\":\"Description 1\""));
        assertTrue(json.contains("\"duration\":\"PT5S\""));
    }

    @Test
    public void testDurationTypeAdapter() {
        Duration duration = Duration.ofMinutes(10);
        String json = gson.toJson(duration);
        assertEquals("600", json);
        Duration deserializedDuration = gson.fromJson(json, Duration.class);
        assertEquals(duration, deserializedDuration);
    }
}