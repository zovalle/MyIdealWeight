package com.androidbasico.myidealweight.models;

public class BodyMassIndexLevel {
    private double downLimit;
    private double upLimit;
    private int resourceId;
    private int iconId;

    public BodyMassIndexLevel(double downLimit, double upLimit, int resourceId, int iconId) {
        this.downLimit = downLimit;
        this.upLimit = upLimit;
        this.resourceId = resourceId;
        this.iconId = iconId;
    }

    public double getDownLimit() {
        return downLimit;
    }

    public double getUpLimit() {
        return upLimit;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getIconId() { return iconId; }
}
