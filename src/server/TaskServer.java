package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import controllers.Managers;
import controllers.TaskManager;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TaskServer {
    private final int port;
    private final Gson gson;
    private HttpServer httpServer;
    private final TaskManager taskManager;

    public TaskServer(int port) {
        this.port = port;
        taskManager = Managers.getDefault();
        this.gson = Managers.getDefaultGson(null);
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