package com.main.personalfinances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.main.personalfinances.R;
import com.main.personalfinances.activities.MainActivity;
import com.main.personalfinances.db.PersonalFinancesDatabase;


import java.util.concurrent.ExecutorService;

public class SavingsActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;

    private ExecutorService databaseWriteExecutor;

    private TextView savings_goal_text_view;

    private TextView saved_amount_text_view;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.savings_toolbar);
        setSupportActionBar(myToolbar);

        savings_goal_text_view = findViewById(R.id.savings_goal);
        saved_amount_text_view = findViewById(R.id.saved_amount);

        ImageButton setSavingsGoalButton = findViewById(R.id.set_savings_goal_button);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}