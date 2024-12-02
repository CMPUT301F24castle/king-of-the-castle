package com.example.king_of_the_castle_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * Activity for administrators to browse and manage images.
 * Displays a list of images stored in Firestore and allows the admin to view or manage them.
 * Provides a return button to navigate back to the previous screen.
 */
public class BrowseImages extends AppCompatActivity {

    private ListView list_of_images;
    private TextView noResultsTextView;
    private AppCompatButton returnButton;
    private ArrayList<String> image_list;
    private ArrayList<String> documentIdList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Called when the activity is first created.
     * Initializes the UI components, fetches the image list from Firestore, and displays it.
     *
     * @param savedInstanceState Contains the data it most recently supplied if the activity is being re-initialized.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_images);

        // Initialize views
        list_of_images = findViewById(R.id.list_of_images);
        noResultsTextView = findViewById(R.id.no_results_textview);
        returnButton = findViewById(R.id.return_button);

        // Initialize an empty image list
        image_list = new ArrayList<>();
        documentIdList = new ArrayList<>();

        // Create and set an adapter for displaying the image list
        AdminImageArrayAdapter ImageArrayAdapter = new AdminImageArrayAdapter(this, image_list, documentIdList);
        list_of_images.setAdapter(ImageArrayAdapter);

        // Fetch image data from Firestore
        db.collection("images")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Populate the image list with data from Firestore
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageData = document.getString("imageData");
                            if (imageData != null) {
                                image_list.add(imageData);
                                documentIdList.add(document.getId());
                            }
                        }
                        // Notify the adapter to refresh the ListView
                        ImageArrayAdapter.notifyDataSetChanged();

                        // Show "no results" message if the list is empty
                        if (image_list.isEmpty()) {
                            noResultsTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        // Handle errors while fetching images
                        Toast.makeText(getApplicationContext(), "Failed to fetch images", Toast.LENGTH_SHORT).show();
                    }
                });

        // Set up the return button to close the activity
        returnButton.setOnClickListener(v -> finish());
    }
}