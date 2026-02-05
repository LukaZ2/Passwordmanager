package me.passwordmanager.database;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Database type.
 */
@Getter
public class Database {

    public List<Entry> entries;

    public Database() {
        this.entries = new ArrayList<>();
    }

}
