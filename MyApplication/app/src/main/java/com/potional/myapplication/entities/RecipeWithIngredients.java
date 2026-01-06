package com.potional.myapplication.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithIngredients {
    @Embedded
    public Recipe recipe;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = RecipeIngredient.class,
                    parentColumn = "recipe_id",
                    entityColumn = "ingredient_id"
            )
    )
    public List<Ingredient> ingredients;
}