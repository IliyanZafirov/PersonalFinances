package com.main.personalfinances.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.main.personalfinances.R;

/**
 * Used for future payments notification
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String futurePaymentDescription = intent.getStringExtra("future_payment_description");
        showNotification(context, futurePaymentDescription);
    }

    private void showNotification(Context context, String futurePaymentDescription) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "future_payment_channel", "Payment Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "future_payment_channel")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Payment Due")
                        .setContentText("Don't forget to pay: " + futurePaymentDescription)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0, builder.build());
    }
}
