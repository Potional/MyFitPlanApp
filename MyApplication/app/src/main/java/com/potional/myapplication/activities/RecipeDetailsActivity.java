package com.potional.myapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.IngredientAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.RecipeWithIngredients;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeDetailsActivity extends AppCompatActivity {
    private TextView nameTextView;
    private TextView caloriesTextView;
    private TextView stepsTextView;
    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;
    private AppDatabaseDao database;
    private ExecutorService executor;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        initViews();
        setupRecyclerView();
        loadRecipeDetails();
    }

    private void initViews() {
        nameTextView = findViewById(R.id.recipe_name_text_view);
        caloriesTextView = findViewById(R.id.recipe_calories_text_view);
        stepsTextView = findViewById(R.id.recipe_steps_text_view);
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view);
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void loadRecipeDetails() {
        executor.execute(() -> {
            RecipeWithIngredients recipeWithIngredients = database.recipeDao().getRecipeWithIngredients(recipeId);

            runOnUiThread(() -> {
                if (recipeWithIngredients != null) {
                    nameTextView.setText(recipeWithIngredients.recipe.getName());
                    caloriesTextView.setText(String.format(Locale.getDefault(), "%.2f cal", recipeWithIngredients.recipe.getTotalCalories()));
                    stepsTextView.setText(recipeWithIngredients.recipe.getSteps());

                    if (recipeWithIngredients.ingredients != null) {
                        ingredientAdapter.updateIngredients(recipeWithIngredients.ingredients);
                    }
                }
            });
        });
    }
}
