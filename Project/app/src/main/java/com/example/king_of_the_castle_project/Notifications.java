package com.example.king_of_the_castle_project;
import static java.lang.Integer.parseInt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import java.util.List;

//This class will send various types of Notifications to Entrants, including being accepted into an event

public class Notifications {
    private Context context;

    public Notifications(Context context) {
        this.context = context;
    }

    public void sendLotteryNotification(List<Entrant> selectedEntrants) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Entrant entrant : selectedEntrants) {
            String message = "Congratulations. You have been chosen through the lottery! Would you like to accept or decline?";
            //add buttons for accept and decline

            //builds the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setContentTitle("Lottery Status")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(entrant.getId().hashCode(), builder.build());
        }
    }

}
