public class Main {

    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task task1 = tm.createTask(new Task("Task1", "Первая задача", Status.NEW));
        Task task2 = tm.createTask(new Task("Task2", "Вторая задача", Status.IN_PROGRESS));
        Epic epic1 = tm.createEpic(new Epic("Epic1", "Первый эпик"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Subtask1", "Первая подзадача", Status.NEW, epic1.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Subtask2", "Вторая подзадача", Status.NEW, epic1.getId()));
        Epic epic2 = tm.createEpic(new Epic("Epic2", "Второй эпик"));
        Subtask subtask3 = tm.createSubtask(new Subtask("Subtask3", "Третья подзадача", Status.NEW, epic2.getId()));

        System.out.println("После объявления: \n");

        System.out.println("Список задач: " + tm.getTasksList());
        System.out.println("Список эпиков: " + tm.getEpicsList());
        System.out.println("Список подзадач: " + tm.getSubtasksList());

        task1.setStatus(Status.IN_PROGRESS);
        task1.setDescription("Новое описание первой задачи");
        tm.updateTask(task1);
        task2.setStatus(Status.DONE);
        task2.setName("Новое имя второй задачи");
        tm.updateTask(task2);
        subtask1.setStatus(Status.IN_PROGRESS);
        tm.updateSubtask(subtask1);
        subtask2.setStatus(Status.DONE);
        tm.updateSubtask(subtask2);
        subtask3.setStatus(Status.DONE);
        tm.updateSubtask(subtask3);

        System.out.println("\nПосле обновления: \n");

        System.out.println("Список задач: " + tm.getTasksList());
        System.out.println("Список эпиков: " + tm.getEpicsList());
        System.out.println("Список подзадач: " + tm.getSubtasksList());

        tm.removeTask(task1.getId());
        tm.removeEpic(epic2.getId());

        System.out.println("\nПосле удаления: \n");

        System.out.println("Список задач: " + tm.getTasksList());
        System.out.println("Список эпиков: " + tm.getEpicsList());
        System.out.println("Список подзадач: " + tm.getSubtasksList());
    }
}