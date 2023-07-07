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
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingsActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        savings_goal_text_view = findViewById(R.id.savings_goal);
        saved_amount_text_view = findViewById(R.id.saved_amount);

        ImageButton setSavingsGoalButton = findViewById(R.id.set_savings_goal_button);

        setSavingsGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        databaseWriteExecutor = Executors.newSingleThreadExecutor();

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
