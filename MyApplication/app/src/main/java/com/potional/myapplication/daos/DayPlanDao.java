package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.potional.myapplication.entities.DayPlan;
import com.potional.myapplication.entities.DayPlanExerciseCrossRef;
import com.potional.myapplication.entities.DayPlanRecipeCrossRef;
import com.potional.myapplication.entities.DayPlanWithExercises;
import com.potional.myapplication.entities.DayPlanWithRecipes;

import java.util.List;

@Dao
public interface DayPlanDao {
    @Query("SELECT * FROM day_plans WHERE date = :date")
    DayPlan getDayPlanByDate(String date);

    @Query("SELECT * FROM day_plans WHERE date LIKE :yearMonth || '%'")
    List<DayPlan> getDayPlansForMonth(String yearMonth);

    @Transaction
    @Query("SELECT * FROM day_plans WHERE date = :date")
    DayPlanWithRecipes getDayPlanWithRecipes(String date);

    @Transaction
    @Query("SELECT * FROM day_plans WHERE date = :date")
    DayPlanWithExercises getDayPlanWithExercises(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDayPlanRecipeCrossRef(DayPlanRecipeCrossRef crossRef);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDayPlanExerciseCrossRef(DayPlanExerciseCrossRef crossRef);

    @Insert
    void insert(DayPlan dayPlan);

    @Update
    void update(DayPlan dayPlan);
}