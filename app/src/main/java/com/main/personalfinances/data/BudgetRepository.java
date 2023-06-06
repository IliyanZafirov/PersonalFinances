package com.main.personalfinances.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.db.PersonalFinancesDatabase;

public class BudgetRepository {
    private BudgetDao mBudgetDao;

    private UserDao mUserDao;
    private LiveData<Double> mGetAmount;

    BudgetRepository(Application application) {
        PersonalFinancesDatabase db = PersonalFinancesDatabase.getDatabase(application);
        mBudgetDao = db.budgetDao();
        mUserDao = db.userDao();
        mGetAmount = mBudgetDao.getAmount(mUserDao.getUserId().getValue());
    }

    LiveData<Double> getAmount() {
        return mGetAmount;
    }

    void insert(Budget budget) {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mBudgetDao.insert(budget);
        });
    }
}
