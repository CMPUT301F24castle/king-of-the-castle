package com.example.king_of_the_castle_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Custom adapter for displaying a list of images to administrators.
 * Provides functionality for rendering Base64-encoded images and removing images from Firestore.
 */
public class AdminImageArrayAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> imageList;
    private ArrayList<String> documentIds; // List of document IDs from Firestore

    /**
     * Constructor to initialize the adapter.
     *
     * @param context     The current context.
     * @param images      A list of Base64-encoded image strings.
     * @param documentIds A list of Firestore document IDs corresponding to the images.
     */
    public AdminImageArrayAdapter(@NonNull Context context, ArrayList<String> images, ArrayList<String> documentIds) {
        super(context, R.layout.admin_image_list_item, images);
        this.context = context;
        this.imageList = images;
        this.documentIds = documentIds;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_image_list_item, parent, false);
        }

        // Retrieve the Base64-encoded image string
        String base64Image = getItem(position);

        // Get the views from the layout
        ImageView imageView = convertView.findViewById(R.id.image);
        AppCompatButton removeImageButton = convertView.findViewById(R.id.remove_image_button);

        // Decode the Base64 string into a Bitmap and set it to the ImageView
        Bitmap bitmap = decodeBase64Image(base64Image);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

        // Set up the remove button to delete the Firestore document by its ID
        removeImageButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docId = documentIds.get(position); // Get the document ID for the selected item

            db.collection("images").document(docId).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show();
                        imageList.remove(position); // Remove the image from the local list
                        documentIds.remove(position); // Remove the corresponding document ID
                        notifyDataSetChanged(); // Refresh the adapter
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove image", Toast.LENGTH_SHORT).show());
        });

        return convertView;
    }

    /**
     * Decodes a Base64-encoded image string into a Bitmap.
     *
     * @param base64Image The Base64 string representing the image.
     * @return A Bitmap representation of the image, or null if decoding fails.
     */
    private Bitmap decodeBase64Image(String base64Image) {
        try {
            byte[] decodedBytes = Base64.decode(base64Image.trim(), Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("Decode Image", "Invalid Base64 string", e);
            return null;
        }
    }
}

