package com.main.personalfinances.activities;

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
import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.data.Budget;
import com.main.personalfinances.data.BudgetRepository;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;

    private BudgetDao budgetDao;

    private BudgetRepository budgetRepository;

    private ExecutorService databaseWriteExecutor;

    private TextView monthlyBudget;
    private TextView currentBudget;
    private Button updateBudgetButton;
    private AlertDialog updateBudgetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch(Exception e) {
            e.printStackTrace();
        }

        monthlyBudget = findViewById(R.id.monthly_budget);
        currentBudget = findViewById(R.id.current_budget);
        updateBudgetButton = findViewById(R.id.update_budget_button);

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Budget budget = budgetRepository.getBudget();
                if(budget != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            monthlyBudget.setText("Monthly budget: " + String.valueOf(budget.getStartingAmount()));
                            currentBudget.setText("Current amount: " + String.valueOf(budget.getCurrentAmount()));
                        }
                    });
                }
            }
        });

        updateBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBudget(view);
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.budget_toolbar);
        setSupportActionBar(myToolbar);


    }

    private void updateBudget(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update budget:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newBudget = input.getText().toString();
                if(isValidNumber(newBudget)) {
                    databaseWriteExecutor.execute(()-> {
                        Budget budget = budgetRepository.getBudget();
                        budget.setStartingAmount(Double.valueOf(newBudget));
                        budget.setCurrentAmount(Double.valueOf(newBudget));
                        budgetRepository.updateBudget(budget);
                        monthlyBudget.setText("Monthly Budget: " + String.valueOf(budget.getStartingAmount()));
                        currentBudget.setText("Current Amount: " + String.valueOf(budget.getCurrentAmount()));
                    });
                } else {
                    Toast.makeText(BudgetActivity.this, "You didn't enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        updateBudgetDialog = builder.create();
        updateBudgetDialog.show();
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean isValidNumber(String numberString) {
        try {
            Double number = Double.valueOf(numberString);
            if(number > 0) {
                return true;
            } else {
                return  false;
            }
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}