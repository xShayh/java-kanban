import controllers.InMemoryHistoryManager;
import controllers.Managers;
import controllers.TaskManager;
import model.TaskStatus;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    public TaskManager tm;
    public InMemoryHistoryManager hm;

    @BeforeEach
    void beforeEach() {
        tm = Managers.getDefault();
        hm = new InMemoryHistoryManager();
    }

    @Test
    public void testEndTimeSetsCorrectly() {
        Task task = tm.createTask(new Task("Задача", "Описание", TaskStatus.NEW));
        task.setDuration(Duration.ofMinutes(5));
        task.setStartTime(LocalDateTime.parse("2024-11-03T12:00:00"));
        assertEquals(LocalDateTime.parse("2024-11-03T12:05:00"), task.getEndTime());
    }
}