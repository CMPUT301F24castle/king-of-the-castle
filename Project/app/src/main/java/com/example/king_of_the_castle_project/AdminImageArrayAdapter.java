package com.example.king_of_the_castle_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminImageArrayAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> image_list;

    public AdminImageArrayAdapter(@NonNull Context context, ArrayList<String> images) {
        super(context, R.layout.admin_image_list_item, images);
        this.context = context;
        this.image_list = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_image_list_item, parent, false);
        }

        String base64Image = getItem(position);

        // get views
        ImageView imageView = convertView.findViewById(R.id.image);
        AppCompatButton removeImageButton = convertView.findViewById(R.id.remove_image_button);

        Bitmap bitmap = decodeBase64Image(base64Image);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }


        String finalBase64Image = base64Image;
        removeImageButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Log.d("RemoveImage", "Base64 Length: " + finalBase64Image.length());
            Log.d("RemoveImage", "Base64: " + finalBase64Image);
            db.collection("images").whereEqualTo("imageData", finalBase64Image).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("RemoveImage", "Found document: " + task.getResult().getDocuments().get(0).getId());
                            String docId = task.getResult().getDocuments().get(0).getId();
                            db.collection("images").document(docId).delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show();
                                        image_list.remove(position); // Remove from local list
                                        notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to remove image", Toast.LENGTH_SHORT).show());
                        }
                        else {
                            Log.d("RemoveImage", "No matching image found.");
                        }
                    });
        });

        return convertView;
    }


    private Bitmap decodeBase64Image(String base64Image) {
        try {
            // Trim any unwanted whitespace or newlines before decoding
            String base64ImageWithoutNewlines = base64Image.trim();

            // Decode the Base64 string to get the byte array
            byte[] decodedBytes = Base64.decode(base64ImageWithoutNewlines, Base64.DEFAULT);

            // After decoding, if you need to append a space for some reason, do it here
            // (Only if the space is needed for some use case, but it should not affect the image decoding)
            // decodedBytes = appendSpace(decodedBytes);

            // Decode the byte array into a Bitmap
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("Decode Image", "Invalid Base64 string", e);
            return null;
        }
    }

    private byte[] appendSpace(byte[] decodedBytes) {
        // Example of appending space to byte array (not generally needed for image decoding)
        byte[] newBytes = new byte[decodedBytes.length + 1];
        System.arraycopy(decodedBytes, 0, newBytes, 0, decodedBytes.length);
        return newBytes;
    }

}
