package com.main.personalfinances.data;

import androidx.room.Entity;

import com.main.personalfinances.enums.BillCategory;

import java.util.Date;

@Entity(tableName = "bill_table")
public class Bill extends Transaction{
    private BillCategory category;
    private Date dueDate;

    public Bill(BillCategory category, Date dueDate) {
        super();
        this.category = category;
        this.dueDate = dueDate;
    }

    public BillCategory getCategory() {
        return category;
    }

    public void setCategory(BillCategory category) {
        this.category = category;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "category=" + category +
                ", dueDate=" + dueDate +
                '}';
    }

}
