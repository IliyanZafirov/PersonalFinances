package com.main.personalfinances.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.main.personalfinances.enums.TransactionCategory;
import com.main.personalfinances.notification.NotificationReceiver;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "transactions")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int budgetId;
    private TransactionCategory category;
    private String description;
    private LocalDate dateAdded;
    private LocalDate dueDate;
    private double price;

    public Expense(int budgetId, TransactionCategory category,String description, LocalDate dateAdded,
                   LocalDate dueDate, double price) {
        this.budgetId = budgetId;
        this.category = category;
        this.description = description;
        this.dateAdded = dateAdded;
        this.price = price;
        this.dueDate = dueDate;
    }

    public void scheduleNotification(Context context) {
        if (dueDate != null && dueDate.isAfter(LocalDate.now())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            notificationIntent.putExtra("expense_description", description);

            // Convert LocalDate to milliseconds since the Unix epoch
            long triggerMillis = LocalDateToMillis(dueDate);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, id, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            // Use AlarmManagerCompat to set the alarm
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
        }
    }

    private long LocalDateToMillis(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private boolean categoryRequiresDueDate(TransactionCategory category) {
        return category == TransactionCategory.BILLS ||
                category == TransactionCategory.TAXES ||
                category == TransactionCategory.LOAN_SERVICING;
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

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
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
