package me.passwordmanager.ui;

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
    private final Stage parent;

    public InfoGui(String text, Stage parent) {
        this(text, "Ok", parent);
    }

    public InfoGui(String text) {
        this(text, "Ok", MainApplication.getInstance().getStage());
    }

    public InfoGui(String text, String buttonText, Stage parent) {
        this.text = text;
        this.buttonText = buttonText;
        this.parent = parent;
    }

    @Override
    public void display() {
        var stage = FxGuiHandler.createModalSubstage(parent);

        stage.setResizable(false);
//        stage.setHeight(100);
//        stage.setWidth(200);

        var root = new VBox();
        root.alignmentProperty().setValue(Pos.CENTER);

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
