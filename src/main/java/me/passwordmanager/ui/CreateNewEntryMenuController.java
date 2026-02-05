package me.passwordmanager.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.passwordmanager.cryptography.CryptoUtils;
import me.passwordmanager.database.Entry;
import me.passwordmanager.ui.util.InfoGui;
import me.passwordmanager.ui.util.TextLimiter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

/**
 * The Create new entry controller
 */
public class CreateNewEntryMenuController extends FxGuiController {
    @FXML public TextField titleField;
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public TextField revealedPasswordField;
    @FXML public TextField urlField;
    @FXML public TextArea descriptionArea;

    private boolean passwordRevealed;

    //New entries are passed into this consumer from the parent (DatabaseMenuController)
    private final Consumer<Entry> entryConsumer;

    private final Entry entry;

    public CreateNewEntryMenuController(Consumer<Entry> entryConsumer) {
        this(new Entry(), entryConsumer);
    }

    public CreateNewEntryMenuController(Entry entry, Consumer<Entry> entryConsumer) {

        this.entry = entry;
        this.entryConsumer = entryConsumer;
    }

    @FXML
    public void initialize() {
        titleField.setTextFormatter(TextLimiter.limit(20));
        usernameField.setTextFormatter(TextLimiter.limit(50));
        passwordField.setTextFormatter(TextLimiter.limit(50));
        revealedPasswordField.setTextFormatter(TextLimiter.limit(50));
        urlField.setTextFormatter(TextLimiter.limit(50));

        titleField.setText(entry.getTitle());
        usernameField.setText(entry.getUsername());
        passwordField.setText(entry.getPassword());
        urlField.setText(entry.getUrl());
        descriptionArea.setText(entry.getDescription());

        passwordRevealed = false;
    }

    @Override
    public String xmlFileName() {
        return "create-new-entry.fxml";
    }

    @Override
    public String initialTitle() {
        return "Create/Modify database entry";
    }

    //Called when the create new entry button is clicked
    @FXML
    public void onClickCreate(ActionEvent actionEvent) {
        //Check title validity
        if(titleField.getText().isBlank()) {
            new InfoGui("The title of the entry cannot be blank!").display(getStage());
            return;
        }
        //Check url validity
        if(!urlField.getText().isBlank() && !checkUrl(urlField.getText())) {
            new InfoGui("Please enter a valid url!").display(getStage());
            return;
        }
        entry.setTitle(titleField.getText());
        entry.setUsername(usernameField.getText());
        entry.setPassword(getPassword());
        entry.setUrl(urlField.getText());
        entry.setDescription(descriptionArea.getText());
        entry.setDateModified(System.currentTimeMillis());

        //Pass created entry to database menu controller
        entryConsumer.accept(entry);
        getStage().close();
    }

    //Called when generate button is clicked and opens the generate menu
    @FXML
    public void onClickedGenerate(ActionEvent actionEvent) {
        var stage = createModalSubstage();
        stage.setTitle("Generate new password");
        var root = new VBox();

        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(10);
        root.setPrefWidth(500);
        root.setAlignment(Pos.CENTER);

        var label = new Label("Password length: ");
        var label2 = new Label();

        var slider = new Slider();
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.setMin(8);
        slider.setMax(50);
        slider.setSnapToTicks(true);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label2.setText(String.valueOf(newValue.intValue()));
        });
        slider.adjustValue(15);

        var button = new Button("Generate");
        button.setOnAction(event -> {
            passwordField.setText(CryptoUtils.generateRandomPassword((int) slider.getValue()));
            stage.close();
        });

        root.getChildren().addAll(new HBox(label, label2), slider, button);
        stage.setScene(new Scene(root));
        stage.show();
    }

    //Called when the reveal button is clicked and toggles password reveal
    @FXML
    public void onClickRevealPassword(ActionEvent actionEvent) {
        passwordRevealed = !passwordRevealed;
        passwordField.setVisible(!passwordRevealed);
        revealedPasswordField.setVisible(passwordRevealed);

        if(passwordRevealed) {
            revealedPasswordField.setText(passwordField.getText());
            return;
        }
        passwordField.setText(revealedPasswordField.getText());
    }

    private String getPassword() {
        return passwordRevealed ? revealedPasswordField.getText() : passwordField.getText();
    }

    /**
     * Check validity of a url
     *
     * @param url the url
     * @return whether url is valid
     */
    public static boolean checkUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
