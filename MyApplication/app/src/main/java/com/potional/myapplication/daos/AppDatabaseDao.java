package com.potional.myapplication.daos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.potional.myapplication.entities.DayPlan;
import com.potional.myapplication.entities.DayPlanExerciseCrossRef;
import com.potional.myapplication.entities.DayPlanRecipeCrossRef;
import com.potional.myapplication.entities.Exercise;
import com.potional.myapplication.entities.ExercisePlan;
import com.potional.myapplication.entities.ExercisePlanExerciseCrossRef;
import com.potional.myapplication.entities.Ingredient;
import com.potional.myapplication.entities.Progression;
import com.potional.myapplication.entities.Recipe;
import com.potional.myapplication.entities.RecipeIngredient;

@Database(entities = {Ingredient.class, Recipe.class, RecipeIngredient.class,
        ExercisePlan.class, Exercise.class, DayPlan.class, DayPlanRecipeCrossRef.class,
        DayPlanExerciseCrossRef.class, ExercisePlanExerciseCrossRef.class, Progression.class},
        version = 7)
public abstract class AppDatabaseDao extends RoomDatabase {
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract ExercisePlanDao exercisePlanDao();
    public abstract ExerciseDao exerciseDao();
    public abstract DayPlanDao dayPlanDao();
    public abstract ProgressionDao progressionDao();

    private static volatile AppDatabaseDao INSTANCE;

    public static AppDatabaseDao getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabaseDao.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabaseDao.class, "fitness_app_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}