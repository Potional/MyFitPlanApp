package com.potional.myapplication.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.potional.myapplication.R;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.DayPlan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalorieChartActivity extends AppCompatActivity {
    private LineChart lineChart;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_chart);

        lineChart = findViewById(R.id.calorie_chart);
        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        loadChartData();
    }

    private void loadChartData() {
        executor.execute(() -> {
            String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
            List<DayPlan> dayPlans = database.dayPlanDao().getDayPlansForMonth(currentMonth);

            runOnUiThread(() -> {
                if (dayPlans != null && !dayPlans.isEmpty()) {
                    setupChart(dayPlans);
                }
            });
        });
    }

    private void setupChart(List<DayPlan> dayPlans) {
        List<Entry> consumedEntries = new ArrayList<>();
        List<Entry> burnedEntries = new ArrayList<>();
        final List<String> xLabels = new ArrayList<>();

        for (int i = 0; i < dayPlans.size(); i++) {
            DayPlan dayPlan = dayPlans.get(i);
            consumedEntries.add(new Entry(i, (float) dayPlan.getCaloriesConsumed()));
            burnedEntries.add(new Entry(i, (float) dayPlan.getCaloriesBurned()));
            xLabels.add(dayPlan.getDate().substring(8)); // Extract day from date
        }

        LineDataSet consumedDataSet = new LineDataSet(consumedEntries, getString(R.string.calories_consumed_text));
        LineDataSet burnedDataSet = new LineDataSet(burnedEntries, getString(R.string.calories_burned_text));

        // Customize colors
        consumedDataSet.setColor(ContextCompat.getColor(this, R.color.colorConsumed));
        burnedDataSet.setColor(ContextCompat.getColor(this, R.color.colorBurned));

        LineData lineData = new LineData(consumedDataSet, burnedDataSet);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }
}
