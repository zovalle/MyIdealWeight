package com.androidbasico.myidealweight.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.constraint.solver.SolverVariable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.models.BodyMassIndexLevel;
import com.androidbasico.myidealweight.models.BodyMassIndexTable;
import com.androidbasico.myidealweight.models.RecordEntry;

import java.text.DecimalFormat;

public class ResultsActivity extends AppCompatActivity {
    private TextView tvIdealWeightLorentz;
    private TextView tvIdealWeightBroca;
    private TextView tvIdealWeightMetlife;
    private TextView tvIdealWeightPerrault;
    private TextView tvIdealWeightWandervael;
    private TextView tvIdealWeightIMC;
    private RecordEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        entry = (RecordEntry) intent.getSerializableExtra("ENTRY");

        tvIdealWeightLorentz = (TextView) findViewById(R.id.text_view_result_lorentz);
        tvIdealWeightBroca = (TextView) findViewById(R.id.text_view_result_broca);
        tvIdealWeightMetlife = (TextView) findViewById(R.id.text_view_result_metlife);
        tvIdealWeightPerrault = (TextView) findViewById(R.id.text_view_result_perrault);
        tvIdealWeightWandervael = (TextView) findViewById(R.id.text_view_result_wandervael);
        tvIdealWeightIMC = (TextView) findViewById(R.id.text_view_result_imc);

        DecimalFormat df = new DecimalFormat("###.##");

        tvIdealWeightLorentz.setText(df.format(entry.getLorentzMethod()));
        tvIdealWeightBroca.setText(df.format(entry.getBrocaIndexMethod()));
        tvIdealWeightMetlife.setText(df.format(entry.getMetLifeMethod()));
        tvIdealWeightPerrault.setText(df.format(entry.getPerraultMethod()));
        tvIdealWeightWandervael.setText(df.format(entry.getWanDerVaelMethod()));

        TypedArray integerArray = getResources().obtainTypedArray(R.array.imc_level);
        int[] imcLevels = new int[integerArray.length()];

        for (int index = 0; index < integerArray.length(); index++) {
            imcLevels[index] = integerArray.getResourceId(index, 0);
        }

        BodyMassIndexTable table = BodyMassIndexTable.getInstance();

        for (BodyMassIndexLevel level : table.getTable()) {
            if (entry.getIMCMethod() >= level.getDownLimit() && entry.getIMCMethod() < level.getUpLimit()) {
                tvIdealWeightIMC.setText(df.format(entry.getIMCMethod()) + "  " + getResources().getString(imcLevels[level.getResourceId()]));
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, R.id.item_share, Menu.NONE, "Share")
                .setIcon(R.drawable.ic_menu_share)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBodyText = getResources().getString(R.string.label_my_current_data) + "\n"
                + getResources().getString(R.string.input_age) + ": " + entry.getAge() + " (" + entry.getGender() + ")\n"
                + getResources().getString(R.string.input_height) + ": " + entry.getHeight() + " " + entry.getHeightUnit() + "\n"
                + getResources().getString(R.string.input_weight) + ": " + entry.getWeight() + " " + entry.getWeightUnit() + "\n\n"
                + getResources().getString(R.string.label_result) + "\n"
                + "Lorentz: " + tvIdealWeightLorentz.getText().toString() + "\n"
                + "Broca: " + tvIdealWeightBroca.getText().toString() + "\n"
                + "MetLife: " + tvIdealWeightMetlife.getText().toString() + "\n"
                + "Perrault: " + tvIdealWeightPerrault.getText().toString() + "\n"
                + "Wan der Vael: " + tvIdealWeightWandervael.getText().toString() + "\n"
                + getResources().getString(R.string.label_imc_method) + "+: " + tvIdealWeightIMC.getText().toString();

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.label_share_subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.label_share_message)));
    }
}
