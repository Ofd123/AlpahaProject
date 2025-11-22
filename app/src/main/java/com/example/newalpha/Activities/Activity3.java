package com.example.newalpha.Activities;

//import static com.example.newalpha.FireBaseFiles.FBRef.GalleryImageRef;
import static com.example.newalpha.FireBaseFiles.FBRef.refStorage;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Query;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.UUID;

public class Activity3 extends MasterActivity
{
    private static final int REQUEST_PICK_IMAGE = 300;
    ImageView imageView;
    String fileName;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_3);

        imageView = findViewById(R.id.imageView);
    }

    public void upload(View view)
    {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("Error", e.toString());
        }
    }

    public void download(View view)
    {
        if (fileName == null || fileName.isEmpty())
        {
            Toast.makeText(this, "Please upload an image first.", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference refFile = refStorage.child("images/" + fileName);
        final long MAX_SIZE = 1024 * 1024;
        refFile.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(Activity3.this, "Image Not Downloaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null)
        {
            try{
                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);

                uploadImage(imageUri);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.i("Error", e.toString());
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }


    }
    private void uploadImage(Uri imageURI)
    {
        if(imageURI != null)
        {
            fileName = UUID.randomUUID().toString() + ".jpg";
            StorageReference refFile = refStorage.child("images/" + fileName);

            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading");
            pd.show();

            refFile.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    pd.dismiss();
                    Toast.makeText(Activity3.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(Activity3.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Uploaded " + (int) progress + "%");
                }
            });
        }
    }

}
