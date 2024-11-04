import controllers.InMemoryHistoryManager;
import controllers.Managers;
import controllers.TaskManager;
import model.Epic;
import model.TaskStatus;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    public TaskManager tm;
    public InMemoryHistoryManager hm;

    @BeforeEach
    void beforeEach() {
        tm = Managers.getDefault();
        hm = new InMemoryHistoryManager();
    }

    @Test
    public void testEpicHasNewStatusAsAllSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Подзадача1", "Описание", TaskStatus.NEW, epic.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Подзадача2", "Описание", TaskStatus.NEW, epic.getId()));
        Subtask subtask3 = tm.createSubtask(new Subtask("Подзадача3", "Описание", TaskStatus.NEW, epic.getId()));
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    public void testEpicHasDoneStatusAsAllSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Подзадача1", "Описание", TaskStatus.DONE, epic.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Подзадача2", "Описание", TaskStatus.DONE, epic.getId()));
        Subtask subtask3 = tm.createSubtask(new Subtask("Подзадача3", "Описание", TaskStatus.DONE, epic.getId()));
        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    public void testEpicHasInProgressStatusWithNewAndDoneSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Подзадача1", "Описание", TaskStatus.NEW, epic.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Подзадача2", "Описание", TaskStatus.DONE, epic.getId()));
        Subtask subtask3 = tm.createSubtask(new Subtask("Подзадача3", "Описание", TaskStatus.NEW, epic.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicHasInProgressStatusAsAllSubtasks() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Подзадача1", "Описание", TaskStatus.IN_PROGRESS, epic.getId()));
        Subtask subtask2 = tm.createSubtask(new Subtask("Подзадача2", "Описание", TaskStatus.IN_PROGRESS, epic.getId()));
        Subtask subtask3 = tm.createSubtask(new Subtask("Подзадача3", "Описание", TaskStatus.IN_PROGRESS, epic.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void testEpicSetsCorrectEndTimeStartTimeAndDuration() {
        Epic epic = tm.createEpic(new Epic("Эпик", "Описание"));
        Subtask subtask1 = tm.createSubtask(new Subtask("Подзадача1", "Описание", TaskStatus.IN_PROGRESS, epic.getId()));
        subtask1.setDuration(Duration.ofMinutes(5));
        subtask1.setStartTime(LocalDateTime.parse("2024-11-03T12:00:00"));
        tm.updateSubtask(subtask1);
        assertEquals(LocalDateTime.parse("2024-11-03T12:05:00"), epic.getEndTime());
        assertEquals(LocalDateTime.parse("2024-11-03T12:00:00"), epic.getStartTime());
        Subtask subtask2 = tm.createSubtask(new Subtask("Подзадача2", "Описание", TaskStatus.IN_PROGRESS, epic.getId()));
        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.parse("2024-11-03T11:00:00"));
        tm.updateSubtask(subtask2);
        assertEquals(LocalDateTime.parse("2024-11-03T12:05:00"), epic.getEndTime());
        assertEquals(LocalDateTime.parse("2024-11-03T11:00:00"), epic.getStartTime());
        assertEquals(Duration.ofMinutes(15), epic.getDuration());
    }
}