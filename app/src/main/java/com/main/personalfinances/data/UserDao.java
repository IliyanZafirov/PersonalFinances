package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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


    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<UserAndSavings> getUserAndSavings(int userId);

}
