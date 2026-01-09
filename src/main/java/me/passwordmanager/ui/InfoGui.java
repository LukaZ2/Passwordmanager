package me.passwordmanager.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.passwordmanager.MainApplication;

public class InfoGui implements IDisplayableGui {

    private final String text;
    private final String buttonText;

    public InfoGui(String text) {
        this(text, "Ok");
    }

    public InfoGui(String text, String buttonText) {
        this.text = text;
        this.buttonText = buttonText;
    }

    @Override
    public void display(Stage parent) {
        var stage = new Stage();
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setResizable(false);

        var root = new VBox();
        root.alignmentProperty().setValue(Pos.CENTER);
        root.setPadding(new Insets(20, 20, 20, 20));

        var textElement = new Text(text);
        root.getChildren().add(textElement);

        var button = new Button();
        button.setText(buttonText);
        button.setOnAction(actionEvent -> {
            stage.close();
        });
        root.getChildren().add(button);

        stage.setScene(new Scene(root));
        root.autosize();
        stage.show();
    }
}
