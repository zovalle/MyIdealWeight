package com.androidbasico.myidealweight.models;

public class BodyMassIndexLevel {
    private double downLimit;
    private double upLimit;
    private int resourceId;

    public BodyMassIndexLevel(double downLimit, double upLimit, int resourceId) {
        this.downLimit = downLimit;
        this.upLimit = upLimit;
        this.resourceId = resourceId;
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
}
