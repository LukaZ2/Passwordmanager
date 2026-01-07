package me.passwordmanager.database;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Database {

    public List<Entry> entries;
    public Map<Integer, String> categoryMap;

}
