package me.passwordmanager.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.MainApplication;

import java.io.IOException;

public abstract class FxGuiController implements IDisplayableGui {

    @Getter
    private Stage stage;

    public abstract String xmlFileName();
    public abstract String initialTitle();
//    public abstract int initialWidth();
//    public abstract int initialHeight();

    private void setStageProperties(Stage stage) {
        if(initialTitle() != null) stage.setTitle(MainApplication.APPLICATION_TITLE + " - " + initialTitle());
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        setStageProperties(stage);
    }

    public Stage createSubstage() {
        return FxGuiHandler.createSubstage(getStage());
    }

    public Stage createModalSubstage() {
        return FxGuiHandler.createModalSubstage(getStage());
    }

    @Override
    public void display(Stage stage) {
        var loader = new FXMLLoader(MainApplication.class.getResource(xmlFileName()));
        loader.setControllerFactory(type -> this);
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setStage(stage);
    }
}
