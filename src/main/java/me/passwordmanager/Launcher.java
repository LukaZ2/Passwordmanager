package me.passwordmanager;

import javafx.application.Application;

public class Launcher {

    public static String arg1 = null;

    public static void main(String[] args) {
        if(args.length == 1) {
            arg1 = args[0];
            System.out.printf("Launching in crash test mode: %s%n", arg1);
        }
        Application.launch(MainApplication.class, args);
    }
}
