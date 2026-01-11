package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.ExercisePlanAdapter;
import com.potional.myapplication.adapters.RecipeAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.DayPlanWithExercises;
import com.potional.myapplication.entities.DayPlanWithRecipes;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DayDetailsActivity extends AppCompatActivity {
    private TextView dateTextView;
    private TextView caloriesTextView;
    private RecyclerView recipesRecyclerView;
    private RecyclerView exercisesRecyclerView;
    private AppDatabaseDao database;
    private String selectedDate;
    private boolean isPastDate;
    private ExecutorService executor;
    private RecipeAdapter recipeAdapter;
    private ExercisePlanAdapter exercisePlanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        selectedDate = getIntent().getStringExtra("date");
        isPastDate = getIntent().getBooleanExtra("is_past_date", false);

        initViews(selectedDate);
        setupRecyclerViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDayData();
    }

    private void initViews(String date) {
        dateTextView = findViewById(R.id.date_text_view);
        caloriesTextView = findViewById(R.id.calories_text_view);
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        exercisesRecyclerView = findViewById(R.id.exercises_recycler_view);

        dateTextView.setText(date);

        Button addRecipeButton = findViewById(R.id.add_recipe_button);
        Button addExerciseButton = findViewById(R.id.add_exercise_button);

        if (isPastDate) {
            addRecipeButton.setVisibility(View.GONE);
            addExerciseButton.setVisibility(View.GONE);
        } else {
            addRecipeButton.setOnClickListener(v -> openAddRecipeToDay());
            addExerciseButton.setOnClickListener(v -> openAddExerciseToDay());
        }
    }

    private void setupRecyclerViews() {
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(new ArrayList<>(), recipe -> {});
        recipesRecyclerView.setAdapter(recipeAdapter);

        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exercisePlanAdapter = new ExercisePlanAdapter(new ArrayList<>(), plan -> {});
        exercisesRecyclerView.setAdapter(exercisePlanAdapter);
    }

    private void loadDayData() {
        executor.execute(() -> {
            DayPlanWithRecipes dayPlanWithRecipes = database.dayPlanDao().getDayPlanWithRecipes(selectedDate);
            DayPlanWithExercises dayPlanWithExercises = database.dayPlanDao().getDayPlanWithExercises(selectedDate);

            runOnUiThread(() -> {
                double caloriesConsumed = 0;
                double caloriesBurned = 0;

                if (dayPlanWithRecipes != null && dayPlanWithRecipes.dayPlan != null) {
                    caloriesConsumed = dayPlanWithRecipes.dayPlan.getCaloriesConsumed();
                    if (dayPlanWithRecipes.recipes != null) {
                        recipeAdapter.updateRecipes(dayPlanWithRecipes.recipes);
                    }
                } else {
                    recipeAdapter.updateRecipes(new ArrayList<>());
                }

                if (dayPlanWithExercises != null && dayPlanWithExercises.dayPlan != null) {
                    caloriesBurned = dayPlanWithExercises.dayPlan.getCaloriesBurned();
                    if (dayPlanWithExercises.exercises != null) {
                        exercisePlanAdapter.updateExercisePlans(dayPlanWithExercises.exercises);
                    }
                } else {
                    exercisePlanAdapter.updateExercisePlans(new ArrayList<>());
                }

                caloriesTextView.setText(String.format(getString(R.string.consumed_burned_format), caloriesConsumed, caloriesBurned));
            });
        });
    }

    private void openAddRecipeToDay() {
        Intent intent = new Intent(this, AddRecipeToDayActivity.class);
        intent.putExtra("date", selectedDate);
        startActivity(intent);
    }

    private void openAddExerciseToDay() {
        Intent intent = new Intent(this, AddExerciseToDayActivity.class);
        intent.putExtra("date", selectedDate);
        startActivity(intent);
    }
}
