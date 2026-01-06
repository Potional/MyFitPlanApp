package com.potional.myapplication.services;

import com.potional.myapplication.daos.AppDatabaseDao;
import com.potional.myapplication.entities.Ingredient;
import com.potional.myapplication.entities.Recipe;
import com.potional.myapplication.entities.RecipeIngredient;

import java.util.List;

public class RecipeService {
    public static double calculateRecipeCalories(Recipe recipe, List<RecipeIngredient> recipeIngredients,
                                                 AppDatabaseDao database) {
        double totalCalories = 0;

        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = database.ingredientDao().getIngredientById(ri.getIngredientId());
            if (ingredient != null) {
                double caloriesPerGram = ingredient.getCaloriesPer100g() / 100.0;
                totalCalories += caloriesPerGram * ri.getAmountInGrams();
            }
        }

        return totalCalories;
    }

    public static void updateRecipeCalories(Recipe recipe, List<RecipeIngredient> recipeIngredients,
                                            AppDatabaseDao database) {
        double calculatedCalories = calculateRecipeCalories(recipe, recipeIngredients, database);
        recipe.setTotalCalories(calculatedCalories);

        // Update in database
        database.recipeDao().update(recipe);
    }
}