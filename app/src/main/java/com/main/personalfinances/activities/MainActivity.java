package com.main.personalfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;

    private AlertDialog nameUpdateDialog;

    private AlertDialog nameInsertDialog;

    BudgetDao budgetDao;

    BudgetRepository budgetRepository;

    SavingsDao savingsDao;

    SavingsRepository savingsRepository;

    SharedPreferences userSharedPref;

    private TextView greetingTextView;
    private ExecutorService databaseWriteExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
            budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
            savingsDao = appDatabase.savingsDao();
            savingsRepository = new SavingsRepository(savingsDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        greetingTextView = findViewById(R.id.greeting_textview);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        userSharedPref = getSharedPreferences("user_preferences", Context.MODE_PRIVATE);

        ImageButton editNameButton = findViewById(R.id.edit_name_image_button);


        userAuthenticate();

        databaseWriteExecutor.execute(()-> {
            Budget existingBudget = budgetRepository.getBudget();
            if(existingBudget == null) {
                Budget newBudget = new Budget(10000);
                long budgetId = budgetRepository.insertBudget(newBudget);

                Savings savings = new Savings(budgetId, 0);
                long savingsId = savingsRepository.insertSavings(savings);

            }
        });


        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptUserForNameUpdate();
            }
        });


    }

    private void promptUserForNameUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update your name:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredName = input.getText().toString();
                SharedPreferences.Editor editor = userSharedPref.edit();
                editor.putString("1", enteredName);
                editor.apply();

                String username = userSharedPref.getString("1", "User");

                greetingTextView.setText("Hello, " + username);

            }
        });

        builder.setNegativeButton("Cancel", null);

        nameUpdateDialog = builder.create();
        nameUpdateDialog.show();
    }


    private void promptUserForName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter your name:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String enteredName = input.getText().toString();

                        SharedPreferences.Editor editor = userSharedPref.edit();
                        editor.putString("1", enteredName);
                        editor.apply();

                        String username = userSharedPref.getString("1", "User");

                        greetingTextView.setText("Hello, " + username);
                    }
                })
                .setCancelable(false);

        nameInsertDialog = builder.create();
        nameInsertDialog.show();
    }

    public void userAuthenticate() {
        if(userSharedPref.getString("1","User").equals("User")) {
            promptUserForName();
        } else {
            String username = userSharedPref.getString("1", "User");
            greetingTextView.setText("Hello, " + username);
        }
    }

    public void goToBudget(View view) {
        Intent intent = new Intent(this, BudgetActivity.class);
        startActivity(intent);
    }

    public void goToSavings(View view) {
        Intent intent = new Intent(this, SavingsActivity.class);
        startActivity(intent);
    }

    public void goToTransactions(View view) {
        Intent intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nameUpdateDialog != null && nameUpdateDialog.isShowing()) {
            nameUpdateDialog.dismiss();
        }
        if (nameInsertDialog != null && nameInsertDialog.isShowing()) {
            nameInsertDialog.dismiss();
        }
        if (databaseWriteExecutor != null) {
            databaseWriteExecutor.shutdown();
        }
    }
}
