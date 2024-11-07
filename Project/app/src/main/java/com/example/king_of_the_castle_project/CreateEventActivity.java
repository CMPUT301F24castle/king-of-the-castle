package com.example.king_of_the_castle_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imageSelectorLauncher;
    protected boolean geolocation_check = false;
    private FirebaseFirestore db;
    private String androidId;
    private Bitmap qrCodeBitmap;

    /**
     * Default method for basic startup logic
     * @param savedInstanceState
     *      If there was an Instance saved, saved instances restores it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create_screen);
        // Get all event details
        EditText eventName = findViewById(R.id.event_name_edit_text);
        EditText eventDate = findViewById(R.id.event_date_edit_text);
        EditText eventLocation = findViewById(R.id.event_location_edit_text);
        EditText eventDetails = findViewById(R.id.event_details_edit_text);
        EditText eventMaxParticipants = findViewById(R.id.event_max_participants_edit_text);
        // however to check photos
        Button uploadImageButton = findViewById(R.id.upload_photo_button);
        Button returnButton = findViewById(R.id.return_button);
        // Geolocation checkbox
        CheckBox eventGeolocation = findViewById(R.id.event_geolocation_checkbox);

        // "Create Event" button
        Button eventCreation = findViewById(R.id.create_event_button);

        // update geolocation bool whenever applicable
        eventGeolocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            geolocation_check = isChecked;
        });

        // Image information retriever
        imageSelectorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                    }
                }
        );

        // event listener for selecting a photo
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open image selector
                imageSelector();
            }
        });

        // event listener for "create event"
        eventCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // fetch info
                String name = eventName.getText().toString();
                String location = eventLocation.getText().toString();
                String details = eventDetails.getText().toString();
                String maxParticipants = eventMaxParticipants.getText().toString();
                String date = eventDate.getText().toString();
                String time = "12:00";
                int number = 50000; // Max participants for now
                // Fetch firebase first
                db = FirebaseFirestore.getInstance();
                // Verify date input
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()); // date formatter
                sdf.setLenient(false);
                try { // Convert date to correct format
                    Date parsedDate = sdf.parse(date);
                } catch (ParseException e) { // Invalid date
                    displayToastNotification("Please enter a vaid date. Returning to organizer home screen.");
                    finish();
                }

                // Conversion of maxparticipants to int
                try {
                    number = Integer.parseInt(maxParticipants);
                } catch (NumberFormatException e) {
                    // Throw toast dialogue, set participants to max
                    displayToastNotification("Invalid or no max participants set, automatically set to max.");
                }
                // Create QR code
                String qrCodeText = String.format("EventDetails: %s", name);

                try {
                    qrCodeBitmap = createQRCode(qrCodeText);
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                    displayToastNotification("Error: Failed to generate QR Code");
                    finish();
                }
                // base64 string conversion so it can be stored in firebase
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String stringConversion = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Create empty waitlist
                ArrayList<String> waitlist = new ArrayList<String>();
                // Create event then send to firebase
                Event newEvent = new Event(name, date, time, location, details, number, waitlist, geolocation_check);
                // Putting QR stuff into event class
                newEvent.setQrCodeData(stringConversion);
                // Sending to firebase
                androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                sendToFirebase(newEvent, androidId, stringConversion);
                // Passing data back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventCreated", name);
                setResult(RESULT_OK, resultIntent);
                // return
                finish();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // return
                finish();
            }
        });
    }

    /**
     * Method to transition into the image selection screen as well as to return it
     */
    private void imageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageSelectorLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    /**
     * Function used to display toast notification
     * @param message
     *      Message parameter to pass what should be shown in the notification
     */
    protected void displayToastNotification(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_notification_layout, findViewById(R.id.custom_toast_container));
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    /**
     * Method that creates and returns the QRcode data, generates QR code.
     * @param qrCodeText
     *      Takes in data for what the QR code should display
     * @return
     *      Returns the bitmap that makes up the QR code
     * @throws WriterException
     *      Exception to be caught if the creation of the QR code fails
     * @throws IOException
     *      Exception to be caught if the QR code creation fails
     */
    private Bitmap createQRCode(String qrCodeText) throws WriterException, IOException {
        int size = 512;
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        int matrixWidth = byteMatrix.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixWidth, Bitmap.Config.RGB_565);
        for (int x = 0; x < matrixWidth; x++) {
            for (int y = 0; y < matrixWidth; y++) {
                bitmap.setPixel(x, y, byteMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return bitmap;
    }

    /**
     * Method to send an event to the database
     * @param event
     *      Event to send to the firebase
     * @param androidId
     *      HWID, used to identify the collection that holds the events
     * @param qrCodeData
     *      Used to pass the 64byte string QR Code data into the firebase
     */
    private void sendToFirebase(Event event, String androidId, String qrCodeData) {
        // Create a new object that can be stored for event that does not use waitlist because it will bug :)
        String name = event.getName();
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("date", event.getDate());
        eventData.put("time", event.getTime());
        eventData.put("location", event.getLocation());
        eventData.put("details", event.getEventDetails());
        eventData.put("maxParticipants", event.getMaxParticipants());
        eventData.put("waitList", event.getWaitList());
        eventData.put("qrCodeData", qrCodeData);
        // Create new document or add to collection
        db.collection(androidId)
                .document(name)
                .set(eventData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Successful send to firebase");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore: ", "Failed to add event to firebase", e);
                    // displayToastNotification("Failed to add event" + e.getMessage());
                });
        // Add to collection of events
        db.collection("events")
                .document(name)
                .set(eventData);
    }
}