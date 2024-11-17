package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import exceptions.NotAcceptableException;
import exceptions.NotFoundException;
import model.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Запрос: " + exchange.getRequestMethod());
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] pathSplit = path.split("/");
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    if (pathSplit.length == 2 && pathSplit[1].equals("epics")) {
                        String jsonEpics = gson.toJson(taskManager.getEpicsList());
                        sendText(exchange, jsonEpics, 200);
                    } else if (pathSplit.length == 3 && pathSplit[1].equals("epics")) {
                        int epicId = Integer.parseInt(pathSplit[2]);
                        Epic epic = taskManager.getEpic(epicId);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic), 200);
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
                    Epic newEpic = gson.fromJson(requestBody, Epic.class);
                    if (newEpic.getId() != 0) {
                        taskManager.updateEpic(newEpic);
                        sendText(exchange, gson.toJson(newEpic), 200);
                    } else {
                        taskManager.createEpic(newEpic);
                        newEpic.setSubtaskList();
                        sendText(exchange, gson.toJson(newEpic), 201);
                    }
                    break;
                case "DELETE":
                    if (pathSplit.length == 3 && pathSplit[1].equals("epics")) {
                        try {
                            int epicId = Integer.parseInt(pathSplit[2]);
                            if (taskManager.removeEpic(epicId)) {
                                sendText(exchange, "{\"Сообщение\":\"Эпик удалён\"}", 200);
                            } else {
                                throw new NotFoundException("Объект не найден");
                            }
                        } catch (NumberFormatException e) {
                            sendText(exchange, "{\"Ошибка\":\"Некорректный формат ID\"}", 400);
                        }
                    } else if (pathSplit.length == 2 && pathSplit[1].equals("epics")) {
                        taskManager.clearEpicsList();
                        sendText(exchange, "{\"Сообщение\":\"Все эпики были удалены\"}", 200);
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
