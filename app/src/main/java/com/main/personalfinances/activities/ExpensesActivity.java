package com.main.personalfinances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.personalfinances.R;
import com.main.personalfinances.adapter.ExpenseAdapter;
import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.daos.ExpenseDao;
import com.main.personalfinances.data.BudgetRepository;
import com.main.personalfinances.data.Expense;
import com.main.personalfinances.data.ExpensesRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.enums.TransactionCategory;


import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpensesActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private ExpenseDao expenseDao;
    private ExpensesRepository expensesRepository;
    private BudgetDao budgetDao;
    private BudgetRepository budgetRepository;

    private ExecutorService databaseWriteExecutor;

    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Toolbar myToolbar = findViewById(R.id.transactions_toolbar);
        setSupportActionBar(myToolbar);
        TextView amountSpendLast30DaysTextView = findViewById(R.id.spent_last_30_days);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            expenseDao = appDatabase.expenseDao();
            expensesRepository = new ExpensesRepository(expenseDao);
            budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LiveData<List<Expense>> liveDataExpenseList = expensesRepository.getAllExpenses();
        adapter = new ExpenseAdapter(liveDataExpenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        liveDataExpenseList.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenseList) {
                double totalAmountSpent = calculateTotalAmountSpentInLast30Days(expenseList);
                amountSpendLast30DaysTextView.setText(
                        String.format("Spent last 30 days: %.2f", totalAmountSpent));
            }
        });
    }



    private double calculateTotalAmountSpentInLast30Days(List<Expense> expenses) {
        double total = 0;
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        for (Expense expense: expenses) {
            if (expense.getDateAdded().isAfter(thirtyDaysAgo)) {
                total += expense.getPrice();
            }
        }
        return total;
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToForm(View view) {
        Intent intent = new Intent(this, CreateExpenseActivity.class);
        startActivity(intent);
    }
}