package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import exceptions.NotAcceptableException;
import exceptions.NotFoundException;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос: " + exchange.getRequestMethod());
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSegments = path.split("/");
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    if (pathSegments.length == 2 && pathSegments[1].equals("subtasks")) {
                        String jsonSubtasks = gson.toJson(taskManager.getSubtasksList());
                        sendText(exchange, jsonSubtasks, 200);
                    } else if (pathSegments.length == 3 && pathSegments[1].equals("subtasks")) {
                        int subtaskId = Integer.parseInt(pathSegments[2]);
                        Subtask subtask = taskManager.getSubtask(subtaskId);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask), 200);
                        } else {
                            throw new NotFoundException("Объект не найден");
                        }
                    } else {
                        sendText(exchange, "{\"Ошибка\":\"Некорректный путь\"}", 400);
                    }
                    break;
                case "POST":
                    InputStream requestBodyStream = exchange.getRequestBody();
                    byte[] requestBodyBites = requestBodyStream.readAllBytes();
                    String requestBody = new String(requestBodyBites, StandardCharsets.UTF_8);
                    Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
                    boolean hasOverlap = taskManager.getPrioritizedTasks().stream()
                            .anyMatch(newSubtask::isOverlapping);
                    if (hasOverlap) {
                        throw new NotAcceptableException("Подзадача пересекается по времени с существующей задачей");
                    }
                    boolean epicExists = taskManager.getEpicsList().stream()
                            .anyMatch(epic -> epic.getId().equals(newSubtask.getEpicId()));
                    if (epicExists) {
                        if (newSubtask.getId() != null && newSubtask.getId() != 0) {
                            taskManager.updateSubtask(newSubtask);
                            sendText(exchange, gson.toJson(newSubtask), 200);
                        } else {
                            taskManager.createSubtask(newSubtask);
                            sendText(exchange, gson.toJson(newSubtask), 201);
                        }
                    } else {
                        throw new NotFoundException("Эпик не найден");
                    }
                    break;
                case "DELETE":
                    if (pathSegments.length == 3 && pathSegments[1].equals("subtasks")) {
                        try {
                            int subtaskId = Integer.parseInt(pathSegments[2]);
                            if (taskManager.removeSubtask(subtaskId)) {
                                sendText(exchange, "{\"Сообщение\":\"Подзадача удалена\"}", 200);
                            } else {
                                throw new NotFoundException("Объект не найден");
                            }
                        } catch (NumberFormatException e) {
                            sendText(exchange, "{\"Ошибка\":\"Некорректный формат ID\"}", 400);
                        }
                    } else if (pathSegments.length == 2 && pathSegments[1].equals("subtasks")) {
                        taskManager.clearSubtasksList();
                        sendText(exchange, "{\"Сообщение\":\"Все подзадачи были удалены\"}", 200);
                    } else {
                        sendText(exchange, "{\"Ошибка\":\"Некорректный путь\"}", 400);
                    }
                    break;
                default:
                    sendText(exchange, "{\"Ошибка\":\"Некорректная операция\"}", 405);
            }
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (NotAcceptableException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            sendText(exchange, "{\"Ошибка\":\"Ошибка сервера\"}", 500);
        }
    }
}