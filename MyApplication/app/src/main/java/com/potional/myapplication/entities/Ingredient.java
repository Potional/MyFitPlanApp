package com.potional.myapplication.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "ingredients")
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "calories_per_100g")
    private double caloriesPer100g;

    @ColumnInfo(name = "type")
    private String type; // vegetable, fish, cereals, meat, lactose

}