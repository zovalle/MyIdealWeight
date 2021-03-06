package com.androidbasico.myidealweight.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.activities.MainActivity;
import com.androidbasico.myidealweight.models.Height;
import com.androidbasico.myidealweight.models.RecordEntry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "myIdealWeightHistory";
    private static final String TABLE_NAME = "resultEntries";

    private static final String DATE = "date";
    private static final String HEIGHT = "height";
    private static final String FEET = "feet";
    private static final String INCHES = "inches";
    private static final String HEIGHT_UNIT = "heightUnit";
    private static final String WEIGHT = "weight";
    private static final String WEIGHT_UNIT = "weightUnit";
    private static final String AGE = "age";
    private static final String GENDER = "gender";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + DATE + " TEXT, "
                + HEIGHT + " NUMERIC, "
                + FEET + " NUMERIC, "
                + INCHES + " NUMERIC, "
                + HEIGHT_UNIT + " TEXT, "
                + WEIGHT + " NUMERIC, "
                + WEIGHT_UNIT + " TEXT, "
                + AGE + " NUMERIC, "
                + GENDER + " TEXT)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(RecordEntry record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, record.getDate());
        values.put(HEIGHT, record.getHeight().getFlatValue());
        values.put(FEET, record.getHeight().getFeet());
        values.put(INCHES, record.getHeight().getInches());
        values.put(HEIGHT_UNIT, record.getHeightUnit());
        values.put(WEIGHT, record.getWeight());
        values.put(WEIGHT_UNIT, record.getWeightUnit());
        values.put(AGE, record.getAge());
        values.put(GENDER, record.getGender());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<RecordEntry> getAllEntries() {
        Resources res = MainActivity.getContext().getResources();
        List<RecordEntry> entries = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        RecordEntry record;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Height height;
        String heightUnit;

        if (cursor.moveToFirst()) {
            do {
                heightUnit = cursor.getString(cursor.getColumnIndex(DatabaseHandler.HEIGHT_UNIT));

                if (heightUnit.equals(res.getString(R.string.unit_feet))) {
                    height = new Height(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.FEET)), cursor.getInt(cursor.getColumnIndex(DatabaseHandler.INCHES)));
                } else {
                    height = new Height(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.HEIGHT)));
                }

                record = (new RecordEntry.RecordEntryBuilder()).setDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.DATE)))
                        .setHeight(height)
                        .setHeightUnit(heightUnit)
                        .setWeight(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.WEIGHT)))
                        .setWeightUnit(cursor.getString(cursor.getColumnIndex(DatabaseHandler.WEIGHT_UNIT)))
                        .setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.AGE)))
                        .setGender(cursor.getString(cursor.getColumnIndex(DatabaseHandler.GENDER)))
                        .build();

                entries.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return entries;
    }

    public int getNumberOfRows() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();

        return cursor.getCount();
    }

    public void deleteEntry(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_NAME
                      + " WHERE date = '" + date + "'";
        db.execSQL(delete);
        db.close();
    }

    public void deleteLastsEntries(int recordsToKeep) {
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_NAME
                      + " WHERE date < (SELECT MIN(date) "
                      + "                 FROM (SELECT date "
                      + "                         FROM " + TABLE_NAME
                      + "                ORDER BY date DESC "
                      + "                LIMIT " + recordsToKeep + "))";

        db.execSQL(delete);
        db.close();
    }

    public void deleteAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public RecordEntry getEntry(String date) {
        Resources res = MainActivity.getContext().getResources();
        SQLiteDatabase db = this.getReadableDatabase();
        String heightUnit;
        Height height;

        Cursor cursor = db.query(TABLE_NAME, new String[] {DATE,
                        HEIGHT,
                        FEET,
                        INCHES,
                        HEIGHT_UNIT,
                        WEIGHT,
                        WEIGHT_UNIT,
                        AGE,
                        GENDER}, DATE + "=?",
                new String[] { date }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        heightUnit = cursor.getString(cursor.getColumnIndex(DatabaseHandler.HEIGHT_UNIT));

        if (heightUnit.equals(res.getString(R.string.unit_feet))) {
            height = new Height(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.FEET)), cursor.getInt(cursor.getColumnIndex(DatabaseHandler.INCHES)));
        } else {
            height = new Height(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.HEIGHT)));
        }

        RecordEntry record = (new RecordEntry.RecordEntryBuilder()).setDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.DATE)))
                .setHeight(height)
                .setWeightUnit(cursor.getString(cursor.getColumnIndex(DatabaseHandler.HEIGHT_UNIT)))
                .setWeight(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.WEIGHT)))
                .setWeightUnit(cursor.getString(cursor.getColumnIndex(DatabaseHandler.WEIGHT_UNIT)))
                .setAge(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.AGE)))
                .setGender(cursor.getString(cursor.getColumnIndex(DatabaseHandler.GENDER)))
                .build();

        cursor.close();
        db.close();

        return record;
    }
}
