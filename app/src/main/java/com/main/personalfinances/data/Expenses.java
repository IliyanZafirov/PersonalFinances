package com.main.personalfinances.data;

import androidx.room.Entity;

import com.main.personalfinances.enums.ExpenseCategory;

import java.util.Date;

@Entity(tableName = "expense_table")
public class Expenses extends Transaction{

    private ExpenseCategory expenseCategory;
    private String productName;
    private Date dateOfPurchase;

    public Expenses(ExpenseCategory expenseCategory, String productName, Date dateOfPurchase) {
        this.expenseCategory = expenseCategory;
        this.productName = productName;
        this.dateOfPurchase = dateOfPurchase;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(ExpenseCategory expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "expenseCategory=" + expenseCategory +
                ", productName='" + productName + '\'' +
                ", dateOfPurchase=" + dateOfPurchase +
                '}';
    }
}
