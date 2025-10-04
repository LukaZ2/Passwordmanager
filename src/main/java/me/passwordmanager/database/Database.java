package me.passwordmanager.database;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Database {

    private List<Entry> entries;
    private Map<Integer, String> categoryMap;

}
