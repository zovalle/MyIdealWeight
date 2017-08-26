package com.androidbasico.myidealweight.models;

import android.content.res.Resources;
import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.activities.MainActivity;
import java.io.Serializable;
import java.text.DecimalFormat;

public class RecordEntry implements Serializable {
    private String date;
    private Height height;
    private String heightUnit;
    private int weight;
    private String weightUnit;
    private int age;
    private String gender;

    private int convertedHeight;
    private int convertedWeight;

    private double IMCMethod;
    private double brocaIndexMethod;
    private double metLifeMethod;
    private double lorentzMethod;
    private double perraultMethod;
    private double wanDerVaelMethod;

    private final double POUNDS_IN_ONE_KILOGRAM = 2.20462;
    private final double CENTIMETERS_IN_ONE_METER = 100;
    private final double FEET_IN_ONE_METER = 3.28084;

    private RecordEntry(RecordEntryBuilder builder) {
        date = builder.date;
        height = builder.height;
        heightUnit = builder.heightUnit;
        weight = builder.weight;
        weightUnit = builder.weightUnit;
        age = builder.age;
        gender = builder.gender;

        convertValues();
        calculateIdealWeight();
    }

    public String getDate() {
        return date;
    }

    public Height getHeight() { return height; }

    public String getHeightUnit() {
        return heightUnit;
    }

    public int getWeight() {
        return weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public double getIMCMethod() {
        return IMCMethod;
    }

    public double getBrocaIndexMethod() {
        return brocaIndexMethod;
    }

    public double getMetLifeMethod() {
        return metLifeMethod;
    }

    public double getLorentzMethod() {
        return lorentzMethod;
    }

    public double getPerraultMethod() {
        return perraultMethod;
    }

    public double getWanDerVaelMethod() {
        return wanDerVaelMethod;
    }

    public String toString() {
        Resources res = MainActivity.getContext().getResources();
        DecimalFormat df = new DecimalFormat("###.##");

        String text =  res.getString(R.string.label_my_current_data) + " : " + date + "\n"
                + res.getString(R.string.input_age) + ": " + age + " (" + gender + ")\n"
                + res.getString(R.string.input_height) + ": "
                + ((!heightUnit.equals(res.getString(R.string.unit_feet))) ? height.getFlatValue() + " " + heightUnit : height.getFeet() + "' " + height.getInches() + "\"") + "\n"
                + res.getString(R.string.input_weight) + ": " + weight + " " + weightUnit + "\n\n"
                + res.getString(R.string.label_result) + "\n"
                + "Lorentz: " + df.format(lorentzMethod) + "\n"
                + "Broca: " + df.format(brocaIndexMethod) + "\n"
                + "MetLife: " + df.format(metLifeMethod) + "\n"
                + "Perrault: " + df.format(perraultMethod) + "\n"
                + "Wan der Vael: " + df.format(wanDerVaelMethod);

        return text;
    }

    private void convertValues() {
        Resources res = MainActivity.getContext().getResources();

        if (weightUnit.equals(res.getString(R.string.unit_pound))) {
            convertedWeight = (int) (weight / POUNDS_IN_ONE_KILOGRAM);
        } else {
            convertedWeight = weight;
        }

        if (heightUnit.equals(res.getString(R.string.unit_centimeter))) {
            convertedHeight = (int) height.getFlatValue();
        } else if (heightUnit.equals(res.getString(R.string.unit_meter))) {
            convertedHeight = (int) (height.getFlatValue() * CENTIMETERS_IN_ONE_METER);
        } else if (heightUnit.equals(res.getString(R.string.unit_feet))) {
            convertedHeight = (int) ((height.getFeet() + height.getInches() / 12.0) / FEET_IN_ONE_METER * CENTIMETERS_IN_ONE_METER);
        }
    }

    private void calculateIdealWeight() {
        Resources res = MainActivity.getContext().getResources();
        IMCMethod = convertedWeight / Math.pow(convertedHeight / 100.0, 2);
        brocaIndexMethod = convertedHeight - 100.0;
        metLifeMethod = 50 + (0.75 * (convertedHeight - 150));
        lorentzMethod = (convertedHeight - 100.0) - (convertedHeight - 150.0) / ((gender.equals(res.getString(R.string.input_male))) ? 4 : 2);
        perraultMethod = convertedHeight - 100 + ((age / 10.0) * 0.9);
        wanDerVaelMethod = ((convertedHeight - 150.0) * ((gender.equals(res.getString(R.string.input_male))) ? 0.75 : 0.60) + 50);

        if (weightUnit.equals(MainActivity.getContext().getResources().getString(R.string.unit_pound))) {
            brocaIndexMethod *= POUNDS_IN_ONE_KILOGRAM;
            metLifeMethod *= POUNDS_IN_ONE_KILOGRAM;
            lorentzMethod *= POUNDS_IN_ONE_KILOGRAM;
            perraultMethod *= POUNDS_IN_ONE_KILOGRAM;
            wanDerVaelMethod *= POUNDS_IN_ONE_KILOGRAM;
        }
    }

    public static class RecordEntryBuilder {
        private String date;
        private Height height;
        private String heightUnit;
        private int weight;
        private String weightUnit;
        private int age;
        private String gender;

        public RecordEntryBuilder() {

        }

        public RecordEntryBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public RecordEntryBuilder setHeight(Height height) {
            this.height = height;
            return this;
        }

        public RecordEntryBuilder setHeightUnit(String heightUnit) {
            this.heightUnit = heightUnit;
            return this;
        }

        public RecordEntryBuilder setWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public RecordEntryBuilder setWeightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
            return this;
        }

        public RecordEntryBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public RecordEntryBuilder setGender(String gender) {
            this.gender = gender;
            return this;
        }

        public RecordEntry build() {
            return new RecordEntry(this);
        }
    }
}
