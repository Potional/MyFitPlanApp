package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.potional.myapplication.entities.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeIngredientDao {
    @Query("SELECT * FROM recipe_ingredients WHERE recipe_id = :recipeId")
    List<RecipeIngredient> getIngredientsForRecipe(int recipeId);

    @Insert
    void insertAll(List<RecipeIngredient> recipeIngredients);

    @Insert
    void insert(RecipeIngredient recipeIngredient);

    @Delete
    void delete(RecipeIngredient recipeIngredient);
}