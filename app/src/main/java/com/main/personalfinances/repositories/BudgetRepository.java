package com.main.personalfinances.repositories;


import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.data.Budget;

public class BudgetRepository {

    private final BudgetDao budgetDao;

    public BudgetRepository(BudgetDao budgetDao) {
        this.budgetDao = budgetDao;
    }

    public long insertBudget(Budget budget) {
        return budgetDao.insertBudget(budget);
    }

    public void updateBudget(Budget budget) {
        budgetDao.updateBudget(budget);
    }

    public Budget getBudget() { return budgetDao.getBudget(); }
}
