package com.potional.myapplication.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.potional.myapplication.R;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Progression;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightChartActivity extends AppCompatActivity {
    private BarChart barChart;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_chart);

        barChart = findViewById(R.id.weight_chart);
        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        loadChartData();
    }

    private void loadChartData() {
        executor.execute(() -> {
            String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
            List<Progression> progressionList = database.progressionDao().getProgressionForMonth(currentMonth);

            runOnUiThread(() -> {
                if (progressionList != null && !progressionList.isEmpty()) {
                    setupChart(progressionList);
                }
            });
        });
    }

    private void setupChart(List<Progression> progressionList) {
        List<BarEntry> weightEntries = new ArrayList<>();
        List<BarEntry> fatEntries = new ArrayList<>();
        List<BarEntry> muscleEntries = new ArrayList<>();
        final List<String> xLabels = new ArrayList<>();

        for (int i = 0; i < progressionList.size(); i++) {
            Progression progression = progressionList.get(i);
            // The x-value is the position, the y-value is the data
            weightEntries.add(new BarEntry(i, (float) progression.getWeight()));
            fatEntries.add(new BarEntry(i, (float) progression.getFatPercent()));
            muscleEntries.add(new BarEntry(i, (float) progression.getMusclePercent()));
            xLabels.add(progression.getDate().substring(8)); // Extract day from date string
        }

        BarDataSet weightDataSet = new BarDataSet(weightEntries, "Weight");
        BarDataSet fatDataSet = new BarDataSet(fatEntries, "Fat %");
        BarDataSet muscleDataSet = new BarDataSet(muscleEntries, "Muscle %");

        // Set colors for the datasets
        weightDataSet.setColor(Color.BLUE);
        fatDataSet.setColor(Color.RED);
        muscleDataSet.setColor(Color.GREEN);

        float groupSpace = 0.1f;
        float barSpace = 0.05f;
        float barWidth = 0.25f;

        BarData barData = new BarData(weightDataSet, fatDataSet, muscleDataSet);
        barData.setBarWidth(barWidth);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(xLabels.size());
        barChart.invalidate(); // Refresh chart
    }
}
