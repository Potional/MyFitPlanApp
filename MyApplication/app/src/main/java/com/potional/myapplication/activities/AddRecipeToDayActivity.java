package com.potional.myapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.RecipeAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.DayPlan;
import com.potional.myapplication.entities.DayPlanRecipeCrossRef;
import com.potional.myapplication.entities.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRecipeToDayActivity extends AppCompatActivity {
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private AppDatabaseDao database;
    private String selectedDate;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_to_day);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        selectedDate = getIntent().getStringExtra("date");

        initViews();
        setupRecyclerView();
        loadRecipes();
    }

    private void initViews() {
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadRecipes() {
        executor.execute(() -> {
            List<Recipe> allRecipes = database.recipeDao().getAllRecipes();
            runOnUiThread(() -> adapter.updateRecipes(allRecipes));
        });
    }

    private void setupRecyclerView() {
        adapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeSelected);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecyclerView.setAdapter(adapter);
    }

    private void onRecipeSelected(Recipe recipe) {
        addRecipeToDay(recipe);
    }

    private void addRecipeToDay(Recipe recipe) {
        executor.execute(() -> {
            DayPlan dayPlan = database.dayPlanDao().getDayPlanByDate(selectedDate);
            if (dayPlan == null) {
                dayPlan = new DayPlan();
                dayPlan.setDate(selectedDate);
                database.dayPlanDao().insert(dayPlan);
            }

            DayPlanRecipeCrossRef crossRef = new DayPlanRecipeCrossRef();
            crossRef.date = selectedDate;
            crossRef.recipeId = recipe.getId();
            database.dayPlanDao().insertDayPlanRecipeCrossRef(crossRef);

            dayPlan.setCaloriesConsumed(dayPlan.getCaloriesConsumed() + recipe.getTotalCalories());
            database.dayPlanDao().update(dayPlan);

            runOnUiThread(() -> {
                Toast.makeText(this, "Added recipe: " + recipe.getName(), Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}