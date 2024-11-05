package filecontrollers;

import controllers.InMemoryTaskManager;
import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbTaskManager = new FileBackedTaskManager(file);
        try {
            int lastId = 0;
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.isEmpty()) {
                    String[] values = line.split(",");
                    if (values[1].equals("EPIC")) {
                        Epic epic = Epic.getEpicFromFile(line);
                        fbTaskManager.createEpic(epic);
                    } else if (values[1].equals("SUBTASK")) {
                        Subtask subtask = Subtask.getSubtaskFromFile(line);
                        fbTaskManager.createSubtask(subtask);
                    } else {
                        Task task = Task.getTaskFromFile(line);
                        fbTaskManager.createTask(task);
                    }
                    if (Integer.parseInt(values[0]) > lastId) {
                        lastId = Integer.parseInt(values[0]);
                    }
                }
            }
            fbTaskManager.setIdCounter(++lastId);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при загрузке данных из файла: " + file.getName(), e);
        }
        return fbTaskManager;
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,duration,startTime,epic\n"); // В ТЗ не указан новый хэдер,
            for (Task task : getTasksList()) {                                     // поэтому изменил его на свой взгляд
                bw.write(task.toString() + "\n");
            }
            for (Epic epic : getEpicsList()) {
                bw.write(epic.toString() + "\n");
            }
            for (Subtask subtask : getSubtasksList()) {
                bw.write(subtask.toString() + "\n");
            }
            System.out.println("Задачи успешно сохранены в файл: ");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении задач в файл: " + file.getName(), e);
        }
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTask(int id) {
        super.removeTask(id);
        save();
    }

    @Override
    public void clearTasksList() {
        super.clearTasksList();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpic(int id) {
        super.removeEpic(id);
        save();
    }

    @Override
    public void clearEpicsList() {
        super.clearEpicsList();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtask(int id) {
        super.removeSubtask(id);
        save();
    }

    @Override
    public void clearSubtasksList() {
        super.clearSubtasksList();
        save();
    }
}