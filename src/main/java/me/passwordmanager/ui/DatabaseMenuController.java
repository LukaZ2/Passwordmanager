package me.passwordmanager.ui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import me.passwordmanager.MainApplication;
import me.passwordmanager.database.Entry;
import me.passwordmanager.file.DatabaseFile;
import me.passwordmanager.ui.util.TextLimiter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The controller of the main database menu
 */
public class DatabaseMenuController extends FxGuiController {


    @FXML public TableView<Entry> tableView;

    @FXML public TableColumn<Entry, String> titleColumn;
    @FXML public TableColumn<Entry, String> usernameColumn;
    @FXML public TableColumn<Entry, String> urlColumn;
    @FXML public TableColumn<Entry, String> descriptionColumn;
    @FXML public TableColumn<Entry, String> modifiedColumn;
    @FXML public TableColumn<Entry, String> createdColumn;

    @FXML
    public TextField searchField;

    //List displayed in table
    private ArrayList<Entry> displayedList;

    //The opened database file
    private final DatabaseFile databaseFile;

    public DatabaseMenuController(DatabaseFile databaseFile) {
        this.databaseFile = databaseFile;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("dateModified"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        tableView.setRowFactory(this::rowFactory);
        displayedList = new ArrayList<>(List.copyOf(databaseFile.getDatabase().getEntries()));
        tableView.setItems(FXCollections.observableList(displayedList));

        initSearchbar();

        MainApplication.getInstance().setDatabaseFile(databaseFile);
    }

    public void initSearchbar() {
        searchField.setTextFormatter(TextLimiter.limit(50));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterEntries(newValue));
    }

    public void filterEntries(String search) {
        displayedList.clear();
        displayedList.addAll(databaseFile.getDatabase().getEntries()
                .stream()
                .filter(entry -> filterEntry(entry, search))
                .toList()
        );
        tableView.refresh();
    }

    /**
     * Check whether any field in an entry match the search string
     *
     * @param entry  the entry
     * @param search the search string
     * @return whether it matches
     */
    public boolean filterEntry(Entry entry, String search) {
        return Stream.of(
                entry.getTitle(),
                entry.getUsername(),
                entry.getPassword(),
                entry.getUrl(),
                entry.getDescription()
        ).anyMatch(s -> s.toLowerCase().contains(search.toLowerCase()));
    }

    //Row factory for JavaFx to create interactable rows (Right-click)
    public TableRow<Entry> rowFactory(TableView<Entry> param) {
        var row = new TableRow<Entry>();
        row.itemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null) return;
            row.setContextMenu(createContextMenu(newValue));
        });
        return row;
    }

    //Create the context menu on right-click
    public ContextMenu createContextMenu(Entry entry) {
        var contextMenu = new ContextMenu();

        var editItem = new MenuItem("Edit entry");
        editItem.setOnAction(event -> {
            new CreateNewEntryMenuController(entry, entry1 -> {
                tableView.refresh();
                wrappedSave();
            }).display(createModalSubstage());
        });

        var deleteItem = new MenuItem("Delete entry");
        deleteItem.setOnAction(event -> {
            tableView.getItems().remove(entry);
            databaseFile.getDatabase().getEntries().remove(entry);
            wrappedSave();
        });

        contextMenu.getItems().addAll(
                createCopyClipboardItem("username", entry.getUsername()),
                createCopyClipboardItem("password", entry.getPassword()),
                createCopyClipboardItem("url", entry.getUrl()),
                editItem,
                deleteItem
        );
        return contextMenu;
    }

    /**
     * Create a MenuItem which sets the system clipboard to a value
     *
     * @param name  the name of the menu item
     * @param value the value
     * @return the menu item
     */
    public MenuItem createCopyClipboardItem(String name, String value) {
        var menuItem = new MenuItem("Copy " + name);
        menuItem.setOnAction(event -> {
            MainApplication.setClipboard(value);
        });
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

    //Called when create new entry is clicked, opens the create menu
    @FXML
    public void onClickCreateNew(ActionEvent actionEvent) {
        new CreateNewEntryMenuController(this::handleCreateNewEntry).display(createModalSubstage());
    }

    //Called when a new entry is created through the create new entry menu
    //Adds entry to database and displayed entries
    public void handleCreateNewEntry(Entry entry) {
        databaseFile.getDatabase().getEntries().add(entry);
        tableView.getItems().add(entry);
        wrappedSave();
    }

    //Catch exceptions and save
    public void wrappedSave() {
        try {
            databaseFile.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
