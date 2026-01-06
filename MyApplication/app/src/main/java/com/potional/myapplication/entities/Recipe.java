package com.potional.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "recipes")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "total_calories")
    private double totalCalories;

    @ColumnInfo(name = "steps")
    private String steps;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

}