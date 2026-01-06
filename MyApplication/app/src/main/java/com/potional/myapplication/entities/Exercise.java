package com.potional.myapplication.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "exercises")
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private int sets;
    private int reps;
}