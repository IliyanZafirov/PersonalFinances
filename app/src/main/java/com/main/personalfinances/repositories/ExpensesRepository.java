package com.main.personalfinances.repositories;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.dao.ExpenseDao;
import com.main.personalfinances.model.Expense;

import java.util.List;

public class ExpensesRepository {

    ExpenseDao expenseDao;

    public ExpensesRepository(ExpenseDao expenseDao) {
        this.expenseDao = expenseDao;
    }

    public void insertExpense(Expense expense) {
        expenseDao.insertExpense(expense);
    }

    public void deleteAllExpenses() {
        expenseDao.deleteAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }
}
