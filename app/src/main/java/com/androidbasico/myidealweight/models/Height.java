package com.androidbasico.myidealweight.models;

import java.io.Serializable;

public class Height implements Serializable {
    private double flatValue;
    private int feet;
    private int inches;

    public Height(double flatValue) {
        this.flatValue = flatValue;
        feet = 0;
        inches = 0;
    }

    public Height(int feet, int inches) {
        this.feet = feet;
        this.inches = inches;
        flatValue = 0;
    }

    public double getFlatValue() {
        return flatValue;
    }

    public int getFeet() {
        return feet;
    }

    public int getInches() {
        return inches;
    }
}
