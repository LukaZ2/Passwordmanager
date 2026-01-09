package me.passwordmanager.ui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
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
        tableView.setRowFactory(param -> {
            var tableRow = new TableRow<Entry>();
            tableRow.setContextMenu(createContextMenu(tableRow));
            return tableRow;
        });
    }

    public ObservableValue<String> mapCategories(TableColumn.CellDataFeatures<Entry, String> param) {
        return new ReadOnlyObjectWrapper<>(
                Arrays.stream(param.getValue().getCategories())
                        .mapToObj(databaseFile.getDatabase()::getCategoryName)
                        .toList().toString()
        );
    }

    public ContextMenu createContextMenu(TableRow<Entry> row) {
        var contextMenu = new ContextMenu();

        var deleteItem = new MenuItem("Delete entry");

        contextMenu.getItems().addAll(
                createCopyClipboardItem("username", row.getItem().getUsername()),
                createCopyClipboardItem("password", row.getItem().getPassword()),
                createCopyClipboardItem("url", row.getItem().getUrl()),
                deleteItem
        );
        return contextMenu;
    }

    public MenuItem createCopyClipboardItem(String name, String value) {
        var menuItem = new MenuItem("Copy " + name);
        return menuItem;
    }

    @Override
    public String xmlFileName() {
        return "database.fxml";
    }

    @Override
    public String initialTitle() {
        return databaseFile.getFileName();
    }

    @FXML
    public void onClickCreateNew(ActionEvent actionEvent) {
    }
}
