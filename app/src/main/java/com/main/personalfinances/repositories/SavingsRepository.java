package com.main.personalfinances.repositories;

import com.main.personalfinances.daos.SavingsDao;
import com.main.personalfinances.data.Savings;

public class SavingsRepository {
    SavingsDao savingsDao;

    public SavingsRepository(SavingsDao savingsDao) { this.savingsDao = savingsDao; }
    public long insertSavings(Savings savings) { return savingsDao.insertSavings(savings);}
    public void updateSavings(Savings savings) { savingsDao.updateSavings(savings); }
    public Savings getSavings() { return savingsDao.getSavings(); }
}
