package com.main.personalfinances.activities;

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
import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.daos.SavingsDao;
import com.main.personalfinances.data.Budget;
import com.main.personalfinances.data.BudgetRepository;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.data.SavingsRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingsActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private SavingsRepository savingsRepository;
    private BudgetRepository budgetRepository;

    private SavingsDao savingsDao;
    private BudgetDao budgetDao;

    private AlertDialog goalUpdateDialog;
    private AlertDialog addMoneyDialog;
    private ExecutorService databaseWriteExecutor;
    private TextView savings_goal_text_view;
    private TextView saved_amount_text_view;

    private Button add_money_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        Toolbar myToolbar = findViewById(R.id.savings_toolbar);
        setSupportActionBar(myToolbar);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            savingsDao = appDatabase.savingsDao();
            savingsRepository = new SavingsRepository(savingsDao);
            budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        savings_goal_text_view = findViewById(R.id.savings_goal);
        saved_amount_text_view = findViewById(R.id.saved_amount);
        add_money_button = findViewById(R.id.add_money_button);
        ImageButton setSavingsGoalButton = findViewById(R.id.set_savings_goal_button);


        databaseWriteExecutor.execute(() -> {
           Savings savings = savingsRepository.getSavings();
           if(savings != null) {
               runOnUiThread(() -> {
                   savings_goal_text_view.setText("Savings goal: " + savings.getTargetAmount());
                   saved_amount_text_view.setText("Saved amount: " + savings.getCurrentAmount());
               });
           }
        });

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

    private void setNewGoal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update savings goal:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newGoal = input.getText().toString();
                if(isValidNumber(newGoal)) {
                    databaseWriteExecutor.execute(()-> {
                        Savings savings = savingsRepository.getSavings();
                        savings.setTargetAmount(Double.parseDouble(newGoal));
                        savings.setCurrentAmount(0);
                        savingsRepository.updateSavings(savings);
                        savings_goal_text_view.setText("Savings goal: " + savings.getTargetAmount());
                        saved_amount_text_view.setText("Saved amount: " + savings.getCurrentAmount());

                    });
                } else {
                    Toast.makeText(SavingsActivity.this, "You didn't enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        goalUpdateDialog = builder.create();
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
        builder.setMessage("Add money:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String moneyToAdd = input.getText().toString();
                if(isValidNumber(moneyToAdd)) {
                    databaseWriteExecutor.execute(()-> {
                        Budget budget = budgetRepository.getBudget();
                        Savings savings = savingsRepository.getSavings();
                        if(budget.getCurrentAmount() > Double.parseDouble(moneyToAdd)) {
                            savings.addMoney(Double.parseDouble(moneyToAdd));
                            budget.pay(Double.parseDouble(moneyToAdd));
                            budgetRepository.updateBudget(budget);
                            savingsRepository.updateSavings(savings);
                            saved_amount_text_view.setText("Saved amount: " + savings.getCurrentAmount());
                        } else {
                            runOnUiThread(()-> {
                                Toast.makeText(SavingsActivity.this, "Amount bigger than current budget",
                                        Toast.LENGTH_SHORT).show();
                            });
                        }

                    });
                } else {
                    Toast.makeText(SavingsActivity.this, "You didn't enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        addMoneyDialog = builder.create();
        addMoneyDialog.show();
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
