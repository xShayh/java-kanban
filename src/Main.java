import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import controllers.Managers;

public class Main {

    public static void main(String[] args) { // Это не доп. задание, личные тесты
        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task("Task1", "Первая задача", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task2", "Вторая задача", Status.IN_PROGRESS));
        Epic epic1 = taskManager.createEpic(new Epic("Epic1", "Первый эпик"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("Subtask1", "Первая подзадача", Status.NEW, epic1.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("Subtask2", "Вторая подзадача", Status.NEW, epic1.getId()));
        Epic epic2 = taskManager.createEpic(new Epic("Epic2", "Второй эпик"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask("Subtask3", "Третья подзадача", Status.NEW, epic2.getId()));

        System.out.println("После объявления: \n");

        System.out.println("Список задач: " + taskManager.getTasksList());
        System.out.println("Список эпиков: " + taskManager.getEpicsList());
        System.out.println("Список подзадач: " + taskManager.getSubtasksList());

        task1.setStatus(Status.IN_PROGRESS);
        task1.setDescription("Новое описание первой задачи");
        taskManager.updateTask(task1);
        task2.setStatus(Status.DONE);
        task2.setName("Новое имя второй задачи");
        taskManager.updateTask(task2);
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);

        System.out.println("\nПосле обновления: \n");

        System.out.println("Список задач: " + taskManager.getTasksList());
        System.out.println("Список эпиков: " + taskManager.getEpicsList());
        System.out.println("Список подзадач: " + taskManager.getSubtasksList());

        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epic2.getId());

        System.out.println("\nПосле удаления: \n");

        System.out.println("Список задач: " + taskManager.getTasksList());
        System.out.println("Список эпиков: " + taskManager.getEpicsList());
        System.out.println("Список подзадач: " + taskManager.getSubtasksList());
    }
}