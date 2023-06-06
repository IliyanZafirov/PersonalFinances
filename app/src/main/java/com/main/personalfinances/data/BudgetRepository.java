package com.main.personalfinances.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.db.PersonalFinancesDatabase;

public class BudgetRepository {
    private BudgetDao mBudgetDao;
    private LiveData<Double> mGetAmount;

    BudgetRepository(Application application, int userId) {
        PersonalFinancesDatabase db = PersonalFinancesDatabase.getDatabase(application);
        mBudgetDao = db.budgetDao();
        mGetAmount = mBudgetDao.getAmount(userId);
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