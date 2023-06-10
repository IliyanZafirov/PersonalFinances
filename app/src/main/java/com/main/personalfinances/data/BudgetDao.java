package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Budget budget);

    @Query("UPDATE budget_table SET amount = :newAmount WHERE id = :budgetId")
    void updateAmount(double newAmount, int budgetId);

    @Delete
    void delete(Budget budget);

    @Query("SELECT budget_table.amount FROM budget_table INNER JOIN user_table ON user_table.budgetId = budget_table.id WHERE user_table.id = :userId")
    LiveData<Double> getAmountById(int userId);
}
