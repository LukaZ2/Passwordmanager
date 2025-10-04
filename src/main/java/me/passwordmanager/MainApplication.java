package me.passwordmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.ui.FxGuiHandler;
import me.passwordmanager.ui.MainMenuController;
import me.passwordmanager.file.DatabaseFile;

import java.io.IOException;

public class MainApplication extends Application {

    public static final String APPLICATION_TITLE = "Passwordmanager";
    public static final String DATABASE_FILE_EXTENSION = "pwd";

    @Getter
    private static MainApplication instance;

    @Getter
    private Stage stage;

    @Getter
    private FxGuiHandler guiHandler;

    @Setter
    private DatabaseFile databaseFile;

    @Override
    public void start(Stage stage) {
        instance = this;
        this.stage = stage;
        this.guiHandler = new FxGuiHandler();

        getGuiHandler().openGui(new MainMenuController());
    }
}
