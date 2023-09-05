package com.main.personalfinances.repositories;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.daos.FuturePaymentDao;
import com.main.personalfinances.data.FuturePayment;

import java.util.List;

public class FuturePaymentRepository {

    FuturePaymentDao futurePaymentDao;

    public FuturePaymentRepository(FuturePaymentDao futurePaymentDao) {
        this.futurePaymentDao = futurePaymentDao;
    }


    public void insertFuturePayment(FuturePayment futurePayment) {
        futurePaymentDao.insertFuturePayment(futurePayment);
    }

    public void updateFuturePayment(FuturePayment futurePayment) {
        futurePaymentDao.updateFuturePayment(futurePayment);
    }

    public void deleteFuturePayment(FuturePayment futurePayment) {
        futurePaymentDao.deleteFuturePayment(futurePayment);
    }

    public LiveData<List<FuturePayment>> getAllFuturePayments() {
        return futurePaymentDao.getAllFuturePayments();
    }
}
