package com.main.personalfinances.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.main.personalfinances.R;
import com.main.personalfinances.dao.BudgetDao;
import com.main.personalfinances.dao.SavingsDao;
import com.main.personalfinances.model.Budget;
import com.main.personalfinances.repositories.BudgetRepository;
import com.main.personalfinances.model.Savings;
import com.main.personalfinances.repositories.SavingsRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingsActivity extends AppCompatActivity {

    private static final String OK_STRING = "OK";
    private static final String CANCEL_STRING = "Cancel";
    private static final String INVALID_AMOUNT_ENTERED_BY_USER_STRING = "You didn't enter a valid amount";
    private static final String SAVINGS_GOAL_STRING = "Savings goal: ";
    private static final String SAVED_AMOUNT_STRING = "Saved amount: ";
    private static final String ADD_MONEY_STRING = "Add money:";
    private static final String UPDATE_SAVINGS_GOAL_STRING = "Update savings goal:";
    private static final String AMOUNT_BIGGER_THAN_CURRENT_BUDGET_STRING = "Amount bigger than current budget";
    private SavingsRepository savingsRepository;
    private BudgetRepository budgetRepository;
    private ExecutorService databaseWriteExecutor;
    private TextView savings_goal_text_view;
    private TextView saved_amount_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);
        Toolbar myToolbar = findViewById(R.id.savings_toolbar);
        setSupportActionBar(myToolbar);

        try {
            PersonalFinancesDatabase appDatabase = PersonalFinancesDatabase.getDatabase(this);
            SavingsDao savingsDao = appDatabase.savingsDao();
            savingsRepository = new SavingsRepository(savingsDao);
            BudgetDao budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        savings_goal_text_view = findViewById(R.id.savings_goal);
        saved_amount_text_view = findViewById(R.id.saved_amount);
        Button add_money_button = findViewById(R.id.add_money_button);
        ImageButton setSavingsGoalButton = findViewById(R.id.set_savings_goal_button);

        setSavingsGoalAndSavingsAmount();

        setSavingsGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewGoal();
            }
        });
        add_money_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSavings(view);
            }
        });
    }

    private void setSavingsGoalAndSavingsAmount() {
        databaseWriteExecutor.execute(() -> {
            Savings savings = savingsRepository.getSavings();
            if (savings != null) {
                runOnUiThread(() -> {
                    savings_goal_text_view.setText(SAVINGS_GOAL_STRING + savings.getTargetAmount());
                    saved_amount_text_view.setText(SAVED_AMOUNT_STRING + savings.getCurrentAmount());
                });
            }
        });
    }

    private void setNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(UPDATE_SAVINGS_GOAL_STRING);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(OK_STRING, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newGoal = input.getText().toString();
                if (isValidNumber(newGoal)) {
                    databaseWriteExecutor.execute(() -> {
                        Savings savings = savingsRepository.getSavings();
                        savings.setTargetAmount(Double.parseDouble(newGoal));
                        savings.setCurrentAmount(0);
                        savingsRepository.updateSavings(savings);
                        savings_goal_text_view.setText(SAVINGS_GOAL_STRING + savings.getTargetAmount());
                        saved_amount_text_view.setText(SAVED_AMOUNT_STRING + savings.getCurrentAmount());

                    });
                } else {
                    Toast.makeText(SavingsActivity.this, INVALID_AMOUNT_ENTERED_BY_USER_STRING,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL_STRING, null);

        AlertDialog goalUpdateDialog = builder.create();
        goalUpdateDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseWriteExecutor != null) {
            databaseWriteExecutor.shutdown();
        }
    }

    public void addSavings(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(ADD_MONEY_STRING);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(OK_STRING, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String moneyToAdd = input.getText().toString();
                if (isValidNumber(moneyToAdd)) {
                    databaseWriteExecutor.execute(() -> {
                        Budget budget = budgetRepository.getBudget();
                        Savings savings = savingsRepository.getSavings();
                        if (budget.getCurrentAmount() > Double.parseDouble(moneyToAdd)) {
                            savings.addMoney(Double.parseDouble(moneyToAdd));
                            budget.pay(Double.parseDouble(moneyToAdd));
                            budgetRepository.updateBudget(budget);
                            savingsRepository.updateSavings(savings);
                            saved_amount_text_view.setText(SAVED_AMOUNT_STRING + savings.getCurrentAmount());
                        } else {
                            runOnUiThread(() -> Toast.makeText(SavingsActivity.this,
                                    AMOUNT_BIGGER_THAN_CURRENT_BUDGET_STRING,
                                    Toast.LENGTH_SHORT).show());
                        }

                    });
                } else {
                    Toast.makeText(SavingsActivity.this, INVALID_AMOUNT_ENTERED_BY_USER_STRING,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(CANCEL_STRING, null);

        AlertDialog addMoneyDialog = builder.create();
        addMoneyDialog.show();
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
