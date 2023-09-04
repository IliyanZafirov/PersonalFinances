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
    private LocalDateTime dateAdded;
    private LocalDateTime dueDate;
    private double price;

    public Expense(int budgetId, TransactionCategory category,String description, LocalDateTime dateAdded,
                   LocalDateTime dueDate, double price) {
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
        if (dueDate != null && dueDate.isAfter(LocalDateTime.now())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            notificationIntent.putExtra("expense_description", description);

            // Convert LocalDateTime to milliseconds since the Unix epoch
            long triggerMillis = LocalDateTimeToMillis(dueDate);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context, id, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            // Use AlarmManagerCompat to set the alarm
            AlarmManagerCompat.setExactAndAllowWhileIdle(alarmManager, AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent);
        }
    }

    private long LocalDateTimeToMillis(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return TimeUnit.SECONDS.toMillis(instant.getEpochSecond());
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

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
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
