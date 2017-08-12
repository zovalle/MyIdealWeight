package com.androidbasico.myidealweight.models;

import java.util.ArrayList;
import java.util.List;

public class BodyMassIndexTable {
    private static BodyMassIndexTable instance;
    private static List<BodyMassIndexLevel> table;

    private BodyMassIndexTable() {
        int index = 0;
        table = new ArrayList<>();
        table.add(new BodyMassIndexLevel(0, 16, index++));
        table.add(new BodyMassIndexLevel(16, 17, index++));
        table.add(new BodyMassIndexLevel(17, 18.5, index++));
        table.add(new BodyMassIndexLevel(18.5, 25, index++));
        table.add(new BodyMassIndexLevel(25, 30, index++));
        table.add(new BodyMassIndexLevel(30, 35, index++));
        table.add(new BodyMassIndexLevel(35, 40, index++));
        table.add(new BodyMassIndexLevel(40, 999, index++));
    }

    public static BodyMassIndexTable getInstance() {
        if (instance == null) {
            instance = new BodyMassIndexTable();
        }

        return instance;
    }

    public List<BodyMassIndexLevel> getTable() {
        return table;
    }
}
