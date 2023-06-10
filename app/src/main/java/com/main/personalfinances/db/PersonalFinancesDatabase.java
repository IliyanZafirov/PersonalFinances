package com.main.personalfinances.db;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.main.personalfinances.converter.DateConverter;
import com.main.personalfinances.data.Bill;
import com.main.personalfinances.data.Budget;
import com.main.personalfinances.data.BudgetDao;
import com.main.personalfinances.data.Expenses;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.data.SavingsDao;
import com.main.personalfinances.data.Transaction;
import com.main.personalfinances.data.User;
import com.main.personalfinances.data.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Budget.class, Savings.class, Transaction.class, Bill.class, Expenses.class}, version = 4, exportSchema = true)
@TypeConverters(DateConverter.class)
public abstract class PersonalFinancesDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract BudgetDao budgetDao();

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
                            PersonalFinancesDatabase.class, "personalFinances_database")
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
