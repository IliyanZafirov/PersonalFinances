package com.main.personalfinances.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.main.personalfinances.R;
import com.main.personalfinances.db.PersonalFinancesDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        greetingTextView = findViewById(R.id.greeting_textview);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();



        ImageButton editNameButton = findViewById(R.id.edit_name_image_button);


        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        databaseWriteExecutor.execute(() -> {
            appDatabase.dropDatabase();
        });
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
