package me.passwordmanager.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Entry {

    private String name;
    private String description;
    private String password;
    private String url;
    private int[] tags;
    private boolean deleted;

    private long dateCreated;
    private long dateUpdated;

}
