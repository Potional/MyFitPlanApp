package com.potional.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.RecipeAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipesActivity extends AppCompatActivity {
    private RecyclerView recipesRecyclerView;
    private RecipeAdapter adapter;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void initViews() {
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);
        Button addRecipeButton = findViewById(R.id.add_recipe_button);
        addRecipeButton.setOnClickListener(v -> openAddRecipeScreen());
    }

    private void loadRecipes() {
        executor.execute(() -> {
            List<Recipe> allRecipes = database.recipeDao().getAllRecipes();
            runOnUiThread(() -> adapter.updateRecipes(allRecipes));
        });
    }

    private void setupRecyclerView() {
        adapter = new RecipeAdapter(new ArrayList<>(), this::onRecipeClick);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecyclerView.setAdapter(adapter);
    }

    private void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe_id", recipe.getId());
        startActivity(intent);
    }

    private void openAddRecipeScreen() {
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }
}