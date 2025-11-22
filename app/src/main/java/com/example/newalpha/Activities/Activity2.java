package com.example.newalpha.Activities;

import static com.example.newalpha.FireBaseFiles.FBRef.keyRef;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.FireBaseFiles.KeyAndVal;
import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity2 extends MasterActivity
{
    ListView itemsListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> strs = new ArrayList<String>();
    EditText valEt,keyEt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);

        itemsListView = findViewById(R.id.itemsListView);
        valEt = findViewById(R.id.valEt);
        keyEt = findViewById(R.id.keyEt);

        adapter = new ArrayAdapter<String>(Activity2.this, android.R.layout.simple_spinner_dropdown_item, strs);
        itemsListView.setAdapter(adapter);

        readDataFromFB();
    }
    public void readDataFromFB()
    {
        strs = new ArrayList<String>();
        ArrayList<KeyAndVal> values = new ArrayList<KeyAndVal>();

        keyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {

                    KeyAndVal val = ds.getValue(KeyAndVal.class);
                    String str = val.getKey() + " : " + val.getVal() + "\n" + val.getID();
                    strs.add(str);
                    values.add(val);
                }

                //add the values to the listview
                adapter = new ArrayAdapter<String>(Activity2.this, android.R.layout.simple_spinner_dropdown_item, strs);
                itemsListView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                strs.clear();
                values.clear();
            }
        });
    }

    public void submitBtn(View view)
    {

        String key = keyEt.getText().toString();
        String val = valEt.getText().toString();
        if(key == "" || val == "")
        {
            Toast.makeText(this,"Please Enter Key and Value", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            //put the key and the value in the realtime database
            KeyAndVal keyAndValue = new KeyAndVal(key,val);
            String id = keyRef.push().getKey();
            keyAndValue.setID(id);
            keyRef.child(keyAndValue.getID()).setValue(keyAndValue);

            //read the data from the realtime database
            readDataFromFB();

        }
        keyEt.setText("");
        valEt.setText("");
    }
}