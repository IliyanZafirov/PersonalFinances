package com.main.personalfinances.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.main.personalfinances.R;
import com.main.personalfinances.dao.BudgetDao;
import com.main.personalfinances.dao.ExpenseDao;
import com.main.personalfinances.model.Budget;
import com.main.personalfinances.repositories.BudgetRepository;
import com.main.personalfinances.repositories.ExpensesRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetActivity extends AppCompatActivity {

    private static final String OK_STRING = "OK";
    private static final String MONTHLY_BUDGET_STRING = "Monthly budget: ";
    private static final String CURRENT_AMOUNT_STRING = "Current amount: ";
    private static final String UPDATE_BUDGET_STRING = "Update budget (IT IS GOING TO DELETE ALL EXPENSES IF PRESENT):";
    private static final String INVALID_AMOUNT_STRING = "You didn't enter a valid amount";
    private static final String CANCEL_STRING = "Cancel";
    private PersonalFinancesDatabase appDatabase;
    private BudgetRepository budgetRepository;
    private ExpensesRepository expensesRepository;
    private ExecutorService databaseWriteExecutor;
    private TextView monthlyBudget;
    private TextView currentBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
        } catch(Exception e) {
            e.printStackTrace();
        }

        BudgetDao budgetDao = appDatabase.budgetDao();
        budgetRepository = new BudgetRepository(budgetDao);
        ExpenseDao expenseDao = appDatabase.expenseDao();
        expensesRepository = new ExpensesRepository(expenseDao);
        monthlyBudget = findViewById(R.id.monthly_budget);
        currentBudget = findViewById(R.id.current_budget);
        Button updateBudgetButton = findViewById(R.id.update_budget_button);

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        databaseWriteExecutor.execute(() -> {
            Budget budget = budgetRepository.getBudget();
            if(budget != null) {
                runOnUiThread(() -> {
                    monthlyBudget.setText(MONTHLY_BUDGET_STRING + budget.getStartingAmount());
                    currentBudget.setText(CURRENT_AMOUNT_STRING + budget.getCurrentAmount());
                });
            }
        });

        updateBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBudget(view);
            }
        });

        Toolbar myToolbar = findViewById(R.id.budget_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void updateBudget(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(UPDATE_BUDGET_STRING);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(OK_STRING, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newBudget = input.getText().toString();
                if(isValidNumber(newBudget)) {
                    databaseWriteExecutor.execute(()-> {
                        Budget budget = budgetRepository.getBudget();
                        budget.setStartingAmount(Double.parseDouble(newBudget));
                        budget.setCurrentAmount(Double.parseDouble(newBudget));
                        budgetRepository.updateBudget(budget);
                        if (expensesRepository.getAllExpenses() != null) {
                            expensesRepository.deleteAllExpenses();
                        }
                        monthlyBudget.setText(MONTHLY_BUDGET_STRING + budget.getStartingAmount());
                        currentBudget.setText(CURRENT_AMOUNT_STRING + budget.getCurrentAmount());
                    });
                } else {
                    Toast.makeText(BudgetActivity.this,
                            INVALID_AMOUNT_STRING, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL_STRING, null);

        AlertDialog updateBudgetDialog = builder.create();
        updateBudgetDialog.show();
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isValidNumber(String numberString) {
        try {
            double number = Double.parseDouble(numberString);
            return number > 0;
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}