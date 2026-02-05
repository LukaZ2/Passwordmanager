package me.passwordmanager;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import me.passwordmanager.database.Database;
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

    //Name regex allowed for files
    public static final Pattern STANDARD_NAME_PATTERN = Pattern.compile("[a-zA-Z0-9\\-_ ]+");

    //Application title in JavaFx
    public static final String APPLICATION_TITLE = "Passwordmanager";

    //Database file extension
    public static final String DATABASE_FILE_EXTENSION = "pwd";

    //The file where the recently opened database is stored
    public static final String RECENT_DATABASE_NAME = ".passwordmanager-recent";

    //The port on which the http server listens on
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
        //Set the main instance
        instance = this;

        //Set the main stage
        this.stage = stage;

        //Start the HTTP server and start listening in a separate thread
        this.networkHandler = new NetworkHandler();
        new Thread(() -> networkHandler.listen(LISTEN_PORT)).start();

        this.databaseFile = null;

        //Open main menu
        new MainMenuController(getRecent()).display(getStage());
    }

    /**
     * Gets the most recently opened database path from the file
     *
     * @return the path
     */
    public String getRecent() {
        var file = new File(RECENT_DATABASE_NAME);
        if(!file.exists()) return "";

        try {
            return StreamUtils.readAll(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write the most recently opened database to file
     *
     * @param recent the path
     */
    public void setRecent(String recent) {
        try(var writer = new BufferedWriter(new FileWriter(RECENT_DATABASE_NAME))) {
            writer.write(recent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the opened database file
     *
     * @param databaseFile the database file
     */
    public void setDatabaseFile(DatabaseFile databaseFile) {
        this.databaseFile = databaseFile;
        setRecent(databaseFile.getFilePath());
    }

    //Stop program
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * Create modal substage with main stage as parent
     *
     * @return the modal substage
     */
    public Stage createModalSubstage() {
        return FxGuiHandler.createModalSubstage(getStage());
    }

    /**
     * Gets formatted date from unix time
     *
     * @param unixTimeMs the unix timestamp
     * @return the formatted date
     */
    public static String getFormattedDate(long unixTimeMs) {
        var date = new Date();
        date.setTime(unixTimeMs);
        return date.toString();
    }

    /**
     * Sets system clipboard
     *
     * @param string the content to be set
     */
    public static void setClipboard(String string) {
        var selection = new StringSelection(string);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}
