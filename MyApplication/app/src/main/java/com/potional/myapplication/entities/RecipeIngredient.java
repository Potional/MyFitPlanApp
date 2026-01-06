package com.potional.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(
        tableName = "recipe_ingredients",
        primaryKeys = {"recipe_id", "ingredient_id"},
        indices = {@Index(value = {"ingredient_id"})}
)
public class RecipeIngredient {
    @ColumnInfo(name = "recipe_id")
    private int recipeId;

    @ColumnInfo(name = "ingredient_id")
    private int ingredientId;

    @ColumnInfo(name = "amount_in_grams")
    private double amountInGrams;

}