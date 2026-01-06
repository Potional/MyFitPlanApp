package com.potional.myapplication.entities;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DayPlanWithExercises {
    @Embedded
    public DayPlan dayPlan;

    @Relation(
            parentColumn = "date",
            entityColumn = "id",
            associateBy = @Junction(
                    value = DayPlanExerciseCrossRef.class,
                    parentColumn = "date",
                    entityColumn = "exercisePlanId"
            )
    )
    public List<ExercisePlan> exercises;
}