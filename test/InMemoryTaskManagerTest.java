import controllers.InMemoryHistoryManager;
import controllers.Managers;
import controllers.TaskManager;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    public TaskManager tm;
    public InMemoryHistoryManager hm;

    @BeforeEach
    void beforeEach() {
        tm = Managers.getDefault();
        hm = new InMemoryHistoryManager();
    }

    @Test
    public void taskStatusUpdatesCorrectly() {
        Task task = tm.createTask(new Task("Задача", "Описание", TaskStatus.NEW));
        assertEquals(TaskStatus.NEW, task.getStatus());
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        task.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    @Test
    public void epicStatusUpdatesDependingOnSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        assertEquals(TaskStatus.NEW, epic.getStatus());
        Subtask subtask1 = tm.createSubtask(new Subtask("1 подзадача", "Описание1", TaskStatus.IN_PROGRESS, epic.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        Subtask subtask2 = tm.createSubtask(new Subtask("2 подзадача", "Описание2", TaskStatus.DONE, epic.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
        subtask1.setStatus(TaskStatus.DONE);
        tm.updateSubtask(subtask1);
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void viewedTasksAreSavedInHistory() {
        ArrayList<Task> expectedArray = new ArrayList<>();
        Task task = tm.createTask(new Task("Задача", "Описание", TaskStatus.NEW));
        task = tm.getTask(task.getId());
        expectedArray.add(task);
        assertEquals(expectedArray, tm.getHistory());
        Task task2 = tm.createTask(new Task("Задача2", "Описание2", TaskStatus.NEW));
        task2 = tm.getTask(task2.getId());
        expectedArray.add(task2);
        assertEquals(expectedArray, tm.getHistory());
    }

    @Test
    public void taskNameAndDescriptionUpdatedCorrectly() {
        Task task = tm.createTask(new Task("Задача", "Описание", TaskStatus.NEW));
        task.setName("Новое имя задачи");
        task.setDescription("Новое описание задачи");
        tm.updateTask(task);
        assertEquals("Новое имя задачи", task.getName());
        assertEquals("Новое описание задачи", task.getDescription());
    }

    @Test
    public void inMemoryTaskManagerSetsIdCorrectly() {
        Task taskId1 = tm.createTask(new Task("taskId1", "Задача с 1 id", TaskStatus.NEW));
        Epic epicId2 = tm.createEpic(new Epic("epicId2", "Эпик с 2 id"));
        Subtask subtaskId3 = tm.createSubtask(new Subtask("subtaskId3", "Подзадача с 3 id",
                TaskStatus.NEW, epicId2.getId()));
        assertEquals(1, taskId1.getId());
        assertEquals(2, epicId2.getId());
        assertEquals(3, subtaskId3.getId());
    }

    @Test
    public void historyCantStoreSameTaskTwice() {
        Task task = tm.createTask(new Task("task", "Задача", TaskStatus.NEW));
        hm.add(task);
        hm.add(task);
        assertEquals(1, hm.getHistory().size());
    }

    @Test
    public void tasksCanBeDeletedFromHistory() {
        Task task = tm.createTask(new Task("task", "Задача", TaskStatus.NEW));
        hm.add(task);
        hm.remove(task.getId());
        assertEquals(0, hm.getHistory().size());
    }

    @Test
    public void testOverlappingThrowsIllegalArgumentException() {
        Epic epic = tm.createEpic(new Epic("Epic Task", "Description of epic task"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Subtask 1", "First subtask", TaskStatus.NEW,
                Duration.ofMinutes(10), LocalDateTime.parse("2024-11-03T10:00:00"), epic.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Subtask 2", "Second subtask", TaskStatus.NEW,
                Duration.ofMinutes(15), LocalDateTime.parse("2024-11-03T11:00:00"), epic.getId()));
        subtask1.setStartTime(LocalDateTime.parse("2024-11-03T10:55:00"));

        assertThrows(IllegalArgumentException.class, () -> tm.updateSubtask(subtask1),
                "Подзадача должна вызывать исключение при пересечении времени с другой задачей");
    }
}