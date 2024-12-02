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
    private ArrayList<String> image_list;

    /**
     * Constructor to initialize the adapter.
     *
     * @param context The current context.
     * @param images  A list of Base64-encoded image strings.
     */
    public AdminImageArrayAdapter(@NonNull Context context, ArrayList<String> images) {
        super(context, R.layout.admin_image_list_item, images);
        this.context = context;
        this.image_list = images;
    }

    /**
     * Populates a ListView item with image data and a button for removing the image.
     *
     * @param position    The position of the item in the list.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
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

        // Set up the remove button to delete the image from Firestore and update the list
        String finalBase64Image = base64Image;
        removeImageButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Log.d("RemoveImage", "Base64 Length: " + finalBase64Image.length());
            Log.d("RemoveImage", "Base64: " + finalBase64Image);
            db.collection("images").whereEqualTo("imageData", finalBase64Image).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Get the document ID of the matching image
                            String docId = task.getResult().getDocuments().get(0).getId();
                            db.collection("images").document(docId).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show();
                                        image_list.remove(position); // Remove image from local list
                                        notifyDataSetChanged(); // Refresh the adapter
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove image", Toast.LENGTH_SHORT).show());
                        } else {
                            Log.d("RemoveImage", "No matching image found.");
                        }
                    });
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
            // Trim unwanted whitespace or newlines before decoding
            String base64ImageWithoutNewlines = base64Image.trim();

            // Decode the Base64 string to a byte array
            byte[] decodedBytes = Base64.decode(base64ImageWithoutNewlines, Base64.DEFAULT);

            // Decode the byte array into a Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("Decode Image", "Invalid Base64 string", e);
            return null;
        }
    }
}
