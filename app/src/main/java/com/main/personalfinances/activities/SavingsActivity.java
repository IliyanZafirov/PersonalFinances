package com.main.personalfinances.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.main.personalfinances.R;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.data.User;
import com.main.personalfinances.data.UserAndSavings;
import com.main.personalfinances.data.UserDao;
import com.main.personalfinances.data.UserRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingsActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private UserRepository userRepository;
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
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            UserDao userDao = appDatabase.userDao();
            userRepository = new UserRepository(userDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        savings_goal_text_view = findViewById(R.id.savings_goal);
        saved_amount_text_view = findViewById(R.id.saved_amount);

        ImageButton setSavingsGoalButton = findViewById(R.id.set_savings_goal_button);

        setSavingsGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSavingsDialog();
            }
        });

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        // Retrieve the user and savings details
        LiveData<UserAndSavings> userAndSavingsLiveData = userRepository.getUserAndSavings(1);
        userAndSavingsLiveData.observe(this, new Observer<UserAndSavings>() {
            @Override
            public void onChanged(UserAndSavings userAndSavings) {
                if (userAndSavings != null) {
                    User user = userAndSavings.user;
                    Savings savings = userAndSavings.savings;
                    if (savings != null) {
                        Double savingsGoal = savings.getTargetAmount();
                        if (savingsGoal != null) {
                            savings_goal_text_view.setText("Savings goal: " + String.valueOf(savingsGoal));
                        } else {
                            savings_goal_text_view.setText("Savings goal: N/A");
                        }
                    } else {
                        savings_goal_text_view.setText("Savings goal: N/A");
                    }

                }
            }
        });
    }

    private void addSavingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add new savings goal:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredSavingsGoal = input.getText().toString();
                updateSavingsGoal(enteredSavingsGoal);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateSavingsGoal(String enteredSavingsGoal) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LiveData<UserAndSavings> userWithSavingsLiveData = userRepository.getUserAndSavings(1);
                UserAndSavings userWithSavings = userWithSavingsLiveData.getValue();
                if (userWithSavings != null) {
                    User existingUser = userWithSavings.user;
                    Savings savings = userWithSavings.savings;
                    if (savings != null) {
                        savings.setTargetAmount(Double.valueOf(enteredSavingsGoal));
                        userRepository.updateSavingsGoal(savings);
                        userRepository.update(existingUser);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        savings_goal_text_view.setText(enteredSavingsGoal);
                    }
                });
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseWriteExecutor != null) {
            databaseWriteExecutor.shutdown();
        }
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
