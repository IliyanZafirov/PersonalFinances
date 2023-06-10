package com.main.personalfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.main.personalfinances.R;
import com.main.personalfinances.data.User;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.views.UserViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private UserViewModel userViewModel;
    private TextView greetingTextView;
    private ExecutorService databaseWriteExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        appDatabase = PersonalFinancesDatabase.getDatabase(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        greetingTextView = findViewById(R.id.greeting_textview);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        userViewModel.init(appDatabase.userDao());
        userViewModel.getUser().observe(this, this::updateGreetingText);

        ImageButton editNameButton = findViewById(R.id.edit_name_image_button);
        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForUserNameUpdate();
            }
        });

        databaseWriteExecutor.execute(() -> {
            appDatabase.dropDatabase();
        });
    }

    private void promptForUserNameUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String userName = input.getText().toString().trim();
            if (!userName.isEmpty()) {
                User currentUser = userViewModel.getUser().getValue();
                if (currentUser != null) {
                    currentUser.setName(userName);
                    databaseWriteExecutor.execute(() -> {
                        appDatabase.userDao().update(currentUser);
                    });
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }



    private void updateGreetingText(User user) {
        if (user != null) {
            String userName = user.getName();
            String greeting = "Hello, " + userName;
            greetingTextView.setText(greeting);
        } else {
            promptForUserNameUpdate();
        }
    }

    private void promptForUserName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String userName = input.getText().toString().trim();
            if (!userName.isEmpty()) {
                User newUser = new User(userName, 1, 1, 1);
                databaseWriteExecutor.execute(() -> {
                    appDatabase.userDao().insert(newUser);
                    runOnUiThread(() -> updateGreetingText(newUser));
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
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
        Intent intent = new Intent(this, TransactionsActivity.class);
        startActivity(intent);
    }

    public void deleteUser(View view) {
        User currentUser = userViewModel.getUser().getValue();
        if (currentUser != null) {
            appDatabase.userDao().delete(currentUser);
            greetingTextView.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseWriteExecutor != null) {
            databaseWriteExecutor.shutdown();

        }
    }
}
