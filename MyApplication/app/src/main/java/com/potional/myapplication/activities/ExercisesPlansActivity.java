package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ExercisePlanAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.ExercisePlan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExercisesPlansActivity extends AppCompatActivity {
    private RecyclerView exercisesPlansRecyclerView;
    private ExercisePlanAdapter adapter;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_plans);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExercisePlans();
    }

    private void initViews() {
        exercisesPlansRecyclerView = findViewById(R.id.exercises_plans_recycler_view);
        Button addPlanButton = findViewById(R.id.add_plan_button);
        addPlanButton.setOnClickListener(v -> openAddExercisePlanScreen());
    }

    private void loadExercisePlans() {
        executor.execute(() -> {
            List<ExercisePlan> allExercisePlans = database.exercisePlanDao().getAllExercisePlans();
            runOnUiThread(() -> adapter.updateExercisePlans(allExercisePlans));
        });
    }

    private void setupRecyclerView() {
        adapter = new ExercisePlanAdapter(new ArrayList<>(), this::onExercisePlanClick);
        exercisesPlansRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesPlansRecyclerView.setAdapter(adapter);
    }

    private void onExercisePlanClick(ExercisePlan plan) {
        Intent intent = new Intent(this, ExercisePlanDetailsActivity.class);
        intent.putExtra("plan_id", plan.getId());
        startActivity(intent);
    }

    private void openAddExercisePlanScreen() {
        Intent intent = new Intent(this, AddExercisePlanActivity.class);
        startActivity(intent);
    }
}
