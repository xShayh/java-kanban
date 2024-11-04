package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    TaskType taskType = TaskType.SUBTASK;
    private final int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int id, Duration duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%d",
                id,
                taskType.name(),
                name,
                taskStatus,
                description,
                duration.toMinutes(),
                startTime,
                epicId
        );
    }

    public static Subtask getSubtaskFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(values[5]));
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        int epicId = Integer.parseInt(values[7]);
        return new Subtask(name, description, taskStatus, id, duration, startTime, epicId);
    }
}