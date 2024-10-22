package Controllers;

public class Managers {
    public static TaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistoryManager();
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
