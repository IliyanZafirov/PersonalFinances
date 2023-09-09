package com.main.personalfinances.repositories;

import androidx.lifecycle.LiveData;

import com.main.personalfinances.dao.FuturePaymentDao;
import com.main.personalfinances.model.FuturePayment;

import java.util.List;

public class FuturePaymentRepository {

    FuturePaymentDao futurePaymentDao;

    public FuturePaymentRepository(FuturePaymentDao futurePaymentDao) {
        this.futurePaymentDao = futurePaymentDao;
    }

    public void insertFuturePayment(FuturePayment futurePayment) {
        futurePaymentDao.insertFuturePayment(futurePayment);
    }

    public void deleteFuturePayment(FuturePayment futurePayment) {
        futurePaymentDao.deleteFuturePayment(futurePayment);
    }

    public LiveData<List<FuturePayment>> getAllFuturePayments() {
        return futurePaymentDao.getAllFuturePayments();
    }
}
