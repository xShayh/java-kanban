package model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask{name='" + name + "', "
                + "description='" + description + "', "
                + "id=" + id + ", "
                + "status=" + status + "}";
    }
}
