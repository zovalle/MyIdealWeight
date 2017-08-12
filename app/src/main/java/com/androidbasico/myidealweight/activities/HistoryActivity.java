package com.androidbasico.myidealweight.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidbasico.myidealweight.R;
import com.androidbasico.myidealweight.data.DatabaseHandler;
import com.androidbasico.myidealweight.models.RecordEntry;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        DatabaseHandler dbHandler = new DatabaseHandler(this);
        List<RecordEntry> entries = dbHandler.getAllEntries();

        listView = (ListView) findViewById(R.id.list_view_history);
        ArrayAdapter<RecordEntry> adapter = new ArrayAdapter<>(this, R.layout.activity_history, entries);
        listView.setAdapter(adapter);
    }
}
