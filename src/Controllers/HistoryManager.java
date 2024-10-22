package Controllers;

import java.util.ArrayList;

import Model.Task;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    ArrayList<Task> getHistory();
}
