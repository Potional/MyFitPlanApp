package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.potional.myapplication.entities.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercises();

    @Insert
    void insertAll(Exercise... exercises);
}