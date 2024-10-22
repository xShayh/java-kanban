package Model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d", id, taskType, name, taskStatus, description, epicId);
    }

    public static Subtask getSubtaskFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        String description = values[4];
        int epicId = Integer.parseInt(values[5]);
        return new Subtask(name, description, taskStatus, id, epicId);
    }
}