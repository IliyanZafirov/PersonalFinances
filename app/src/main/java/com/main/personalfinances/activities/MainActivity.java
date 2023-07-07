package com.main.personalfinances.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.main.personalfinances.R;
import com.main.personalfinances.data.Savings;
import com.main.personalfinances.data.User;
import com.main.personalfinances.daos.UserDao;
import com.main.personalfinances.data.UserRepository;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;

    private UserRepository userRepository;

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
            UserDao userDao = appDatabase.userDao();
            userRepository = new UserRepository(userDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        greetingTextView = findViewById(R.id.greeting_textview);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();



        ImageButton editNameButton = findViewById(R.id.edit_name_image_button);
        LiveData<User> userLiveData = userRepository.getUser(1);
        userLiveData.observe(this, user -> {
            if (user != null) {
                String userName = user.getName();
                greetingTextView.setText("Hello, " + userName);
            } else {
                promptUserForName();
            }
        });

        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptUserForNameUpdate();
            }
        });

//        databaseWriteExecutor.execute(() -> {
//            appDatabase.dropDatabase();
//        });
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

                LiveData<User> userLiveData = userRepository.getUser(1);
                userLiveData.observe(MainActivity.this, new Observer<User>() {
                    @Override
                    public void onChanged(User existingUser) {
                        if (existingUser != null) {
                            existingUser.setName(enteredName);

                            databaseWriteExecutor.execute(() -> {
                                userRepository.update(existingUser);

                                runOnUiThread(() -> greetingTextView.setText("Hello, " + enteredName));
                            });
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void promptUserForName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter your name:");

        // Create an EditText widget programmatically
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Retrieve the entered name from the EditText
                        String enteredName = input.getText().toString();

                        // Insert the new user into the database
                        databaseWriteExecutor.execute(() -> {
                            User newUser = new User(enteredName);
                            userRepository.insertUser(newUser);

                            Savings savings = new Savings();
                            savings.setUserId(newUser.getId());
                            userRepository.insertSavings(savings);

                            // Update the greetingTextView with the new user's name
                            runOnUiThread(() -> greetingTextView.setText("Hello, " + enteredName));
                        });
                    }
                })
                .setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseWriteExecutor != null) {
            databaseWriteExecutor.shutdown();
        }
    }
}
