package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("SELECT id FROM user_table")
    LiveData<Integer> getUserId();

    @Query("SELECT * FROM user_table")
    LiveData<User> getUser();
}
