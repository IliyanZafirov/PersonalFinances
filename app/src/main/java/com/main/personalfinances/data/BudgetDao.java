package com.main.personalfinances.data;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface BudgetDao {

    @Insert
    void insertBudget(Budget budget);


}
