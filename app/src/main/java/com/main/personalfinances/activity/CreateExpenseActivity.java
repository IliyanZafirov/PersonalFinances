package com.main.personalfinances.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.main.personalfinances.R;
import com.main.personalfinances.dao.BudgetDao;
import com.main.personalfinances.dao.ExpenseDao;
import com.main.personalfinances.model.Budget;
import com.main.personalfinances.repositories.BudgetRepository;
import com.main.personalfinances.model.Expense;
import com.main.personalfinances.repositories.ExpensesRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.model.TransactionCategory;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateExpenseActivity extends AppCompatActivity {

    private static final String INVALID_EXPENSE_STRING = "Invalid expense";
    private static final String PRICE_BIGGER_THAN_CURRENT_BUDGET_AMOUNT_STRING =
            "Price is bigger than current budget amount";
    private ExpensesRepository expensesRepository;
    private BudgetRepository budgetRepository;
    private ExecutorService databaseWriteExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form);

        Toolbar myToolbar = findViewById(R.id.expense_form_toolbar);
        setSupportActionBar(myToolbar);

        try {
            PersonalFinancesDatabase appDatabase = PersonalFinancesDatabase.getDatabase(this);
            ExpenseDao expenseDao = appDatabase.expenseDao();
            expensesRepository = new ExpensesRepository(expenseDao);
            BudgetDao budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        Spinner categorySpinner = findViewById(R.id.spinnerCategory);
        TransactionCategory[] categories = TransactionCategory.values();
        ArrayAdapter<TransactionCategory> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categoryAdapter);

        Button saveExpenseButton = findViewById(R.id.btnSaveExpense);
        saveExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExpense();
            }
        });
    }

    private void createExpense() {
        Spinner categorySpinner = findViewById(R.id.spinnerCategory);
        TransactionCategory category = (TransactionCategory) categorySpinner.getSelectedItem();
        EditText editPrice = findViewById(R.id.editPrice);
        EditText editDescription = findViewById(R.id.editDescription);

        final String description;
        description = editDescription.getText().toString();

        double price;
        try {
            price = Double.parseDouble(editPrice.getText().toString());
            databaseWriteExecutor.execute(() -> {
                Budget budget = budgetRepository.getBudget();
                if (budget.getCurrentAmount() >= price) {
                    budget.pay(price);
                    budgetRepository.updateBudget(budget);

                    Expense expense = new Expense(1, category, description,
                            LocalDate.now(), price);
                    expensesRepository.insertExpense(expense);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, ExpensesActivity.class);
                        startActivity(intent);
                        finish();

                    });
                } else {
                    runOnUiThread(() -> showToast(PRICE_BIGGER_THAN_CURRENT_BUDGET_AMOUNT_STRING));
                }
            });
        } catch (NumberFormatException e) {
            runOnUiThread(() -> showToast(INVALID_EXPENSE_STRING));
        }
    }

    private void showToast(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToExpenses(View view) {
        Intent intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
