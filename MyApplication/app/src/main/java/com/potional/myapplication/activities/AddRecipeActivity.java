package com.potional.myapplication.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.potional.myapplication.R;
import com.potional.myapplication.adapters.IngredientAdapter;
import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Ingredient;
import com.potional.myapplication.entities.Recipe;
import com.potional.myapplication.entities.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddRecipeActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText caloriesEditText;
    private EditText stepsEditText;
    private RecyclerView ingredientsRecyclerView;
    private IngredientAdapter ingredientAdapter;
    private AppDatabaseDao database;
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        database = AppDatabaseDao.getDatabase(this);
        executor = Executors.newSingleThreadExecutor();

        initViews();
        setupRecyclerView();
        loadIngredients();
        setupSaveButton();
        setupAddIngredientButton();
    }

    private void initViews() {
        nameEditText = findViewById(R.id.recipe_name_edit_text);
        caloriesEditText = findViewById(R.id.calories_edit_text);
        stepsEditText = findViewById(R.id.steps_edit_text);
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view);
    }

    private void loadIngredients() {
        executor.execute(() -> {
            List<Ingredient> allIngredients = database.ingredientDao().getAllIngredients();
            runOnUiThread(() -> ingredientAdapter.updateIngredients(allIngredients));
        });
    }

    private void setupRecyclerView() {
        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.save_recipe_button);
        saveButton.setOnClickListener(v -> saveRecipe());
    }

    private void setupAddIngredientButton() {
        Button addIngredientButton = findViewById(R.id.add_ingredient_button);
        addIngredientButton.setOnClickListener(v -> showAddIngredientDialog());
    }

    private void showAddIngredientDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Ingredient");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_ingredient, null);
        builder.setView(view);

        EditText nameEditText = view.findViewById(R.id.ingredient_name_edit_text);
        EditText caloriesEditText = view.findViewById(R.id.ingredient_calories_edit_text);
        EditText typeEditText = view.findViewById(R.id.ingredient_type_edit_text);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameEditText.getText().toString().trim();
            String caloriesStr = caloriesEditText.getText().toString().trim();
            String type = typeEditText.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(caloriesStr) || TextUtils.isEmpty(type)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double calories = Double.parseDouble(caloriesStr);

            Ingredient newIngredient = new Ingredient();
            newIngredient.setName(name);
            newIngredient.setCaloriesPer100g(calories);
            newIngredient.setType(type);

            executor.execute(() -> {
                database.ingredientDao().insert(newIngredient);
                loadIngredients();
            });
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void saveRecipe() {
        String name = nameEditText.getText().toString().trim();
        String caloriesStr = caloriesEditText.getText().toString().trim();
        String steps = stepsEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter recipe name");
            return;
        }

        if (TextUtils.isEmpty(caloriesStr)) {
            caloriesEditText.setError("Please enter total calories");
            return;
        }

        if (TextUtils.isEmpty(steps)) {
            stepsEditText.setError("Please enter cooking steps");
            return;
        }

        double totalCalories = Double.parseDouble(caloriesStr);

        List<Ingredient> selectedIngredients = ingredientAdapter.getSelectedIngredients();
        if (selectedIngredients.isEmpty()) {
            Toast.makeText(this, "Please select at least one ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setTotalCalories(totalCalories);
        recipe.setSteps(steps);

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (Ingredient ingredient : selectedIngredients) {
            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setIngredientId(ingredient.getId());
            recipeIngredients.add(recipeIngredient);
        }

        executor.execute(() -> {
            database.recipeDao().insertRecipeWithIngredients(recipe, recipeIngredients, database.recipeIngredientDao());

            runOnUiThread(() -> {
                Toast.makeText(this, "Recipe saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}