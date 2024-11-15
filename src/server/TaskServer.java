package server;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import controllers.Managers;
import controllers.TaskManager;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskServer {
    private final int port;
    private final Gson gson;
    private HttpServer httpServer;
    private final TaskManager taskManager;

    public TaskServer(int port) {
        this.port = port;
        taskManager = Managers.getDefault();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager, gson));
        httpServer.start();
        System.out.println("Сервер запущен, порт: " + httpServer.getAddress().getPort());
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен");
    }

    public static void main(String[] args) throws IOException {
        TaskServer server = new TaskServer(8080);
        server.start();
    }
}