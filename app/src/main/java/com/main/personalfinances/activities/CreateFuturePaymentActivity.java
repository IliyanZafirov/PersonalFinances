package com.main.personalfinances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.main.personalfinances.R;
import com.main.personalfinances.daos.FuturePaymentDao;
import com.main.personalfinances.data.FuturePayment;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.enums.FuturePaymentCategory;
import com.main.personalfinances.repositories.FuturePaymentRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateFuturePaymentActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private FuturePaymentDao futurePaymentDao;
    private FuturePaymentRepository futurePaymentRepository;

    private ExecutorService databaseWriteExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futurepayment_form);

        Toolbar myToolbar = findViewById(R.id.futurepayments_form_toolbar);
        setSupportActionBar(myToolbar);


        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        futurePaymentDao = appDatabase.futurePaymentDao();
        futurePaymentRepository = new FuturePaymentRepository(futurePaymentDao);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();

        Spinner categorySpinner = findViewById(R.id.spinnerCategoryForFuturePayment);
        FuturePaymentCategory[] categories = FuturePaymentCategory.values();
        ArrayAdapter<FuturePaymentCategory> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        categorySpinner.setAdapter(categoryAdapter);

        Button saveFuturePaymentButton = findViewById(R.id.btnSaveFuturePayment);
        saveFuturePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createExpense();
            }
        });
    }

    private void createExpense() {
        Spinner categorySpinner = findViewById(R.id.spinnerCategoryForFuturePayment);
        FuturePaymentCategory category = (FuturePaymentCategory) categorySpinner.getSelectedItem();
        EditText editDescription = findViewById(R.id.editDescriptionForFuturePayment);
        EditText editDueDate = findViewById(R.id.editDueDateForFuturePayment);

        String dueDateString = editDueDate.getText().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        final String description;
        if (editDescription.getText().toString() != null) {
            description = editDescription.getText().toString();
        } else {
            description = null;
        }
        LocalDate dueDate;
        try {
            if (isValidDate(dueDateString, "yyyy-MM-dd")) {
                dueDate = LocalDate.parse(dueDateString, formatter);
                if (dueDate.isBefore(LocalDate.now())) {
                    runOnUiThread(() -> showToast("Due date can't be in the past"));
                } else {
                    databaseWriteExecutor.execute(() -> {
                        FuturePayment futurePayment = new FuturePayment(1, category, description,
                                dueDate);
                        futurePaymentRepository.insertFuturePayment(futurePayment);

                        runOnUiThread(() -> {
                            futurePayment.scheduleNotification(getApplicationContext());
                            Intent intent = new Intent(this, FuturePaymentActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                }
            } else {
                runOnUiThread(() -> showToast("Invalid due date"));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void showToast(String s) {
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void goToFuturePayments(View view) {
        Intent intent = new Intent(this, FuturePaymentActivity.class);
        startActivity(intent);
    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
