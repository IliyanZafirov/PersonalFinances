package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public LiveData<User> getUser(int userId) {
        return userDao.getUser(userId);
    }
}
