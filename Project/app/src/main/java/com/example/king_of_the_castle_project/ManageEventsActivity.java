package com.example.king_of_the_castle_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/*
 * Class used to manage the screen for viewing organizer's activities. Pulls from firebase
 */
public class ManageEventsActivity extends AppCompatActivity {
    private ListView listView;
    private EventArrayAdapter arrayAdapter;
    private ArrayList<Event> events;
    private FirebaseFirestore db;
    private String androidId;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.organizer_manage_events_screen);
        // Button
        Button returnButton = findViewById(R.id.return_button);
        // Get android id
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        // Initialize the firebase
        db = FirebaseFirestore.getInstance();
        // find listview
        listView = findViewById(R.id.event_list);
        // Initialize the events array
        events = new ArrayList<>();
        // Adapter
        arrayAdapter = new EventArrayAdapter(this, events);
        listView.setAdapter(arrayAdapter);
        // Fetch events from firebase, then events.add
        db.collection(androidId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Event event = document.toObject(Event.class);
//                                    String qrCodeDataString = document.getString("qrCode");
//                                    // Convert back to QR code
//                                    byte[] decodedBytes = Base64.decode(qrCodeDataString, Base64.DEFAULT);
//                                    Bitmap qrCodebitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//                                    // put it into the image view
//                                    ImageView qrCodeImage = findViewById(R.id.organizer_event_qr_code);
//                                    qrCodeImage.setImageBitmap(qrCodebitmap);

                                    events.add(event);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        });

        // Return button
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}
