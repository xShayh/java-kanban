package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import exceptions.NotAcceptableException;
import exceptions.NotFoundException;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос: " + exchange.getRequestMethod());
        String requestMethod = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathSegments = path.split("/");
        try {
            switch (requestMethod) {
                case "GET":
                    if (pathSegments.length == 2 && pathSegments[1].equals("tasks")) {
                        String jsonTasks = gson.toJson(taskManager.getTasksList());
                        sendText(exchange, jsonTasks, 200);
                    } else if (pathSegments.length == 3 && pathSegments[1].equals("tasks")) {
                        int taskId = Integer.parseInt(pathSegments[2]);
                        Task task = taskManager.getTask(taskId);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task), 200);
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
                    Task newTask = gson.fromJson(requestBody, Task.class);
                    boolean hasOverlap = taskManager.getPrioritizedTasks().stream()
                            .anyMatch(newTask::isOverlapping);
                    if (hasOverlap) {
                        throw new NotAcceptableException("Задача пересекается по времени с существующей задачей");
                    }
                    if (newTask.getId() != 0) {
                        taskManager.updateTask(newTask);
                        String updatedTaskJson = gson.toJson(newTask);
                        exchange.sendResponseHeaders(200, updatedTaskJson.getBytes(StandardCharsets.UTF_8).length);
                        try (OutputStream responseBodyStream = exchange.getResponseBody()) {
                            responseBodyStream.write(updatedTaskJson.getBytes(StandardCharsets.UTF_8));
                            responseBodyStream.flush();
                        }
                    } else {
                        taskManager.createTask(newTask);
                        String createdTaskJson = gson.toJson(newTask);
                        exchange.sendResponseHeaders(201, createdTaskJson.getBytes(StandardCharsets.UTF_8).length);
                        try (OutputStream responseBodyStream = exchange.getResponseBody()) {
                            responseBodyStream.write(createdTaskJson.getBytes(StandardCharsets.UTF_8));
                            responseBodyStream.flush();
                        }
                    }
                    break;
                case "DELETE":
                    if (pathSegments.length == 3 && pathSegments[1].equals("tasks")) {
                        try {
                            int taskId = Integer.parseInt(pathSegments[2]);
                            if (taskManager.removeTask(taskId)) {
                                sendText(exchange, "{\"Сообщение\":\"Задача удалена\"}", 200);
                            } else {
                                throw new NotFoundException("Объект не найден");
                            }
                        } catch (NumberFormatException e) {
                            sendText(exchange, "{\"Ошибка\":\"Некорректный формат ID\"}", 400);
                        }
                    } else if (pathSegments.length == 2 && pathSegments[1].equals("tasks")) {
                        taskManager.clearTasksList();
                        sendText(exchange, "{\"Сообщение\":\"Все задачи удалены\"}", 200);
                    } else {
                        sendText(exchange, "{\"Ошибка\":\"Некорректный путь\"}", 400);
                    }
                    break;
                default:
                    sendText(exchange, "{\"Ошибка\":\"Некорректная операция\"}", 405);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"Ошибка\":\"Некорректный формат ввода\"}", 400);
        } catch (NotFoundException e) {
            sendNotFound(exchange);
        } catch (NotAcceptableException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            sendText(exchange, "{\"Ошибка\":\"Ошибка сервера\"}", 500);
        }
    }
}