package me.passwordmanager.database;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.MainApplication;

/**
 * The type Entry.
 */
@Getter
@Setter
@AllArgsConstructor
public class Entry {

    public String title;
    public String username;
    public String password;
    public String url;
    public String description;

    public long dateCreated;
    public long dateModified;

    public Entry() {
        title = "";
        username = "";
        password = "";
        url = "";
        description = "";
        dateCreated = System.currentTimeMillis();
        dateModified = System.currentTimeMillis();
    }


    /**
     * Required by JavaFX to get updatable tables
     *
     * @return date created String
     */
    public ObservableValue<String> dateCreatedProperty() {
        return new ReadOnlyObjectWrapper<>(MainApplication.getFormattedDate(dateCreated));
    }

    /**
     * Required by JavaFX to get updatable tables
     *
     * @return date modified String
     */
    public ObservableValue<String> dateModifiedProperty() {
        return new ReadOnlyObjectWrapper<>(MainApplication.getFormattedDate(dateModified));
    }

}
