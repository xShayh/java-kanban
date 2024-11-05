package model;

import java.util.ArrayList;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    private final TaskType taskType = TaskType.EPIC;
    private final ArrayList<Subtask> subtaskList;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        this.subtaskList = new ArrayList<>();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    public Epic(String name, String description, int id) {
        super(name, description, TaskStatus.NEW, id);
        this.subtaskList = new ArrayList<>();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    public Epic(String name, String description, Duration duration, LocalDateTime startTime) {
        super(name, description, TaskStatus.NEW);
        this.subtaskList = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = this.getEndTime();
    }

    public Epic(String name, String description, int id, Duration duration, LocalDateTime startTime) {
        super(name, description, TaskStatus.NEW, id);
        this.subtaskList = new ArrayList<>();
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = this.getEndTime();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        calculateEpicTimes();
    }

    public void removeSubtask(Subtask subtask) {
        subtaskList.remove(subtask);
        calculateEpicTimes();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void removeAllSubtasks() {
        subtaskList.clear();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    @Override
    public LocalDateTime getEndTime() {
        this.endTime = null;
        for (Subtask subtask : subtaskList) {
            if (subtask.getEndTime() != null &&
                    (this.endTime == null || this.endTime.isBefore(subtask.getEndTime()))) {
                this.endTime = subtask.getEndTime();
            }
        }
        return this.endTime;
    }

    public void calculateEpicTimes() {
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = this.getEndTime();
        for (Subtask subtask : subtaskList) {
            this.duration = this.duration.plus(subtask.getDuration());
            if (subtask.getStartTime() != null &&
                    (this.startTime == null || this.startTime.isAfter(subtask.getStartTime()))) {
                this.startTime = subtask.getStartTime();
            }
        }
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

    public static Epic getEpicFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        String description = values[4];
        Duration duration = Duration.ofMinutes(Long.parseLong(values[5]));
        LocalDateTime startTime = LocalDateTime.parse(values[6]);
        return new Epic(name, description, id, duration, startTime);
    }
}