module me.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires java.datatransfer;
    requires java.desktop;

    opens me.passwordmanager to javafx.fxml;
    exports me.passwordmanager;
    exports me.passwordmanager.ui;
    exports me.passwordmanager.database;
    exports me.passwordmanager.ui.util;
}