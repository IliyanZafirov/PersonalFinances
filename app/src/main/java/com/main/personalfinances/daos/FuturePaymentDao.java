package com.main.personalfinances.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.main.personalfinances.data.FuturePayment;

import java.util.List;

@Dao
public interface FuturePaymentDao {
    @Insert
    public void insertFuturePayment(FuturePayment futurePayment);
    @Update
    public void updateFuturePayment(FuturePayment futurePayment);
    @Delete
    public void deleteFuturePayment(FuturePayment futurePayment);

    @Query("SELECT * FROM future_payments")
    public LiveData<List<FuturePayment>> getAllFuturePayments();
}
