package com.androidbasico.myidealweight.models;

import java.util.ArrayList;
import java.util.List;

public class BodyMassIndexTable {
    private static BodyMassIndexTable instance;
    private static List<BodyMassIndexLevel> table;

    private BodyMassIndexTable() {
        int indexResId = 0;
        int indexIconId = 0;
        table = new ArrayList<>();
        table.add(new BodyMassIndexLevel(0, 16, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(16, 17, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(17, 18.5, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(18.5, 25, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(25, 30, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(30, 35, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(35, 40, indexResId++, indexIconId++));
        table.add(new BodyMassIndexLevel(40, 999, indexResId++, indexIconId++));
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
