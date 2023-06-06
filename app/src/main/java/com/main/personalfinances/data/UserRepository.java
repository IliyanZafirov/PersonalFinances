package com.main.personalfinances.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.db.PersonalFinancesDatabase;

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<User> mUser;

    UserRepository(Application application) {
        PersonalFinancesDatabase db = PersonalFinancesDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mUser = mUserDao.getUser();
    }

    LiveData<User> getUser() {
        return mUser;
    }

    void insert(User user) {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }
}
