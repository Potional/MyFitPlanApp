package com.potional.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "day_plans")
public class DayPlan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private String date; // yyyy-MM-dd format

    @ColumnInfo(name = "calories_consumed")
    private double caloriesConsumed = 0;

    @ColumnInfo(name = "calories_burned")
    private double caloriesBurned = 0;

    @ColumnInfo(name = "is_completed")
    private boolean isCompleted = false;

}