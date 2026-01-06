package com.potional.myapplication.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DayPlanWithRecipes {
    @Embedded
    public DayPlan dayPlan;

    @Relation(
            parentColumn = "date",
            entityColumn = "id",
            associateBy = @Junction(
                    value = DayPlanRecipeCrossRef.class,
                    parentColumn = "date",
                    entityColumn = "recipeId"
            )
    )
    public List<Recipe> recipes;
}