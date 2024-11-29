package com.example.king_of_the_castle_project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

/**
 * Main activity screen that starts the app
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private FirebaseFirestore db;
    private String userID;
    private Boolean recognize = Boolean.FALSE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        userID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        Button startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("entrants").document(userID)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, ChooseRoleActivity.class);
                                startActivity(intent);
                                recognize = Boolean.TRUE;
                    }
                            else {
                                Intent intent = new Intent(MainActivity.this, LoginScreenActivity.class);
                                startActivity(intent);
                            }
                });

                /*
                if (recognize) {
                    Intent intent = new Intent(MainActivity.this, ChooseRoleActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginScreenActivity.class);
                    startActivity(intent);
                }

                 */
            }
        });








        //notification permissions
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String notificationPermission = "android.permission.POST_NOTIFICATIONS";
            if (ContextCompat.checkSelfPermission(this, notificationPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{notificationPermission},
                        REQUEST_NOTIFICATION_PERMISSION);
            } else {
                sendLotteryNotifications();
            }
        } else {
            sendLotteryNotifications();
        }*/


    }


  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendLotteryNotification();
        }
    } */








}