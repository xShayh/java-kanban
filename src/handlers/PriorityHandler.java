package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import exceptions.NotAcceptableException;
import exceptions.NotFoundException;

import java.io.IOException;

public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    private final Gson gson;

    public PriorityHandler(TaskManager taskManager, Gson gson) {
        super(taskManager);
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().equals("GET")) {
                String jsonPrioritizedTasks = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, jsonPrioritizedTasks, 200);
            } else {
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
