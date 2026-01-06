package com.potional.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ExercisePlanAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.DayPlan;
import com.potional.myapplication.entities.DayPlanExerciseCrossRef;
import com.potional.myapplication.entities.ExercisePlan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddExerciseToDayActivity extends AppCompatActivity {
    private RecyclerView exercisesRecyclerView;
    private ExercisePlanAdapter adapter;
    private AppDatabaseDao database;
    private String selectedDate;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_to_day);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        selectedDate = getIntent().getStringExtra("date");

        initViews();
        setupRecyclerView();
        loadExercisePlans();
    }

    private void initViews() {
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadExercisePlans() {
        executor.execute(() -> {
            List<ExercisePlan> allExercisePlans = database.exercisePlanDao().getAllExercisePlans();
            runOnUiThread(() -> adapter.updateExercisePlans(allExercisePlans));
        });
    }

    private void setupRecyclerView() {
        adapter = new ExercisePlanAdapter(new ArrayList<>(), this::onExercisePlanSelected);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesRecyclerView.setAdapter(adapter);
    }

    private void onExercisePlanSelected(ExercisePlan plan) {
        addExercisePlanToDay(plan);
    }

    private void addExercisePlanToDay(ExercisePlan plan) {
        executor.execute(() -> {
            DayPlan dayPlan = database.dayPlanDao().getDayPlanByDate(selectedDate);
            if (dayPlan == null) {
                dayPlan = new DayPlan();
                dayPlan.setDate(selectedDate);
                database.dayPlanDao().insert(dayPlan);
            }

            DayPlanExerciseCrossRef crossRef = new DayPlanExerciseCrossRef();
            crossRef.date = selectedDate;
            crossRef.exercisePlanId = plan.getId();
            database.dayPlanDao().insertDayPlanExerciseCrossRef(crossRef);

            dayPlan.setCaloriesBurned(dayPlan.getCaloriesBurned() + calculateCaloriesBurned(plan));
            database.dayPlanDao().update(dayPlan);

            runOnUiThread(() -> {
                Toast.makeText(this, "Added exercise plan: " + plan.getName(), Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }

    private double calculateCaloriesBurned(ExercisePlan plan) {
        return plan.getEstimatedTimeMinutes() * 5.0;
    }
}