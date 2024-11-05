import controllers.HistoryManager;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new HistoryManager() {
            @Override
            public void add(Task task) {

            }

            @Override
            public ArrayList<Task> getHistory() {
                return new ArrayList<>();
            }

            @Override
            public void remove(int id) {

            }
        };
    }

    @Test
    public void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История не пустая.");
    }
}