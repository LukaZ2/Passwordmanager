module me.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires static lombok;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires java.datatransfer;
    requires java.desktop;
    requires java.base;

    opens me.passwordmanager to javafx.fxml;
    exports me.passwordmanager;
    exports me.passwordmanager.ui;
    exports me.passwordmanager.database;
    exports me.passwordmanager.ui.util;
    exports me.passwordmanager.network.requests;
}