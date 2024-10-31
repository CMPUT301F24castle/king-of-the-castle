package com.example.king_of_the_castle_project;
import static java.lang.Integer.parseInt;

import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;
import java.util.List;

//This class will send various types of Notifications to Entrants, including being accepted into an event

/**
 * This is a class that defines all notifications needed for user stories
 */
public class Notifications {
    private Context context;

    public Notifications(Context context) {
        this.context = context;
    }


    /**
     * This will send a notification to all chosen entrants (from lottery class) asking if they would like to accept or decline their invitation
     * 2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
     * @param selectedEntrants
     * This is the list of selected entrants by the lottery class
     */
    //2.5.1: As an organiser I want to send a notification to chosen entrants to sign up for events
    public void sendLotteryNotification(List<Entrant> selectedEntrants) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Entrant entrant : selectedEntrants) {
            String message = "Congratulations. You have been chosen through the lottery! Would you like to accept or decline?";
            //(ADD) buttons for accept and decline

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

    /**
     * This will send a notification to all entrants on the waiting list, whether they have been chosen or not
     * 2.7.1: As an organiser I want to send notifications to all entrants on the waiting list
     * @param waitingList
     * This is the waiting list that consists of all entrants that scanned the QR code and enrolled in the event
     */
    //2.7.1: As an organiser I want to send notifications to all entrants on the waiting list
    public void notifyWaitingListEntrants(List<Entrant> waitingList) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Entrant entrant : waitingList) {
            //(ADD) the message will be a text that the organizer will input
            String message = "notification description";


            //builds the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setContentTitle("notification title")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(entrant.getId().hashCode(), builder.build());
        }
    }

    /**
     * This sends a notification to all cancelled entrants
     * 2.7.3: As an organiser I want to send a notification to all cancelled entrants
     * @param cancelledList
     * this is a list of all entrants who chose to cancel
     */
    //2.7.3: As an organiser I want to send a notification to all cancelled entrants
    public void notifyCancelledEntrants(List<Entrant> cancelledList) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        for (Entrant entrant : cancelledList) {
            String message = "notification description";
            //(ADD) the message will be a text that the organizer inputs



            //builds the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setContentTitle("notification title")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true); //dismiss notification when clicked

            // Send the notification with hashCode() as ID
            notificationManager.notify(entrant.getId().hashCode(), builder.build());
        }
    }



}
