package me.passwordmanager.ui;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.MainApplication;

public abstract class FxGuiController {

    @Getter
    private Stage stage;

    public abstract String xmlFileName();
    public abstract String initialTitle();
    public abstract int initialWidth();
    public abstract int initialHeight();

    private void setStageProperties(Stage stage) {
        if(initialTitle() != null) stage.setTitle(MainApplication.APPLICATION_TITLE + " - " + initialTitle());
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        setStageProperties(stage);
    }
}
