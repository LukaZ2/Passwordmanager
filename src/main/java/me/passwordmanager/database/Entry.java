package me.passwordmanager.database;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.passwordmanager.MainApplication;

@Getter
@Setter
@AllArgsConstructor
public class Entry {

    public String title;
    public String username;
    public String password;
    public String url;
    public String description;
    public int[] categories;
    public boolean deleted;

    public long dateCreated;
    public long dateModified;
    

    public ObservableValue<String> dateCreatedProperty() {
        return new ReadOnlyObjectWrapper<>(MainApplication.getFormattedDate(dateCreated));
    }
    
    public ObservableValue<String> dateModifiedProperty() {
        return new ReadOnlyObjectWrapper<>(MainApplication.getFormattedDate(dateModified));
    }

}
