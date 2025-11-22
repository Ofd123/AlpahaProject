package com.example.newalpha.Activities;

import static com.example.newalpha.FireBaseFiles.FBRef.refStorage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class Activity4 extends MasterActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CAMERA_PERMISSION_CODE = 200;

    private ImageView imageView;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        imageView = findViewById(R.id.photoImageView);
    }

    /* -------------------------------------------------------
     *  Upload button clicked → check permission first
     * ------------------------------------------------------- */
    public void upload(android.view.View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            return;
        }
        openCamera();
    }

    /* -------------------------------------------------------
     *  Camera Launcher
     * ------------------------------------------------------- */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No camera app found.", Toast.LENGTH_SHORT).show();
        }
    }

    /* -------------------------------------------------------
     *  Handle permission result
     * ------------------------------------------------------- */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* -------------------------------------------------------
     *  Handle result from camera
     * ------------------------------------------------------- */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Bitmap imageBitmap = (Bitmap) (data.getExtras() != null ? data.getExtras().get("data") : null);

            if (imageBitmap == null) {
                Toast.makeText(this, "Failed to retrieve image from camera.", Toast.LENGTH_SHORT).show();
                return;
            }

//            imageView.setImageBitmap(imageBitmap);

            // Convert bitmap to bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Unique filename
            fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference fileRef = refStorage.child("images/" + fileName);

            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

            fileRef.putBytes(imageData)
                    .addOnSuccessListener(taskSnapshot ->
                            Toast.makeText(Activity4.this, "Image Uploaded", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e ->
                            Toast.makeText(Activity4.this, "Upload Failed" + e.getMessage(), Toast.LENGTH_SHORT).show()
                    )
                    .addOnProgressListener(snapshot -> {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        // Optional: Update UI
                    });
        }
    }

    /* -------------------------------------------------------
     *  Download button clicked
     * ------------------------------------------------------- */
    public void download(android.view.View view) {
        if (fileName == null) {
            Toast.makeText(this, "Upload an image first.", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileRef = refStorage.child("images/" + fileName);
        final long FIVE_MB = 5 * 1024 * 1024;

        Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();

        fileRef.getBytes(FIVE_MB)
                .addOnSuccessListener(bytes -> {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(Activity4.this, "Download Failed", Toast.LENGTH_SHORT).show()
                );
    }
}
