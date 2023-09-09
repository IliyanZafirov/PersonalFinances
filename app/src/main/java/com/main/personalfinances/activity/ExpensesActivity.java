package com.main.personalfinances.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.personalfinances.R;
import com.main.personalfinances.adapter.ExpenseAdapter;
import com.main.personalfinances.dao.ExpenseDao;
import com.main.personalfinances.model.Expense;
import com.main.personalfinances.repositories.ExpensesRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;


import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class ExpensesActivity extends AppCompatActivity {

    private ExpensesRepository expensesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Toolbar myToolbar = findViewById(R.id.transactions_toolbar);
        setSupportActionBar(myToolbar);
        TextView amountSpendLast30DaysTextView = findViewById(R.id.spent_last_30_days);

        try {
            PersonalFinancesDatabase appDatabase = PersonalFinancesDatabase.getDatabase(this);
            ExpenseDao expenseDao = appDatabase.expenseDao();
            expensesRepository = new ExpensesRepository(expenseDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.expenses_recyclerView);
        LiveData<List<Expense>> liveDataExpenseList = expensesRepository.getAllExpenses();
        ExpenseAdapter adapter = new ExpenseAdapter(liveDataExpenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        liveDataExpenseList.observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenseList) {
                double totalAmountSpent = calculateTotalAmountSpentInLast30Days(expenseList);
                amountSpendLast30DaysTextView.setText(
                        String.format(Locale.US, "Spent last 30 days: %.2f", totalAmountSpent));
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
        finish();
    }

    public void goToForm(View view) {
        Intent intent = new Intent(this, CreateExpenseActivity.class);
        startActivity(intent);
    }
}