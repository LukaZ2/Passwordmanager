module me.passwordmanager.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires me.passwordmanager.passwordmanager;
    requires com.google.gson;

    opens me.passwordmanager to javafx.fxml;
    exports me.passwordmanager;
}