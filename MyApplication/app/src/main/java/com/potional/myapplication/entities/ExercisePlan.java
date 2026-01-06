package com.potional.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "exercise_plans")
public class ExercisePlan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "estimated_time_minutes")
    private int estimatedTimeMinutes;

    @ColumnInfo(name = "rounds")
    private int rounds;

}