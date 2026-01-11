package me.passwordmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.database.Database;
import me.passwordmanager.file.FileUtils;
import me.passwordmanager.file.StreamUtils;
import me.passwordmanager.network.NetworkHandler;
import me.passwordmanager.ui.util.FxGuiHandler;
import me.passwordmanager.ui.MainMenuController;
import me.passwordmanager.file.DatabaseFile;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.Date;
import java.util.regex.Pattern;

public class MainApplication extends Application {

    public static final Pattern STANDARD_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_ ]+");
    public static final String APPLICATION_TITLE = "Passwordmanager";
    public static final String DATABASE_FILE_EXTENSION = "pwd";
    public static final String RECENT_DATABASE_NAME = ".passwordmanager-recent";

    public static final int LISTEN_PORT = 23232;

    @Getter
    private static MainApplication instance;

    @Getter
    private Stage stage;

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
        this.networkHandler = new NetworkHandler();
        new Thread(() -> networkHandler.listen(LISTEN_PORT)).start();

        this.databaseFile = null;

        new MainMenuController(getRecent()).display(getStage());
    }

    public String getRecent() {
        var file = new File(RECENT_DATABASE_NAME);
        if(!file.exists()) return "";

        try {
            return StreamUtils.readAll(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRecent(String recent) {
        try(var writer = new BufferedWriter(new FileWriter(RECENT_DATABASE_NAME))) {
            writer.write(recent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDatabaseFile(DatabaseFile databaseFile) {
        this.databaseFile = databaseFile;
        setRecent(databaseFile.getFilePath());
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public Stage createSubstage() {
        return FxGuiHandler.createSubstage(getStage());
    }

    public Stage createModalSubstage() {
        return FxGuiHandler.createModalSubstage(getStage());
    }

    public static String getFormattedDate(long unixTimeMs) {
        var date = new Date();
        date.setTime(unixTimeMs);
        return date.toString();
    }

    public static void setClipboard(String string) {
        var selection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}
