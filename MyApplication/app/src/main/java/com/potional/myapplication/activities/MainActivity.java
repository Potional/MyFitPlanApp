package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.potional.myapplication.R;

public class MainActivity extends AppCompatActivity {
    private Button recipesButton, exercisesButton, calendarButton, timerButton, progressionButton, chartsButton, settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesButton = findViewById(R.id.recipes_button);
        exercisesButton = findViewById(R.id.exercises_button);
        calendarButton = findViewById(R.id.calendar_button);
        timerButton = findViewById(R.id.timer_button);
        progressionButton = findViewById(R.id.progression_button);
        chartsButton = findViewById(R.id.charts_button);
        settingsButton = findViewById(R.id.settings_button);

        recipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecipesActivity.class);
            startActivity(intent);
        });

        exercisesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExercisesPlansActivity.class);
            startActivity(intent);
        });

        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        timerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimerActivity.class);
            startActivity(intent);
        });

        progressionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProgressionActivity.class);
            startActivity(intent);
        });

        chartsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChartsActivity.class);
            startActivity(intent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}
