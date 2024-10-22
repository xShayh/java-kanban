package model;

import filecontrollers.TaskType;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus taskStatus;
    protected TaskType taskType;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String name, String description, TaskStatus taskStatus, int id) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Task otherTask = (Task) obj;
        return id == otherTask.id;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s", id, taskType, name, taskStatus, description);
    }

    public static Task getTaskFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        return new Task(name, description, taskStatus, id);
    }
}