package com.main.personalfinances.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.model.Savings;

@Dao
public interface SavingsDao {
    @Insert
    long insertSavings(Savings savings);

    @Update
    void updateSavings(Savings savings);

    @Query("SELECT * FROM savings")
    Savings getSavings();
}
