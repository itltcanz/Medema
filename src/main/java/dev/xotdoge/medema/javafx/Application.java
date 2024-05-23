package dev.xotdoge.medema.javafx;

import atlantafx.base.theme.PrimerLight;
import dev.xotdoge.medema.config.ModuleTrackerConfig;
import dev.xotdoge.medema.logic.ModuleTracker;
import dev.xotdoge.medema.config.HibernateUtil;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Application extends javafx.application.Application {
    private List<ModuleTracker> moduleTrackers;

    @Override
    public void start(Stage stage) throws IOException {
        javafx.application.Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Controller controller = fxmlLoader.getController();
        stage.setTitle("Medema");
        stage.getIcons().add(new Image("/image/metal-detector.png"));
        stage.setScene(scene);
        stage.show();

        moduleTrackers = ModuleTrackerConfig.createModuleTrackers(controller);

        for (ModuleTracker moduleTracker : moduleTrackers) {
            CompletableFuture.runAsync(moduleTracker::runModule);
            controller.addModuleStatus(moduleTracker.getModule().getId(), moduleTracker.getModule().getLocation());
        }
    }

    @Override
    public void stop() {
        for (ModuleTracker moduleTracker : moduleTrackers) {
            moduleTracker.stop();
        }
        HibernateUtil.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}