package com.androidbasico.myidealweight.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.data.DatabaseHandler;
import com.androidbasico.myidealweight.models.RecordEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static Context context;
    private TextInputLayout editAge;
    private TextInputLayout editHeight;
    private TextInputLayout editWeight;
    private RadioGroup radioGender;
    private Button buttonGetResults;
    private Spinner spinnerWeightUnit;
    private Spinner spinnerHeightUnit;
    private String[] heightUnits;
    private String[] weightUnits;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editAge = (TextInputLayout) findViewById(R.id.edit_text_age);
        editHeight = (TextInputLayout) findViewById(R.id.edit_text_height);
        editWeight = (TextInputLayout) findViewById(R.id.edit_text_weight);

        radioGender = (RadioGroup) findViewById(R.id.radio_group_gender);
        buttonGetResults = (Button) findViewById(R.id.button_get_results);
        spinnerHeightUnit = (Spinner) findViewById(R.id.spinner_height_unit);
        spinnerWeightUnit = (Spinner) findViewById(R.id.spinner_weight_unit);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        populateArrays();

        ArrayAdapter<String> adapterWeightUnit = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, weightUnits);
        ArrayAdapter<String> adapterHeightUnit = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, heightUnits);

        spinnerWeightUnit.setAdapter(adapterWeightUnit);
        spinnerHeightUnit.setAdapter(adapterHeightUnit);

        buttonGetResults.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_history:
                intent = new Intent(this, HistoryActivity.class);
                break;
            case R.id.nav_settings:
                //intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.nav_about:
                intent = new Intent(this, AboutActivity.class);
                break;
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        if (isEditTextValid(editHeight) && isEditTextValid(editWeight) &&  isEditTextValid(editAge)) {
            if (radioGender.getCheckedRadioButtonId() != -1) {
                progressBar.setVisibility(ProgressBar.VISIBLE);

                DatabaseHandler dbHandler = new DatabaseHandler(this);
                int age = Integer.parseInt(editAge.getEditText().getText().toString());
                double height = Double.parseDouble(editHeight.getEditText().getText().toString());
                int weight = Integer.parseInt(editWeight.getEditText().getText().toString());
                String gender = (radioGender.getCheckedRadioButtonId() == R.id.radio_button_gender_male)
                        ? getResources().getString(R.string.input_male) : getResources().getString(R.string.input_female);
                String heightUnit = spinnerHeightUnit.getSelectedItem().toString();
                String weightUnit = spinnerWeightUnit.getSelectedItem().toString();

                RecordEntry entry = (new RecordEntry.RecordEntryBuilder())
                        .setAge(age)
                        .setGender(gender)
                        .setHeight(height)
                        .setHeightUnit(heightUnit)
                        .setWeight(weight)
                        .setWeightUnit(weightUnit)
                        .setDate((new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date()))
                        .build();

                dbHandler.addEntry(entry);

                // Settings
                //    Language
                //    Default measure and mass units
                //      * Height
                //      * Weight
                //    Store results in database
                //    Max number of records to be stored
                //    Erase database content
                //
                //    @@ SharedPreferences save settings @@
                // About
                //    Information about the training
                // Splash screen
                //    Get wallpaper from already installed app


                Intent intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("ENTRY", entry);

                progressBar.setVisibility(ProgressBar.INVISIBLE);

                startActivity(intent);
            } else {
                Toast.makeText(this, getResources().getString(R.string.validator_gender), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEditTextValid(TextInputLayout editText) {
        if (editText == null || editText.getEditText().getText().toString().isEmpty() || editText.getEditText().getText().toString().length() == 0) {
            editText.setError(editText.getHint().toString() + " " + getResources().getString(R.string.validator_is_required));
            editText.requestFocus();
            return false;
        } else {
            if (Double.parseDouble(editText.getEditText().getText().toString()) <= 0) {
                editText.setError(editText.getHint().toString() + " " + getResources().getString(R.string.validator_positive_value));
                editText.requestFocus();
                return false;
            } else {
                return true;
            }
        }
    }

    private void populateArrays() {
        int index = 0;
        heightUnits = new String[3];
        heightUnits[index++] = getResources().getString(R.string.unit_meter);
        heightUnits[index++] = getResources().getString(R.string.unit_centimeter);
        heightUnits[index++] = getResources().getString(R.string.unit_feet);

        index = 0;
        weightUnits = new String[2];
        weightUnits[index++] = getResources().getString(R.string.unit_kilogram);
        weightUnits[index++] = getResources().getString(R.string.unit_pound);
    }

    public static Context getContext() {
        return context;
    }
}
