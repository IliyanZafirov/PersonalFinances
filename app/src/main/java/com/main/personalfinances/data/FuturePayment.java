package com.main.personalfinances.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.AlarmManagerCompat;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.main.personalfinances.enums.FuturePaymentCategory;
import com.main.personalfinances.notification.NotificationReceiver;

import java.time.LocalDate;
import java.time.ZoneId;

@Entity(tableName = "future_payments")
public class FuturePayment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int budgetId;
    private FuturePaymentCategory category;
    private String description;
    private LocalDate dueDate;


    public FuturePayment(int budgetId, FuturePaymentCategory category, String description,
                         LocalDate dueDate) {
        this.budgetId = budgetId;
        this.category = category;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getBudgetId() {
        return budgetId;
    }
    public String getDescription() {
        return description;
    }

    public FuturePaymentCategory getCategory() {
        return category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void scheduleNotification(Context context) {
        if (dueDate != null && dueDate.isAfter(LocalDate.now())) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            notificationIntent.putExtra("future_payment_description", description);

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
}
