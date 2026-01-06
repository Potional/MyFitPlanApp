package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.potional.myapplication.entities.Recipe;
import com.potional.myapplication.entities.RecipeIngredient;
import com.potional.myapplication.entities.RecipeWithIngredients;

import java.util.List;

@Dao
public interface RecipeDao {
    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipes();

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    RecipeWithIngredients getRecipeWithIngredients(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Transaction
    default void insertRecipeWithIngredients(Recipe recipe, List<RecipeIngredient> ingredients, RecipeIngredientDao recipeIngredientDao) {
        long recipeId = insert(recipe);
        for (RecipeIngredient ingredient : ingredients) {
            ingredient.setRecipeId((int) recipeId);
            recipeIngredientDao.insert(ingredient);
        }
    }
}