package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.data.Budget;

@Dao
public interface BudgetDao {

    @Insert
    long insertBudget(Budget budget);

    @Update
    void updateBudget(Budget budget);

    @Query("SELECT * FROM budget LIMIT 1")
    Budget getBudget();
}
