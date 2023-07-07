package com.main.personalfinances.db;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.main.personalfinances.converter.DateConverter;
import com.main.personalfinances.data.Budget;
import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.daos.SavingsDao;
import com.main.personalfinances.data.Expense;
import com.main.personalfinances.daos.ExpenseDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Budget.class, Expense.class, Savings.class}, version = 105, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class PersonalFinancesDatabase extends RoomDatabase {


    public abstract BudgetDao budgetDao();
    public abstract ExpenseDao expenseDao();
    public abstract SavingsDao savingsDao();

    private static volatile PersonalFinancesDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PersonalFinancesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PersonalFinancesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PersonalFinancesDatabase.class, "personalFinance_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    public void dropDatabase() {
        clearAllTables();
    }

}
