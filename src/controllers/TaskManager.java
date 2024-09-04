package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    private int taskIdCounter = 1;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public Task createTask(Task task) {
        task.setId(taskIdCounter);
        tasks.put(taskIdCounter, task);
        taskIdCounter++;
        return task;
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        if (updatedTask == null) {
            return;
        }
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void clearTasksList() {
        tasks.clear();
    }


    public Epic createEpic(Epic epic) {
        epic.setId(taskIdCounter);
        epic.removeAllSubtasks();
        epics.put(taskIdCounter, epic);
        taskIdCounter++;
        return epic;
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epics.get(id).getSubtaskIdList()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        if (updatedEpic == null) {
            return;
        }
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    public void updateEpicStatus(Epic epic) {
        int newTasks = 0;
        int doneTasks = 0;
        if (!epic.getSubtaskIdList().isEmpty()) {
            for (Integer id : epic.getSubtaskIdList()) {
                Subtask subtask = subtasks.get(id);
                if (subtask != null) {
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                    } else if (subtask.getStatus() == Status.NEW) {
                        newTasks += 1;
                    } else if (subtask.getStatus() == Status.DONE) {
                        doneTasks += 1;
                    }
                }
            }
            if (newTasks == epic.getSubtaskIdList().size()) {
                epic.setStatus(Status.NEW);
            } else if (doneTasks == epic.getSubtaskIdList().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    public void removeEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIdList()) {
            subtasks.remove(subtaskId);
        }
    }

    public void clearEpicsList() {
        epics.clear();
        subtasks.clear();
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(taskIdCounter);
        subtasks.put(taskIdCounter, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
        taskIdCounter++;
        return subtask;
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public void updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = subtasks.get(subtask.getId());
        if (updatedSubtask == null) {
            return;
        }
        updatedSubtask.setName(subtask.getName());
        updatedSubtask.setDescription(subtask.getDescription());
        updatedSubtask.setStatus(subtask.getStatus());
        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    public void removeSubtask(int id) {
        final Subtask subtask = subtasks.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }

    public void clearSubtasksList() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            updateEpicStatus(epic);
        }
    }
}
