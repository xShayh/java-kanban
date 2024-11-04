import filecontrollers.FileBackedTaskManager;
import model.Epic;
import java.io.File;

public class Main {

    public static void main(String[] args) { // Это не доп. задание, личные тесты
        File file = new File("resources/file.csv");
        FileBackedTaskManager fbtm = FileBackedTaskManager.loadFromFile(file);
        Epic epic = fbtm.getEpic(2);
        System.out.println(epic.getEndTime());
        fbtm.removeTask(1);
    }
}