package me.passwordmanager;

import javafx.application.Application;
import me.passwordmanager.file.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Launcher {

    public static final String CRASH_TEST_FILENAME = "CRASH_TEST.txt";
    public static String crashTest = null;

    public static void main(String[] args) throws IOException {

        var file = new File(CRASH_TEST_FILENAME);
        if(file.exists()) {
            crashTest = StreamUtils.readAll(new FileInputStream(CRASH_TEST_FILENAME));
        }

        Application.launch(MainApplication.class, args);
    }
}
