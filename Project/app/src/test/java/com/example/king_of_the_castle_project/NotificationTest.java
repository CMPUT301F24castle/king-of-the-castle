package com.example.king_of_the_castle_project;

import android.app.NotificationManager;
import android.content.Context;
import android.app.Notification;
import androidx.core.app.NotificationCompat;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotificationTest {
    private Notifications notifications;
    private Context context;
    private NotificationManager mockNotificationManager;

    @Before
    public void setUp() {
        // Use the real app context and NotificationManager
        context = ApplicationProvider.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notifications = new Notifications(context); // Assuming Notifications class uses context for system services
    }

    private List<Entrant> createSampleEntrants() {
        List<Entrant> entrants = new ArrayList<>();
        entrants.add(new Entrant("Alice", "alice@example.com", "555-1234", "id1"));
        entrants.add(new Entrant("Bob", "bob@example.com", "555-5678", "id2"));
        return entrants;
    }

    @Test
    public void testSendLotteryNotification() {
        // Trigger the lottery notification
        List<Entrant> entrants = createSampleEntrants();
        notifications.sendLotteryNotification(entrants); // Sends a notification

        // In a real scenario, we cannot directly check the notification tray with Espresso.
        // Instead, you would verify that UI elements update after the notification is triggered.
        // For example, the app might display a "Notification received" button.

        // Use Espresso to check UI after notification (for example, checking if a button is displayed)
        Espresso.onView(ViewMatchers.withId(R.id.notification_button)) // Assume a button is shown after notification
                .check(matches(ViewMatchers.isDisplayed())); // Check if the button is visible after notification

        // Alternatively, you can test if the NotificationManager received a call to notify (use reflection to inspect NotificationManager).
        // However, Espresso itself can't directly verify the notification in the system tray.
    }



}
