package com.example.newalpha.Activities;

import static android.content.ContentValues.TAG;

import static com.example.newalpha.FireBaseFiles.FBRef.refStorage;
import static com.example.newalpha.GeminiRelevant.Prompts.GET_DATA_FROM_IMAGE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.example.newalpha.GeminiRelevant.GeminiCallback;
import com.example.newalpha.GeminiRelevant.GeminiManager;
import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.firebase.storage.StorageReference;

public class Activity7 extends MasterActivity
{
    Bitmap imageBitmap;
    final int CAMERA_REQUEST_CODE = 1;
    static final int REQUEST_FULL_IMAGE_CAPTURE = 2;
    GeminiManager geminiManager;
    TextView dataTV;
    ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_7);

        geminiManager = GeminiManager.getInstance();
        dataTV = findViewById(R.id.dataTV);
        imageButton = findViewById(R.id.imageButton);




    }
    public void takeAPic(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode == RESULT_OK) && (requestCode == REQUEST_FULL_IMAGE_CAPTURE))
        {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Uploading...");
            pd.show();
            Uri imageUri = data.getData();


            imageBitmap = BitmapFactory.decodeFile(imageUri.getPath());
            imageButton.setImageBitmap(imageBitmap);
            String prompt = GET_DATA_FROM_IMAGE;
            geminiManager.sendTextWithPhotoPrompt(prompt, imageBitmap, new GeminiCallback() {
                @Override
                public void onSuccess(String result)
                {
                    pd.dismiss();
                    dataTV.setText(result);
                }
                @Override
                public void onFailure(Throwable error)
                {
                    pd.dismiss();
                    dataTV.setText("Erorr:" + error.getMessage());
                    Log.e(TAG, "onActivityResult/Error: " + error.getMessage());
                }
            });
        }
    }



}
