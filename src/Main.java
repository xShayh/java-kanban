import controllers.TaskManager;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import controllers.Managers;

public class Main {

    public static void main(String[] args) { // Это не доп. задание, личные тесты
        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task("Task1", "Первая задача", TaskStatus.NEW));
        Task task2 = taskManager.createTask(new Task("Task2", "Вторая задача", TaskStatus.IN_PROGRESS));
        Epic epic1 = taskManager.createEpic(new Epic("Epic1", "Первый эпик"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("Subtask1", "Первая подзадача", TaskStatus.NEW, epic1.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("Subtask2", "Вторая подзадача", TaskStatus.NEW, epic1.getId()));
        Epic epic2 = taskManager.createEpic(new Epic("Epic2", "Второй эпик"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask("Subtask3", "Третья подзадача", TaskStatus.NEW, epic2.getId()));

        System.out.println("После объявления: \n");

        System.out.println("Список задач: " + taskManager.getTasksList());
        System.out.println("Список эпиков: " + taskManager.getEpicsList());
        System.out.println("Список подзадач: " + taskManager.getSubtasksList());

        task1.setStatus(TaskStatus.IN_PROGRESS);
        task1.setDescription("Новое описание первой задачи");
        taskManager.updateTask(task1);
        task2.setStatus(TaskStatus.DONE);
        task2.setName("Новое имя второй задачи");
        taskManager.updateTask(task2);
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);
        subtask3.setStatus(TaskStatus.DONE);
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