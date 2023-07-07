package com.main.personalfinances.daos;

import androidx.room.Dao;
import androidx.room.Insert;

import com.main.personalfinances.data.Budget;

@Dao
public interface BudgetDao {

    @Insert
    void insertBudget(Budget budget);


}
