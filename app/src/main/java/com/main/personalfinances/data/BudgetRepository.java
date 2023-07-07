package com.main.personalfinances.data;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.daos.BudgetDao;

public class BudgetRepository {

    private BudgetDao budgetDao;

    public BudgetRepository(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
    }

    public long insertBudget(Budget budget) {
        return budgetDao.insertBudget(budget);
    }

    public void updateBudget(Budget budget) {
        budgetDao.updateBudget(budget);
    }

    public void deleteBudget(Budget budget) {
        budgetDao.deleteBudget(budget);
    }
    public Budget getBudget() { return budgetDao.getBudget(); }
}
