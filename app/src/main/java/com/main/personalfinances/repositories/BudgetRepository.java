package com.main.personalfinances.repositories;


import com.main.personalfinances.dao.BudgetDao;
import com.main.personalfinances.model.Budget;

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

    public Budget getBudget() {
        return budgetDao.getBudget();
    }
}
