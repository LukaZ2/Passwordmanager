package me.passwordmanager.ui;

import javafx.stage.Stage;
import me.passwordmanager.MainApplication;

public abstract class FxGui {
    public abstract String xmlFileName();
    public abstract String initialTitle();
    public abstract int initialWidth();
    public abstract int initialHeight();

    public void setStageProperties(Stage stage) {
        if(initialTitle() != null) stage.setTitle(MainApplication.APPLICATION_TITLE + " - " + initialTitle());
        stage.show();
    }
}
