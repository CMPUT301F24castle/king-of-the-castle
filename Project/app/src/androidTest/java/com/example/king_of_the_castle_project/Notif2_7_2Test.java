package com.example.king_of_the_castle_project;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import org.junit.Test;


import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.junit.Before;
import static org.junit.Assert.assertEquals;
import androidx.core.app.NotificationCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class Notif2_7_2Test {
    private Context context;
    private NotificationManager mockNotificationManager;
    private Notif2_7_2 notifier;

    @Before
    public void setUp() {
        // Use real application context for realistic behavior
        context = ApplicationProvider.getApplicationContext();
        mockNotificationManager = Mockito.mock(NotificationManager.class);
        notifier = new Notif2_7_2(context);
    }

    @Test
    public void testsendLotteryNotificationForResponse() {
        // Arrange: Set up the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "sendLotteryNotificationForResponse")
                .setContentTitle("Lottery Status")
                .setContentText("Congratulations. You have been chosen through the lottery! Would you like to accept or decline? (Please note we are still waiting for your response)")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Act: Build the notification
        Notification notification = builder.build();

        // Assert: Check notification properties
        assertEquals("Lottery Status", notification.extras.getString(Notification.EXTRA_TITLE));
        assertEquals("Congratulations. You have been chosen through the lottery! Would you like to accept or decline? (Please note we are still waiting for your response)", notification.extras.getString(Notification.EXTRA_TEXT));
    }


}
