package me.passwordmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.passwordmanager.ui.FxGuiHandler;
import me.passwordmanager.ui.MainMenuController;

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

    @Override
    public void start(Stage stage) throws IOException {
        instance = this;
        this.stage = stage;
        this.guiHandler = new FxGuiHandler();

        getGuiHandler().openGui(new MainMenuController("Test"));

//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-menu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
//        stage.setTitle(APPLICATION_TITLE);
//        stage.setScene(scene);
//        stage.show();
    }
}
