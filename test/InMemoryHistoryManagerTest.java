import java.util.List;

import controllers.InMemoryHistoryManager;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    public InMemoryHistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testHistoryContainsOnlyUniqueTasks() {
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.getFirst());
    }

    @Test
    public void testHistoryIsEmptyAfterTasksDeletion() {
        Task task = new Task("Задача", "Описание", TaskStatus.NEW);

        historyManager.add(task);

        historyManager.remove(task.getId());
        List<Task> history = historyManager.getHistory();

        Assertions.assertEquals(0, history.size());
    }
}