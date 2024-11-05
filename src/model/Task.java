package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus taskStatus;
    private final TaskType taskType = TaskType.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    public Task(String name, String description, TaskStatus taskStatus, int id) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
        this.duration = Duration.ZERO;
        this.startTime = null;
    }

    public Task(String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, TaskStatus taskStatus, int id, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
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

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean isOverlapping(Task other) {
        if (this.startTime == null || other.startTime == null || this.duration == null || other.duration == null) {
            return false;
        }
        LocalDateTime thisStart = this.getStartTime();
        LocalDateTime thisEnd = this.getEndTime();
        LocalDateTime otherStart = other.getStartTime();
        LocalDateTime otherEnd = other.getEndTime();
        return (thisStart.isBefore(otherEnd) && otherStart.isBefore(thisEnd));
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
        return String.format("%d,%s,%s,%s,%s,%d,%s",
                id,
                taskType.name(),
                name,
                taskStatus,
                description,
                duration.toMinutes(),
                startTime
        );
    }

    public static Task getTaskFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(values[5]));
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        return new Task(name, description, taskStatus, id, duration, startTime);
    }
}