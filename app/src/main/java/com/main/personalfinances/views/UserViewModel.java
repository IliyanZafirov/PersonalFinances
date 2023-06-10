package com.main.personalfinances.views;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.main.personalfinances.data.User;
import com.main.personalfinances.data.UserDao;
import com.main.personalfinances.data.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    private final LiveData<User> mUser;

    public UserViewModel (Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mUser = mRepository.getUser();
    }

    public LiveData<User> getUser() { return mUser; }

    public void init(UserDao userDao) {
        mRepository.init(userDao);
    }

    public void insert(User user) { mRepository.insert(user); }

    public void deleteAll() {mRepository.deleteAll(); }

}
