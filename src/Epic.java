import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIdList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void addSubtask(int id) {
        subtaskIdList.add(id);
    }

    public void removeSubtask(Integer id) {
        subtaskIdList.remove(id);
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    public void removeAllSubtasks() {
        subtaskIdList.clear();
    }

    @Override
    public String toString() {
        return "Epic{name='" + name + "', "
                + "description='" + description + "', "
                + "id=" + id + ", "
                + "status=" + status + "}";
    }
}
