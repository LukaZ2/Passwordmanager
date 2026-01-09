package me.passwordmanager.ui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import me.passwordmanager.database.Database;
import me.passwordmanager.database.Entry;
import me.passwordmanager.file.DatabaseFile;

import java.util.Arrays;

public class DatabaseMenuController extends FxGuiController {


    @FXML public TableView<Entry> tableView;

    @FXML public TableColumn<Entry, String> titleColumn;
    @FXML public TableColumn<Entry, String> usernameColumn;
    @FXML public TableColumn<Entry, String> urlColumn;
    @FXML public TableColumn<Entry, String> categoriesColumn;
    @FXML public TableColumn<Entry, String> descriptionColumn;
    @FXML public TableColumn<Entry, String> modifiedColumn;
    @FXML public TableColumn<Entry, String> createdColumn;

    private final DatabaseFile databaseFile;

    public DatabaseMenuController(DatabaseFile databaseFile) {
        this.databaseFile = databaseFile;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        categoriesColumn.setCellValueFactory(this::mapCategories);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        tableView.setItems(FXCollections.observableList(databaseFile.getDatabase().getEntries()));
    }

    public ObservableValue<String> mapCategories(TableColumn.CellDataFeatures<Entry, String> param) {
        return new ReadOnlyObjectWrapper<>(
                Arrays.stream(param.getValue().getCategories())
                        .mapToObj(databaseFile.getDatabase()::getCategoryName)
                        .toList().toString()
        );
    }

    @Override
    public String xmlFileName() {
        return "database.fxml";
    }

    @Override
    public String initialTitle() {
        return databaseFile.getFileName();
    }

    @Override
    public int initialWidth() {
        return 600;
    }

    @Override
    public int initialHeight() {
        return 500;
    }
}
