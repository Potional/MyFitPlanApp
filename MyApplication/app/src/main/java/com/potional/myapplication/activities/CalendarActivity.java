package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.appcompat.app.AppCompatActivity;

import com.potional.myapplication.R;

import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initCalendar();
        setupCalendarView();
    }

    private void initCalendar() {
        calendarView = findViewById(R.id.calendar_view);
    }

    private void setupCalendarView() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            openDayDetails(year, month, dayOfMonth);
        });
    }

    private boolean isPastDate(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return selectedDate.before(today);
    }

    private void openDayDetails(int year, int month, int day) {
        Intent intent = new Intent(this, DayDetailsActivity.class);
        intent.putExtra("date", formatDate(year, month, day));
        intent.putExtra("is_past_date", isPastDate(year, month, day));
        startActivity(intent);
    }

    private String formatDate(int year, int month, int day) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
    }
}