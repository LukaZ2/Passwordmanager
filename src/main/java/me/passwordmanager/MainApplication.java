package me.passwordmanager;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.database.Database;
import me.passwordmanager.network.NetworkHandler;
import me.passwordmanager.ui.FxGuiController;
import me.passwordmanager.ui.FxGuiHandler;
import me.passwordmanager.ui.MainMenuController;
import me.passwordmanager.file.DatabaseFile;

public class MainApplication extends Application {

    public static final String APPLICATION_TITLE = "Passwordmanager";
    public static final String DATABASE_FILE_EXTENSION = "pwd";

    public static final int LISTEN_PORT = 23232;

    @Getter
    private static MainApplication instance;

    @Getter
    private Stage stage;

    @Getter
    private FxGuiHandler guiHandler;

    @Setter
    @Getter
    private DatabaseFile databaseFile;

    @Getter
    private NetworkHandler networkHandler;

    public Database getDatabase() {
        return getDatabaseFile().getDatabase();
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        this.stage = stage;
        this.guiHandler = new FxGuiHandler();
        this.networkHandler = new NetworkHandler();
        new Thread(() -> networkHandler.listen(LISTEN_PORT)).start();

        String recentPath = "test";

        FxGuiController mainMenu = new MainMenuController(recentPath);
        getGuiHandler().openGui(mainMenu);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public Stage createSubstage() {
        var substage = new Stage();
        substage.initOwner(getStage());
        return substage;
    }

    public Stage createModalSubstage() {
        var substage = createSubstage();
        substage.initModality(Modality.WINDOW_MODAL);
        return substage;
    }
}
