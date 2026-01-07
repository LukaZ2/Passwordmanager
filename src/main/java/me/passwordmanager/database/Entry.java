package me.passwordmanager.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Entry {

    public String name;
    public String description;
    public String password;
    public String url;
    public int[] categories;
    public boolean deleted;

    public long dateCreated;
    public long dateUpdated;

}
