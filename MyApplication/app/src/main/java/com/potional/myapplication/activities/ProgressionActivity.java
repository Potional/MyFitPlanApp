package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import com.potional.myapplication.R;

import java.util.Calendar;

public class ProgressionActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progression);

        calendarView = findViewById(R.id.progression_calendar_view);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(year, month, dayOfMonth);
            openProgressionDetails(selectedCalendar);
        });
    }

    private void openProgressionDetails(Calendar selectedCalendar) {
        Intent intent = new Intent(this, ProgressionDetailsActivity.class);
        intent.putExtra("date", selectedCalendar.getTimeInMillis());
        intent.putExtra("is_today", isToday(selectedCalendar));
        startActivity(intent);
    }

    private boolean isToday(Calendar selectedCalendar) {
        Calendar today = Calendar.getInstance();
        return today.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == selectedCalendar.get(Calendar.DAY_OF_MONTH);
    }
}
