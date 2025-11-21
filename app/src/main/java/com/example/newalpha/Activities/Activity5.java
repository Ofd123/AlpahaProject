package com.example.newalpha.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;

public class Activity5 extends MasterActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_5);
    }

    public void activateBroadcast(View view)
    {
        if(!chargeListen)
        {
            continueListen();
        }
    }

    public void deactivateBroadcast(View view)
    {
        if(chargeListen)
        {
            stopListen();
        }
    }
}
