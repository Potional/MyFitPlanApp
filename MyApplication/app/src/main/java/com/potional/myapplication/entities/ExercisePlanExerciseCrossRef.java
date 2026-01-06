package com.potional.myapplication.entities;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(primaryKeys = {"exercisePlanId", "exerciseId"},
        indices = {@Index(value = {"exerciseId"})})
public class ExercisePlanExerciseCrossRef {
    public int exercisePlanId;
    public int exerciseId;
}