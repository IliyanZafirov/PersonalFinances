package com.main.personalfinances.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.db.PersonalFinancesDatabase;

public class SavingsRepository {
    private SavingsDao mSavingsDao;
    private LiveData<Double> mGetAmount;

    SavingsRepository(Application application, int userId) {
        PersonalFinancesDatabase db = PersonalFinancesDatabase.getDatabase(application);
        mSavingsDao = db.savingsDao();
    }

    LiveData<Double> getAmountById() {
        return mGetAmount;
    }

    void insert(Savings savings) {
        PersonalFinancesDatabase.databaseWriteExecutor.execute(() -> {
            mSavingsDao.insert(savings);
        });
    }
}
