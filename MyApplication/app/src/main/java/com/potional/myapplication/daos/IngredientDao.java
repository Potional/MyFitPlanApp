package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.potional.myapplication.entities.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {
    @Query("SELECT * FROM ingredients")
    List<Ingredient> getAllIngredients();

    @Query("SELECT * FROM ingredients WHERE id = :id")
    Ingredient getIngredientById(int id);

    @Insert
    void insertAll(List<Ingredient> ingredients);

    @Insert
    void insert(Ingredient ingredient);

    @Update
    void update(Ingredient ingredient);

    @Delete
    void delete(Ingredient ingredient);
}