package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;
import androidx.room.Update;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public LiveData<Double> getCurrentSavingsAmount(int userId) {
        return userDao.getCurrentSavingsAmount(userId);
    }

    public LiveData<Double> getTargetAmount(int userId) {
        return userDao.getTargetAmount(userId);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void updateCurrentAmount(Savings savings) { userDao.updateCurrentAmount(savings);}


    public void updateSavingsGoal(Savings savings) {userDao.updateSavingsGoal(savings);}

    public LiveData<User> getUser(int userId) {
        return userDao.getUser(userId);
    }

    public LiveData<UserAndSavings> getUserAndSavings(int userId) {
        return userDao.getUserAndSavings(userId);
    }
}
