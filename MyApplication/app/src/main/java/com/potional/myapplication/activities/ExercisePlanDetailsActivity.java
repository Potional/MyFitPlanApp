package com.potional.myapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ExerciseAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.ExercisePlanWithExercises;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExercisePlanDetailsActivity extends AppCompatActivity {
    private TextView nameTextView;
    private RecyclerView exercisesRecyclerView;
    private ExerciseAdapter exerciseAdapter;
    private AppDatabaseDao database;
    private ExecutorService executor;
    private int planId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_plan_details);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        planId = getIntent().getIntExtra("plan_id", -1);

        initViews();
        setupRecyclerView();
        loadPlanDetails();
    }

    private void initViews() {
        nameTextView = findViewById(R.id.plan_name_text_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(new ArrayList<>());
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisesRecyclerView.setAdapter(exerciseAdapter);
    }

    private void loadPlanDetails() {
        executor.execute(() -> {
            ExercisePlanWithExercises planWithExercises = database.exercisePlanDao().getExercisePlanWithExercises(planId);

            runOnUiThread(() -> {
                if (planWithExercises != null) {
                    nameTextView.setText(planWithExercises.exercisePlan.getName());

                    if (planWithExercises.exercises != null) {
                        exerciseAdapter.updateExercises(planWithExercises.exercises);
                    }
                }
            });
        });
    }
}
