package com.main.personalfinances.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.main.personalfinances.data.FuturePayment;

import java.util.List;

@Dao
public interface FuturePaymentDao {
    @Insert
    void insertFuturePayment(FuturePayment futurePayment);

    @Delete
    void deleteFuturePayment(FuturePayment futurePayment);

    @Query("SELECT * FROM future_payments")
    LiveData<List<FuturePayment>> getAllFuturePayments();
}
