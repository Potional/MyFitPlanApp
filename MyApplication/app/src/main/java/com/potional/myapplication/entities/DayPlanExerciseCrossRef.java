package com.potional.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"date", "exercisePlanId"},
        indices = {@Index(value = {"exercisePlanId"})})
public class DayPlanExerciseCrossRef {
    @NonNull
    public String date;
    public int exercisePlanId;
}