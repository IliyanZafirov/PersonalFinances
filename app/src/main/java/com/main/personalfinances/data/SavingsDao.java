package com.main.personalfinances.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface SavingsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Savings savings);


}
