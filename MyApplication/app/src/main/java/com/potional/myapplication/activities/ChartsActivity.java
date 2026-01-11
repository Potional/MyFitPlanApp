package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ChartAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChartsActivity extends AppCompatActivity {
    private RecyclerView chartsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        chartsRecyclerView = findViewById(R.id.charts_recycler_view);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        chartsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> chartList = new ArrayList<>();
        chartList.add(getString(R.string.weight_progression));
        chartList.add(getString(R.string.calorie_intake_vs_burned));

        ChartAdapter adapter = new ChartAdapter(chartList, chartName -> {
            if (chartName.equals(getString(R.string.weight_progression))) {
                startActivity(new Intent(this, WeightChartActivity.class));
            } else if (chartName.equals(getString(R.string.calorie_intake_vs_burned))) {
                startActivity(new Intent(this, CalorieChartActivity.class));
            }
        });

        chartsRecyclerView.setAdapter(adapter);
    }
}
