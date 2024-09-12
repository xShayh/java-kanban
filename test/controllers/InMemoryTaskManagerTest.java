package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager tm;

    @BeforeEach
    void beforeEach() {
        tm = Managers.getDefault();
    }

    @Test
    public void taskStatusUpdatesCorrectly() {
        Task task = tm.createTask(new Task("Задача", "Описание", Status.NEW));
        assertEquals(Status.NEW, task.getStatus());
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        task.setStatus(Status.DONE);
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    public void epicStatusUpdatesDependingOnSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        assertEquals(Status.NEW, epic.getStatus());
        Subtask subtask1 = tm.createSubtask(new Subtask("1 подзадача", "Описание1", Status.IN_PROGRESS, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        Subtask subtask2 = tm.createSubtask(new Subtask("2 подзадача", "Описание2", Status.DONE, epic.getId()));
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        subtask1.setStatus(Status.DONE);
        tm.updateSubtask(subtask1);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void viewedTasksAreSavedInHistory() {
        ArrayList<Task> expectedArray = new ArrayList<>();
        Task task = tm.createTask(new Task("Задача", "Описание", Status.NEW));
        task = tm.getTask(task.getId());
        expectedArray.add(task);
        assertEquals(expectedArray, tm.getHistory());
        Task task2 = tm.createTask(new Task("Задача2", "Описание2", Status.NEW));
        task2 = tm.getTask(task2.getId());
        expectedArray.add(task2);
        assertEquals(expectedArray, tm.getHistory());
    }

    @Test
    public void historyDeletesFirstTaskAfterMaxHistorySizeReached() {
        ArrayList<Task> expectedArray = new ArrayList<>();
        Task task = tm.createTask(new Task("task", "Описание", Status.NEW));
        Task taskReplacingLastTask = tm.createTask(new Task("taskReplacingLastTask", "Описание", Status.NEW));
        for (int i = 0; i < 10; i++) {
            task = tm.getTask(task.getId());
            expectedArray.add(task);
        }
        taskReplacingLastTask = tm.getTask(taskReplacingLastTask.getId());
        expectedArray.remove(9);
        expectedArray.add(taskReplacingLastTask);
        ArrayList<Task> realArray = tm.getHistory();
        assertEquals(expectedArray, realArray);
    }

    @Test
    public void taskNameAndDescriptionCorrectly() {
        Task task = tm.createTask(new Task("Задача", "Описание", Status.NEW));
        task.setName("Новое имя задачи");
        task.setDescription("Новое описание задачи");
        tm.updateTask(task);
        assertEquals("Новое имя задачи", task.getName());
        assertEquals("Новое описание задачи", task.getDescription());
    }

    @Test
    public void inMemoryTaskManagerSetsIdCorrectly() {
        Task taskId1 = tm.createTask(new Task("taskId1", "Задача с 1 id", Status.NEW));
        Epic epicId2 = tm.createEpic(new Epic("epicId2", "Эпик с 2 id"));
        Subtask subtaskId3 = tm.createSubtask(new Subtask ("subtaskId3", "Подзадача с 3 id",
                Status.NEW, epicId2.getId()));
        assertEquals(1, taskId1.getId());
        assertEquals(2, epicId2.getId());
        assertEquals(3, subtaskId3.getId());
    }
}