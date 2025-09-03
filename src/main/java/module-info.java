module me.passwordmanager.passwordmanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens me.passwordmanager to javafx.fxml;
    exports me.passwordmanager;
}