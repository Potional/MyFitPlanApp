package com.potional.myapplication.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.potional.myapplication.entities.Progression;

import java.util.List;

@Dao
public interface ProgressionDao {
    @Query("SELECT * FROM progression")
    List<Progression> getAllProgression();

    @Query("SELECT * FROM progression WHERE date = :date")
    Progression getProgressionByDate(String date);

    @Query("SELECT * FROM progression WHERE date LIKE :yearMonth || '%'")
    List<Progression> getProgressionForMonth(String yearMonth);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Progression progression);

    @Update
    void update(Progression progression);
}