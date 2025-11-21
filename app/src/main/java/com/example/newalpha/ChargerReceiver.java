package com.example.newalpha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ChargerReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        if(Intent.ACTION_POWER_CONNECTED.equals(action))
            Toast.makeText(context, "Charger connected", Toast.LENGTH_SHORT).show();
        else if(Intent.ACTION_POWER_DISCONNECTED.equals(action))
            Toast.makeText(context, "Charger Disconnected", Toast.LENGTH_SHORT).show();
    }
}