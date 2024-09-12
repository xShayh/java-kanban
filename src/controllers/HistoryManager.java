package controllers;

import java.util.ArrayList;

import model.Task;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory();
}
