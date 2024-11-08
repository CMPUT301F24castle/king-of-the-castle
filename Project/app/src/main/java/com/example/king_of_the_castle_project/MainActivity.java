package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;
    private FirebaseFirestore db;
    private String userID;
    private Boolean recognize = Boolean.TRUE;


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



        Button startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recognize) {
                    Intent intent = new Intent(MainActivity.this, ChooseRoleActivity.class);
                    startActivity(intent);
                    //finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginScreenActivity.class);
                    startActivity(intent);
                }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendLotteryNotifications();
        }
    }

    private void sendLotteryNotifications() {
        // Your notification sending code here
    }


}