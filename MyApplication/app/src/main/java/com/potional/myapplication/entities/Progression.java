package com.potional.myapplication.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "progression")
public class Progression {
    @PrimaryKey
    @NonNull
    private String date;

    private double weight;
    private double musclePercent;
    private double fatPercent;
    private String photoUri;
}