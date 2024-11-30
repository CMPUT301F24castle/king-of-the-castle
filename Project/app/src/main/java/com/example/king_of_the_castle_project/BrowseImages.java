package com.example.king_of_the_castle_project;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * Activity that shows the admin a list of all images and allows the admin to remove the images.
 */
public class BrowseImages extends AppCompatActivity {

    private ListView list_of_images;
    private TextView noResultsTextView;
    private AppCompatButton returnButton;
    private ArrayList<String> image_list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_images);

        // get views
        list_of_images = findViewById(R.id.list_of_images);
        noResultsTextView = findViewById(R.id.no_results_textview);
        returnButton = findViewById(R.id.return_button);

        // init empty entrant list
        image_list = new ArrayList<>();

        // create adapter
        AdminImageArrayAdapter ImageArrayAdapter = new AdminImageArrayAdapter(this, image_list);

        // set view to adapter
        list_of_images.setAdapter(ImageArrayAdapter);

        db.collection("images")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageData = document.getString("imageData");
                            if (imageData != null) {
                                image_list.add(imageData);
                            }
                        }
                        ImageArrayAdapter.notifyDataSetChanged();

                        if (image_list.isEmpty()) {
                            noResultsTextView.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        // handle failure
                        Toast.makeText(getApplicationContext(), "Failed to fetch images", Toast.LENGTH_SHORT).show();
                    }
                });


        returnButton.setOnClickListener(v -> finish());

    }

}