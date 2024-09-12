package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;

    private int taskIdCounter = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(taskIdCounter);
        tasks.put(taskIdCounter, task);
        taskIdCounter++;
        return task;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (Objects.nonNull(task)) {
            historyManager.add(task);
        }
        return tasks.get(id);
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void updateTask(Task task) {
        Task updatedTask = tasks.get(task.getId());
        if (updatedTask == null) {
            return;
        }
        updatedTask.setName(task.getName());
        updatedTask.setDescription(task.getDescription());
        updatedTask.setStatus(task.getStatus());
    }

    @Override
    public void removeTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void clearTasksList() {
        tasks.clear();
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(taskIdCounter);
        epic.removeAllSubtasks();
        epics.put(taskIdCounter, epic);
        taskIdCounter++;
        return epic;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (Objects.nonNull(epic)) {
            historyManager.add(epic);
        }
        return epics.get(id);
    }

    @Override
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

    @Override
    public void updateEpic(Epic epic) {
        Epic updatedEpic = epics.get(epic.getId());
        if (updatedEpic == null) {
            return;
        }
        updatedEpic.setName(epic.getName());
        updatedEpic.setDescription(epic.getDescription());
    }

    @Override
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

    @Override
    public void removeEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIdList()) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void clearEpicsList() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(taskIdCounter);
        subtasks.put(taskIdCounter, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask.getId());
        updateEpicStatus(epic);
        taskIdCounter++;
        return subtask;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (Objects.nonNull(subtask)) {
            historyManager.add(subtask);
        }
        return subtasks.get(id);
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
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

    @Override
    public void removeSubtask(int id) {
        final Subtask subtask = subtasks.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(id);
        updateEpicStatus(epic);
    }

    @Override
    public void clearSubtasksList() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            updateEpicStatus(epic);
        }
    }
}
