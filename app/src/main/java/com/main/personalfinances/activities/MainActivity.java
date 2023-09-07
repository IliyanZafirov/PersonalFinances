package com.main.personalfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.main.personalfinances.R;
import com.main.personalfinances.daos.BudgetDao;
import com.main.personalfinances.daos.SavingsDao;
import com.main.personalfinances.data.Budget;
import com.main.personalfinances.repositories.BudgetRepository;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.repositories.SavingsRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
    private AlertDialog nameUpdateDialog;
    private AlertDialog nameInsertDialog;
    private BudgetRepository budgetRepository;
    private SavingsRepository savingsRepository;
    private SharedPreferences userSharedPref;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView greetingTextView;
    private ExecutorService databaseWriteExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        try {
            PersonalFinancesDatabase appDatabase = PersonalFinancesDatabase.getDatabase(this);
            BudgetDao budgetDao = appDatabase.budgetDao();
            budgetRepository = new BudgetRepository(budgetDao);
            SavingsDao savingsDao = appDatabase.savingsDao();
            savingsRepository = new SavingsRepository(savingsDao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        greetingTextView = findViewById(R.id.greeting_textview);
        ImageButton editNameButton = findViewById(R.id.edit_name_image_button);

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        userSharedPref = getSharedPreferences("user_preferences", Context.MODE_PRIVATE);

        // TODO: if this doesn't work on lower API, change POST_NOTIFICATIONS with VIBRATE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_CODE);
            initializeApp();
        } else {
            initializeApp();
        }

        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptUserForNameUpdate();
            }
        });
    }

    private void initializeApp() {
        userAuthenticate();

        databaseWriteExecutor.execute(() -> {
            Budget existingBudget = budgetRepository.getBudget();
            if (existingBudget == null) {
                Budget newBudget = new Budget(0);
                long budgetId = budgetRepository.insertBudget(newBudget);
                Savings savings = new Savings(budgetId, 0);
                long savingsId = savingsRepository.insertSavings(savings);
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
        }).setCancelable(false);

        nameInsertDialog = builder.create();
        nameInsertDialog.show();
    }

    public void userAuthenticate() {
        if (userSharedPref.getString("1", "User").equals("User")) {
            promptUserForName();
        } else {
            String username = userSharedPref.getString("1", "User");
            greetingTextView.setText("Hello, " + username);
        }
    }

    /**
     * Double-click to exit the application instead of returning to previous activity
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            // If the user presses back again within a short time, exit the app
            finish();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            // Reset the flag after a short delay
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000); // 2 seconds
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

    public void goToFuturePayments(View view) {
        Intent intent = new Intent(this, FuturePaymentActivity.class);
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
