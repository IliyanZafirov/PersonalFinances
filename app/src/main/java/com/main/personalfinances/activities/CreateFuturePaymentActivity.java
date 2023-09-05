package com.main.personalfinances.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.main.personalfinances.R;
import com.main.personalfinances.daos.FuturePaymentDao;
import com.main.personalfinances.data.FuturePayment;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.enums.FuturePaymentCategory;
import com.main.personalfinances.repositories.FuturePaymentRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateFuturePaymentActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private FuturePaymentDao futurePaymentDao;
    private FuturePaymentRepository futurePaymentRepository;

    private ExecutorService databaseWriteExecutor;
    private Button openDateAndTimeButton;
    private TextView selectedDateTimeText;

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

        openDateAndTimeButton = findViewById(R.id.openDateAndTimePickerButton);
        selectedDateTimeText = findViewById(R.id.editDueDateForFuturePayment);
        Button saveFuturePaymentButton = findViewById(R.id.btnSaveFuturePayment);

        openDateAndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the current date and time
                Calendar calendar = Calendar.getInstance();

                // Show a DatePickerDialog for date selection
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateFuturePaymentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Handle the selected date
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                showTimePickerDialog(calendar);
                            }
                        },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });
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

        String dueDateText = selectedDateTimeText.getText().toString();

        final String description;
        if (editDescription.getText().toString() != null) {
            description = editDescription.getText().toString();
        } else {
            description = null;
        }

        if (!dueDateText.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dueDate = LocalDateTime.parse(dueDateText, formatter);
                if (!LocalDateTime.now().isAfter(dueDate)) {
                    databaseWriteExecutor.execute(() -> {
                        FuturePayment futurePayment = new FuturePayment(1, category, description, dueDate);
                        futurePaymentRepository.insertFuturePayment(futurePayment);

                        runOnUiThread(() -> {
                            futurePayment.scheduleNotification(getApplicationContext());
                            Intent intent = new Intent(this, FuturePaymentActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    });
                } else {
                    showToast("Date can't be in the past");
                }
            } catch (DateTimeParseException e) {
                showToast("Invalid date format. Please use yyyy-MM-dd HH:mm");
            }
        } else {
            showToast("Due date is empty. Please select a date and time.");
        }
    }

    private void showTimePickerDialog(final Calendar calendar) {
        // Show a TimePickerDialog for time selection
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateFuturePaymentActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Handle the selected time
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String selectedDateTime = sdf.format(calendar.getTime());
                        selectedDateTimeText.setText(selectedDateTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
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
