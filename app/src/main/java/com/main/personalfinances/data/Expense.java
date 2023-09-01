package com.main.personalfinances.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.main.personalfinances.enums.TransactionCategory;
import com.main.personalfinances.notification.NotificationReceiver;

import java.util.Date;

@Entity(tableName = "transactions")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int budgetId;
    private TransactionCategory category;
    private String description;
    private Date dateAdded;
    private Date dueDate;
    private double price;

    public Expense(int budgetId, TransactionCategory category,String description, Date dateAdded,
                   Date dueDate, double price) {
        this.budgetId = budgetId;
        this.category = category;
        this.description = description;
        this.dateAdded = dateAdded;
        if (category == TransactionCategory.BILLS || category == TransactionCategory.TAXES
        || category == TransactionCategory.LOAN_SERVICING) {
            this.dueDate = dueDate;
        } else {
            this.dueDate = dateAdded;
        }
        this.price = price;
    }

    public void scheduleNotification(Context context) {
        if (dueDate != dateAdded) {
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            notificationIntent.putExtra("expense description", description);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, id, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            if (dueDate.getTime() > System.currentTimeMillis()) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, dueDate.getTime(), pendingIntent);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public String getDescription() { return description; }

    public void setDescription() { this.description = description; }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }
}
