package com.main.personalfinances.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.main.personalfinances.R;
import com.main.personalfinances.adapter.FuturePaymentAdapter;
import com.main.personalfinances.daos.FuturePaymentDao;
import com.main.personalfinances.data.FuturePayment;
import com.main.personalfinances.db.PersonalFinancesDatabase;
import com.main.personalfinances.repositories.FuturePaymentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FuturePaymentActivity extends AppCompatActivity {

    private PersonalFinancesDatabase appDatabase;
    private FuturePaymentRepository futurePaymentRepository;
    private FuturePaymentDao futurePaymentDao;

    private ExecutorService databaseWriteExecutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futurepayments);

        Toolbar myToolbar = findViewById(R.id.transactions_toolbar);
        setSupportActionBar(myToolbar);

        try {
            appDatabase = PersonalFinancesDatabase.getDatabase(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        futurePaymentDao = appDatabase.futurePaymentDao();
        futurePaymentRepository = new FuturePaymentRepository(futurePaymentDao);
        databaseWriteExecutor = Executors.newSingleThreadExecutor();
        RecyclerView recyclerView = findViewById(R.id.futurepayments_recyclerView);
        LiveData<List<FuturePayment>> liveDataFuturePaymentList = futurePaymentRepository.getAllFuturePayments();
        FuturePaymentAdapter adapter = new FuturePaymentAdapter(liveDataFuturePaymentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        removeOldFuturePayments();
    }

    private void removeOldFuturePayments() {
        LiveData<List<FuturePayment>> allFuturePayments = futurePaymentRepository.getAllFuturePayments();
        allFuturePayments.observe(this, new Observer<List<FuturePayment>>() {
            @Override
            public void onChanged(List<FuturePayment> futurePayments) {
                LocalDate currentDate = LocalDate.now();
                for (FuturePayment futurePayment: futurePayments) {
                    LocalDate dueDate = futurePayment.getDueDate();
                    if (dueDate != null && dueDate.isBefore(currentDate)) {
                        removeFuturePayment(futurePayment);
                    }
                }
            }
        });
    }

    private void removeFuturePayment(FuturePayment futurePayment) {
        databaseWriteExecutor.execute(() -> futurePaymentRepository.deleteFuturePayment(futurePayment));
    }


    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToFuturePaymentForm(View view) {
        Intent intent = new Intent(this, CreateFuturePaymentActivity.class);
        startActivity(intent);
    }
}
