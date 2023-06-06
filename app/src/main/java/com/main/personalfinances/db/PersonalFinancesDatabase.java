package com.main.personalfinances.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.main.personalfinances.data.Budget;
import com.main.personalfinances.data.BudgetDao;
import com.main.personalfinances.data.User;
import com.main.personalfinances.data.UserDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Budget.class}, version = 1, exportSchema = false)
public abstract class PersonalFinancesDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract BudgetDao budgetDao();

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
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
