package com.main.personalfinances.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.main.personalfinances.data.Savings;
import com.main.personalfinances.data.User;
import com.main.personalfinances.data.UserAndSavings;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Update
    void update(User user);

    @Update
    void updateCurrentAmount(Savings savings);

    @Update
    void updateSavingsGoal(Savings savings);

    @Query("SELECT s.currentAmount FROM users u INNER JOIN savings s ON u.id = s.userId WHERE u.id = :userId")
    LiveData<Double> getCurrentSavingsAmount(int userId);

    @Query("SELECT s.targetAmount FROM users u INNER JOIN savings s ON u.id = s.userId WHERE u.id = :userId")
    LiveData<Double> getTargetAmount(int userId);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUser(int userId);

    @Insert
    void insertSavings(Savings savings);


    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<UserAndSavings> getUserAndSavings(int userId);

}
