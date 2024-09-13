package controllers;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getHistory();

    Task createTask(Task task);

    Task getTask(int id);

    ArrayList<Task> getTasksList();

    void updateTask(Task task);

    void removeTask(int id);

    void clearTasksList();

    Epic createEpic(Epic epic);

    Epic getEpic(int id);

    ArrayList<Epic> getEpicsList();

    ArrayList<Subtask> getEpicSubtasks(int id);

    void updateEpic(Epic epic);

    void updateEpicStatus(Epic epic);

    void removeEpic(int id);

    void clearEpicsList();

    Subtask createSubtask(Subtask subtask);

    Subtask getSubtask(int id);

    ArrayList<Subtask> getSubtasksList();

    void updateSubtask(Subtask subtask);

    void removeSubtask(int id);

    void clearSubtasksList();
}
