package dev.itltcanz.medema.config;

import dev.itltcanz.medema.javafx.Controller;
import dev.itltcanz.medema.logic.ModuleTracker;
import dev.itltcanz.medema.logic.Module;
import dev.itltcanz.medema.services.ModuleService;
import javafx.application.Platform;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ModuleTrackerConfig {
    public static Object threadLock = new Object();
    public static Object getThreadLock() {
        return threadLock;
    }

    private static ModuleTracker createModuleTracker(Controller controller, String ip, String id, String location) {
        Module module = new Module(ip, 23, id, location);
        ModuleService.addModule(module.getId(), module.getLocation());
        return new ModuleTracker(controller, getThreadLock(), module);
    }

    public static List<ModuleTracker> createModuleTrackers(Controller controller) throws IOException {
        checkConfig(controller);
        List<ModuleTracker> moduleTrackerList = new ArrayList<>();
        try {
            String content = Files.readString(Paths.get("./config.json"));
            //Отметание символа кодировки
            if (content.startsWith("\uFEFF")) {
                content = content.substring(1);
            }
            JSONObject jsonObject = new JSONObject(content);
            JSONArray modules = jsonObject.getJSONArray("modules");
            // Создание экземпляров ModuleTracker для каждого модуля
            for (int i = 0; i < modules.length(); i++) {
                JSONObject module = modules.getJSONObject(i);
                String ip = module.getString("ip");
                String id = module.getString("id");
                String location = module.getString("location");
                ModuleTracker moduleTracker = ModuleTrackerConfig.createModuleTracker(controller, ip, id, location);
                moduleTrackerList.add(moduleTracker);
            }
        } catch (JSONException e) {
            String headerText = "Ошибка инициализации!";
            String contentText = "Ошибка при чтении данных из конфигурационного файла.";
            Platform.runLater(() -> controller.viewError(headerText, contentText));
        }
        return moduleTrackerList;
    }

    private static void checkConfig(Controller controller) {
        File config = new File("./config.json");
        if (!config.exists()) {
            try {
                config.createNewFile();
            } catch (IOException e) {
                String headerText = "Ошибка создания документа!";
                String contentText = "Невозможно открыть файл!";
                Platform.runLater(() -> controller.viewError(headerText, contentText));
            }
        }
    }
}