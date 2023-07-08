package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.data.Savings;

@Dao
public interface SavingsDao {

    @Insert
    public long insertSavings(Savings savings);
    @Update
    public void updateSavings(Savings savings);
    @Delete
    public void deleteSavings(Savings savings);

    @Query("SELECT * FROM savings")
    public Savings getSavings();
}
