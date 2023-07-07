package com.main.personalfinances.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.data.Budget;

@Dao
public interface BudgetDao {

    @Insert
    public long insertBudget(Budget budget);

    @Update
    public void updateBudget(Budget budget);

    @Delete
    public void deleteBudget(Budget budget);

    @Query("SELECT * FROM budget LIMIT 1")
    public Budget getBudget();
}
