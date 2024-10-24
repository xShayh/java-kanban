package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIdList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public Epic(String name, String description, int id) {
        super(name, description, TaskStatus.NEW, id);
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
        return String.format("%d,%s,%s,%s,%s", id, taskType, name, taskStatus, description);
    }

    public static Epic getEpicFromFile(String fileString) {
        String[] values = fileString.split(",");
        int id = Integer.parseInt(values[0]);
        String name = values[2];
        String description = values[4];
        return new Epic(name, description, id);
    }
}