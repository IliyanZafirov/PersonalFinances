package com.main.personalfinances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

// ... (import statements)

public class ExpensesActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private ExpenseDao expenseDao;
    private ExpensesRepository expensesRepository;
    private BudgetDao budgetDao;

    private BudgetRepository budgetRepository;
    private ExecutorService databaseWriteExecutor;

    private EditText amountEditText;
    private EditText categoryEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        Toolbar myToolbar = findViewById(R.id.transactions_toolbar);
        setSupportActionBar(myToolbar);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            expenseDao = appDatabase.expenseDao();
            expensesRepository = new ExpensesRepository(expenseDao);
            budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(new Expense(1, TransactionCategory.BILLS, "Electricity",
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), 20));
        expenseList.get(0).scheduleNotification(this);
        ExpenseAdapter adapter = new ExpenseAdapter(expenseList);
        adapter.setExpenseList(expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}