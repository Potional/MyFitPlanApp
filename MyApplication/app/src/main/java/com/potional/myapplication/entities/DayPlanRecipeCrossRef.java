package com.potional.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"date", "recipeId"},
        indices = {@Index(value = {"recipeId"})})
public class DayPlanRecipeCrossRef {
    @NonNull
    public String date;
    public int recipeId;
}