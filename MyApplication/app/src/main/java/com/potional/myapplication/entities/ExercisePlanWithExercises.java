package com.potional.myapplication.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class ExercisePlanWithExercises {
    @Embedded
    public ExercisePlan exercisePlan;

    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = ExercisePlanExerciseCrossRef.class,
                    parentColumn = "exercisePlanId",
                    entityColumn = "exerciseId"
            )
    )
    public List<Exercise> exercises;
}