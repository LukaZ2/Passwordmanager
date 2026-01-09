package me.passwordmanager.database;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Database {

    public List<Entry> entries;
    public Map<Integer, String> categoryMap;

    public Database() {
        this.entries = new ArrayList<>();
        this.categoryMap = new HashMap<>();
    }

    public String getCategoryName(int id) {
        return categoryMap.get(id);
    }

}
