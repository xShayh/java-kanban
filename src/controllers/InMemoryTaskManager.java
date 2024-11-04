package controllers;

import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    private int taskIdCounter = 1;

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime() == null && task2.getStartTime() == null) return 0;
        if (task1.getStartTime() == null) return 1;
        if (task2.getStartTime() == null) return -1;
        return task1.getStartTime().compareTo(task2.getStartTime());
    });

    @Override
    public Collection<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void setIdCounter(int newId) {
        taskIdCounter = newId;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task createTask(Task task) {
        task.setId(taskIdCounter);
        boolean hasOverlap = getPrioritizedTasks().stream()
                .anyMatch(task::isOverlapping);
        if (hasOverlap) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        tasks.put(taskIdCounter, task);
        prioritizedTasks.add(task);
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
        Task existingTask = tasks.get(task.getId());
        if (existingTask == null) {
            return;
        }
        prioritizedTasks.remove(existingTask);
        boolean hasOverlap = prioritizedTasks.stream()
                .anyMatch(task::isOverlapping);
        if (hasOverlap) {
            prioritizedTasks.add(existingTask);
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }
        existingTask.setName(task.getName());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setStartTime(task.getStartTime());
        existingTask.setDuration(task.getDuration());
        prioritizedTasks.add(existingTask);
    }

    @Override
    public void removeTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
        }
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

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        return subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toCollection(ArrayList::new));
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
        if (!epic.getSubtaskList().isEmpty()) {
            for (Subtask subtask : epic.getSubtaskList()) {
                if (subtask != null) {
                    if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                        epic.setStatus(TaskStatus.IN_PROGRESS);
                        return;
                    } else if (subtask.getStatus() == TaskStatus.NEW) {
                        newTasks += 1;
                    } else if (subtask.getStatus() == TaskStatus.DONE) {
                        doneTasks += 1;
                    }
                }
            }
            if (newTasks == epic.getSubtaskList().size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (doneTasks == epic.getSubtaskList().size()) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void removeEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            epic.getSubtaskList().forEach(subtask -> subtasks.remove(subtask.getId()));
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
        boolean hasOverlap = getPrioritizedTasks().stream()
                .anyMatch(subtask::isOverlapping);
        if (hasOverlap) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующей задачей");
        }
        subtasks.put(taskIdCounter, subtask);
        prioritizedTasks.add(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtask(subtask);
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
        Subtask existingSubtask = subtasks.get(subtask.getId());
        if (existingSubtask == null) {
            return;
        }
        prioritizedTasks.remove(existingSubtask);
        boolean hasOverlap = prioritizedTasks.stream()
                .filter(anotherSubtask -> anotherSubtask.getId() != subtask.getId())
                .anyMatch(subtask::isOverlapping);
        if (hasOverlap) {
            prioritizedTasks.add(existingSubtask);
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующей задачей");
        }
        existingSubtask.setName(subtask.getName());
        existingSubtask.setDescription(subtask.getDescription());
        existingSubtask.setStatus(subtask.getStatus());
        existingSubtask.setStartTime(subtask.getStartTime());
        existingSubtask.setDuration(subtask.getDuration());
        Epic epic = epics.get(subtask.getEpicId());
        epic.calculateEpicTimes();
        prioritizedTasks.add(existingSubtask);
        updateEpicStatus(epic);
    }

    @Override
    public void removeSubtask(int id) {
        final Subtask subtask = subtasks.remove(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void clearSubtasksList() {
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.removeAllSubtasks();
            updateEpicStatus(epic);
        });
    }
}
