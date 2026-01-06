package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.potional.myapplication.entities.ExercisePlan;
import com.potional.myapplication.entities.ExercisePlanExerciseCrossRef;
import com.potional.myapplication.entities.ExercisePlanWithExercises;

import java.util.List;

@Dao
public interface ExercisePlanDao {
    @Query("SELECT * FROM exercise_plans")
    List<ExercisePlan> getAllExercisePlans();

    @Transaction
    @Query("SELECT * FROM exercise_plans WHERE id = :planId")
    ExercisePlanWithExercises getExercisePlanWithExercises(int planId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ExercisePlan exercisePlan);

    @Update
    void update(ExercisePlan exercisePlan);

    @Delete
    void delete(ExercisePlan exercisePlan);

    @Transaction
    default void insertPlanWithExercises(ExercisePlan exercisePlan, List<ExercisePlanExerciseCrossRef> exercises, ExercisePlanDao exercisePlanDao) {
        long planId = insert(exercisePlan);
        for (ExercisePlanExerciseCrossRef exercise : exercises) {
            exercise.exercisePlanId = (int) planId;
            exercisePlanDao.insertExercisePlanExerciseCrossRef(exercise);
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertExercisePlanExerciseCrossRef(ExercisePlanExerciseCrossRef crossRef);
}