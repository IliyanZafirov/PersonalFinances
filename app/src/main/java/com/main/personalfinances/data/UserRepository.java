package com.main.personalfinances.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.db.PersonalFinancesDatabase;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<User> mUser;

    public UserRepository(Application application) {
        PersonalFinancesDatabase db = PersonalFinancesDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mUser = mUserDao.getUser();
    }

    public void init(UserDao userDao) {
        this.mUserDao = userDao;
        mUser = userDao.getUser();
    }

    public LiveData<User> getUser() {
        return mUser;
    }

    public void insert(User user) {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }

    public void update(User user) {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.update(user);
        });
    }
    public void deleteAll() {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.deleteAll();
        });
    }
}
