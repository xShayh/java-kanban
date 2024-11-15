package controllers;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class Managers {
    public static TaskManager getDefault() {
        HistoryManager historyManager = getDefaultHistoryManager();
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static Gson getDefaultGson(Consumer<GsonBuilder> customSetup) {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        if (customSetup != null) {
            customSetup.accept(gsonBuilder);
        }
        return gsonBuilder.create();
    }
}