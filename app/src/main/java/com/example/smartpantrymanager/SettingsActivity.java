package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch alertsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        alertsSwitch = findViewById(R.id.alertsSwitch);

        // Load saved preference
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean alertsEnabled = prefs.getBoolean("alertsEnabled", false);
        alertsSwitch.setChecked(alertsEnabled);

        // Save preference when toggled
        alertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("alertsEnabled", isChecked);
            editor.apply();
        });
    }
}
