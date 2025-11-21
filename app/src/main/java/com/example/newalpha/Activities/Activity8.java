package com.example.newalpha.Activities;

import static android.content.ContentValues.TAG;
import static com.example.newalpha.GeminiRelevant.Prompts.GET_DATA_FROM_SITE;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;

import com.example.newalpha.GeminiRelevant.GeminiCallback;
import com.example.newalpha.GeminiRelevant.GeminiManager;
import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;

public class Activity8 extends MasterActivity
{
    final String DEFAULT_URL = "https://www.homecenter.co.il/products/1733864416038?variant=49286713049335&country=IL&currency=ILS&utm_medium=product_sync&utm_source=google&utm_content=sag_organic&utm_campaign=sag_organic&gad_source=1&gad_campaignid=23166357270&gbraid=0AAAAADmrDV_cwB1SY3OIKNDqODl52t7ZG&gclid=CjwKCAiAuIDJBhBoEiwAxhgyFpZvTxDUS6jAKX8Vh7ZyEGsPTYY2QTBQirNG0jtpdVYa8IIAud_VKxoCjlcQAvD_BwE";
    GeminiManager geminiManager;
    EditText urlEd;
    TextView responseTV;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_8);

        urlEd = findViewById(R.id.URL_ED);
        responseTV = findViewById(R.id.responseTV);


        geminiManager = GeminiManager.getInstance();

    }

    public void searchByURL(View view)
    {
        String url = urlEd.getText().toString();
        search(url);
    }

    public void searchByDefaultURL(View view)
    {
        search(DEFAULT_URL);

    }
    public void search(String url)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading...");
        pd.show();

        String prompt = GET_DATA_FROM_SITE + "from this site: " + url;
        geminiManager.sendTextPrompt(prompt, new GeminiCallback() {
            @Override
            public void onSuccess(String result)
            {
                pd.dismiss();
                responseTV.setText(result);
            }

            @Override
            public void onFailure(Throwable error)
            {
                pd.dismiss();
                responseTV.setText("Erorr:" + error.getMessage());
                Log.e(TAG, "onActivityResult/Error: " + error.getMessage());
            }
        });
    }
}