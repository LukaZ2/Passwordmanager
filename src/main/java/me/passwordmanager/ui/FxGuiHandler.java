package me.passwordmanager.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.passwordmanager.MainApplication;

import java.io.IOException;

public class FxGuiHandler {

    public void openGui(FxGuiController gui) {
        openGui(MainApplication.getInstance().getStage(), gui);
    }
    public void openGui(Stage stage, FxGuiController gui) {
        var loader = new FXMLLoader(MainApplication.class.getResource(gui.xmlFileName()));
        loader.setControllerFactory(type -> gui);
        try {
            stage.setScene(new Scene(loader.load(), gui.initialWidth(), gui.initialHeight()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gui.setStageProperties(stage);
    }

}
