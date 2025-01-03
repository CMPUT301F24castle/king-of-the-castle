package com.example.king_of_the_castle_project;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Activity that allows an entrant to edit their profile information (photo, name, email, phone number)
 */
public class EditProfileActivity extends AppCompatActivity {
    private EditText nameET;
    private EditText emailET;
    private EditText phoneET;

    private AppCompatButton confirmBtn;
    private AppCompatButton returnBut;
    private Button updatePhotoBtn;
    private Button deletePhotoBtn;
    private ImageView profilePhotoIV;

    private FirebaseFirestore db;
    private static String injectedAndroidID; // For testing
    private String androidID;

    // Variables for image selection
    private Uri filePath;
    StorageReference storageReference;
    FirebaseStorage storage;
    private final int PICK_IMAGE_REQUEST = 22;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private Bitmap profileImg;

    /**
     * Default method that performs basic application startup logic
     * @param savedInstanceState
     *          If there was an Instance saved, saved instances restores it
     *
     * @see ProfileActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // get views
        nameET = findViewById(R.id.entrant_name_edit_text_EP);
        emailET = findViewById(R.id.entrant_email_edit_text_EP);
        phoneET = findViewById(R.id.entrant_phone_edit_text_EP);
        profilePhotoIV = findViewById(R.id.profile_photo_IV);

        confirmBtn = findViewById(R.id.confirm_button_EP);
        updatePhotoBtn = findViewById(R.id.update_photo_button_EP);
        deletePhotoBtn = findViewById(R.id.delete_photo_button_EP);
        returnBut = findViewById(R.id.return_button_EP_screen);

        // get database reference and the current user's android id
        db = FirebaseFirestore.getInstance();

        // Use injected Android ID for testing, otherwise get the real ID
        androidID = injectedAndroidID != null ? injectedAndroidID :
                Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Launcher for selecting image from library
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();

                        }
                    }
                });



        // set all edit texts to current profile values
        setCurrentValues();
        // get current profile photo
        getProfilePhoto();
        //showProfileImg();

        // Updating/uploading new photo
        updatePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                uploadImage();
            }
        });

        // Deleting existing photo
        deletePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage();
            }
        });

        // confirm any values changed in the EditTexts
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editFirebaseValues();
            }
        });

        // Return to previous screen
        returnBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

    }

    /**
     * Allows injection of a mock Android ID for testing purposes.
     * @param mockAndroidID The mocked Android ID to use.
     */
    public static void setAndroidIDEPA(String mockAndroidID) {
        injectedAndroidID = mockAndroidID;
    }

    /**
     * Adds the current values of the user's profile to the EditText views
     */
    private void setCurrentValues() {
        // get user from database
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // set edit text values
                            nameET.setText(document.getString("name"));
                            emailET.setText(document.getString("email"));
                            String phone = document.getString("phone");
                            if (phone != null && !phone.isEmpty()) {
                                phoneET.setText(phone);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Adds the current values in the EditText Views to the current user's collection in firebase
     *
     * @see Entrant
     * @see ProfileActivity
     */
    private void editFirebaseValues() {
        // get current values
        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();

        // Check for empty name
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            Toast.makeText(this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("\\d+")) {
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        // create a new entrant with these values
        Entrant entrant = new Entrant(name, email, phone.isEmpty() ? null : phone, androidID);

        /*
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profileImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String stringConversion = Base64.encodeToString(byteArray, Base64.DEFAULT);

         */

        // create a hashmap for the entrant
        Map<String, Object> entrantData = new HashMap<>();
        entrantData.put("name", entrant.getName());
        entrantData.put("email", entrant.getEmail());
        entrantData.put("phone", entrant.getPhoneNumber());
        entrantData.put("id", entrant.getId());
        //entrantData.put("profileImg", stringConversion);


        // add entrant's data to the database
        db.collection("entrants").document(androidID)
                .set(entrantData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // go back to Profile Activity
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    /**
     * Sets the default deterministically-generated profile photo according to the first character of one's name
     */
    private void showProfileImg() {
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String letter = document.getString("name").substring(0, 1);
                            TextDrawable drawable = new TextDrawable.Builder()
                                    .setColor(Color.parseColor("#C7C2EE"))
                                    .setShape(TextDrawable.SHAPE_ROUND_RECT)
                                    .setRadius(10)
                                    .setText(letter)
                                    .setTextColor(Color.BLACK)
                                    .build();
                            profilePhotoIV.setImageDrawable(drawable);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Retrieves the current profile photo, and if it doesn't exist, calls to use a deterministically-generated one
     */
    void getProfilePhoto() {
        db.collection("entrants")
                .whereEqualTo("id", androidID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String photoString = document.getString("profileImg");
                            if (photoString != null && !photoString.isEmpty()) {
                                try {
                                    byte[] decodedBytes = Base64.decode(photoString, Base64.DEFAULT);
                                    profileImg = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                    profilePhotoIV.setImageBitmap(profileImg);
                                } catch (IllegalArgumentException e) {
                                    Toast.makeText(this, "Invalid QR Code Image", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                showProfileImg(); // Show a placeholder if QR code is missing
                            }
                        }
                    }
                });
    }

    /**
     * Result of the launching of an upload image
     * Overridden to save uploaded image as a string so it can be properly saved in Firebase
     * Code used from this tutorial: https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                profilePhotoIV.setImageBitmap(bitmap);
                profileImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                profileImg.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String stringConversion = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Send to firebase
                //db.collection("entrants").document(androidID);
                Map<String, Object> imgData = new HashMap<>();
                //db.collection("entrants").document(androidID); //.update("profileImg", stringConversion);
                imgData.put("profileImg", stringConversion);

                db.collection("entrants").document(androidID)
                        .set(imgData, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Part of the uploading image process
     * Portion which allows one to select from their device's image library
     * Code used from this tutorial: https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
     */
    private void selectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    /**
     * Takes the image selected, and stores it
     * Tries to send to Firebase storage (not used), but the onActivityResult override ensures that it can be stored in the database as a string
     * Code used from this tutorial: https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
     */
    private void uploadImage() {
        if (filePath != null) {

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    Toast.makeText(EditProfileActivity.this,
                                                    "Image Uploaded!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            Toast
                                    .makeText(EditProfileActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                }
                            });



        }
    }

    /**
     * Removes profile image from Firebase, and displays default deterministically-generated profile photo
     */
    private void removeImage() {
        Map<String, Object> data = new HashMap<>();
        data.put("profileImg", null);

        db.collection("entrants").document(androidID)
                .set(data, SetOptions.merge());
        getProfilePhoto();
    }

}