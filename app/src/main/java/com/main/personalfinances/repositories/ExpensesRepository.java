package com.main.personalfinances.repositories;



import androidx.lifecycle.LiveData;

import com.main.personalfinances.daos.ExpenseDao;
import com.main.personalfinances.data.Expense;

import java.util.List;

public class ExpensesRepository {

    ExpenseDao expenseDao;

    public ExpensesRepository(ExpenseDao expenseDao) { this.expenseDao = expenseDao; }


    public void insertExpense(Expense expense) { expenseDao.insertExpense(expense);}

    public void updateExpense(Expense expense) {expenseDao.updateExpense(expense);}

    public void deleteExpense(Expense expense) { expenseDao.deleteExpense(expense);}
    public void deleteAllExpenses() { expenseDao.deleteAllExpenses();}

    public LiveData<List<Expense>> getAllExpenses() { return expenseDao.getAllExpenses(); }
}
