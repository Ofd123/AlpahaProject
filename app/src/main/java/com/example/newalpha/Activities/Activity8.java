package com.example.newalpha.Activities;

import static android.content.ContentValues.TAG;
import static com.example.newalpha.GeminiRelevant.Prompts.GET_DATA_FROM_FILE;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class Activity8 extends MasterActivity
{
    final String DEFAULT_URL = "[https://www.homecenter.co.il/products/1733864416038?variant=49286713049335&country=IL&currency=ILS&utm_medium=product_sync&utm_source=google&utm_content=sag_organic&utm_campaign=sag_organic&gad_source=1&gad_campaignid=23166357270&gbraid=0AAAAADmrDV_cwB1SY3OIKNDqODl52t7ZG&gclid=CjwKCAiAuIDJBhBoEiwAxhgyFpZvTxDUS6jAKX8Vh7ZyEGsPTYY2QTBQirNG0jtpdVYa8IIAud_VKxoCjlcQAvD_BwE]";
//    final String DEFAULT_URL = "https://ksp.co.il/web/item/414674";
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
//    public void search(String urlStr) {
//        ProgressDialog pd = new ProgressDialog(this);
//        pd.setTitle("Downloading and Analyzing...");
//        pd.setCancelable(false);
//        pd.show();
////
////        // Create and start a new background thread for network and file operations
//        new Thread(() -> {
//            try
//            {
////                // --- Background Thread Operations ---
//                Log.d(TAG, "Opening connection on background thread...");
//                URL url = new URL(urlStr);
////
//                InputStream in = url.openStream();
////
//                java.io.ByteArrayOutputStream byteBuffer = new java.io.ByteArrayOutputStream();
//                int nRead;
//                byte[] data = new byte[1024];
//                while ((nRead = in.read(data, 0, data.length)) != -1)
//                {
//                    byteBuffer.write(data, 0, nRead);
//                }
//                byteBuffer.flush();
//                byte[] fileBytes = byteBuffer.toByteArray();
//                in.close();
//                Log.d(TAG, "File downloaded, size: " + fileBytes.length + " bytes");
//
////                // --- Send to Gemini ---
//                String prompt = GET_DATA_FROM_FILE;
//                String mimeType = "application/pdf"; //    (The KSP link returns HTML, not a PDF).
//                geminiManager.sendTextWithFilePrompt(prompt, fileBytes, mimeType, new GeminiCallback() {
//                    @Override
//                    public void onSuccess(String result)
//                    {
////                        // Switch back to the main thread to update the UI
//                        runOnUiThread(() -> {
//                            pd.dismiss();
//                            responseTV.setText(result);
//                        });
//                    }
//
//                    @Override
//                    public void onFailure(Throwable error)
//                    {
//                        runOnUiThread(() -> {
//                            pd.dismiss();
//                            responseTV.setText("Gemini Error: " + error.getMessage());
//                            Log.e(TAG, "Gemini Failure: " + error.getMessage());
//                        });
//                    }
//                });
//
//            }
//            catch (IOException e)
//            {
//                Log.e(TAG, "Network or File Error", e);
//                // Switch back to the main thread to update UI with the error
//                runOnUiThread(() -> {
//                    pd.dismiss();
//                    responseTV.setText("Error downloading data: " + e.getMessage());
//                });
//            }
//        }).start(); // start the thread!
//    }
// IMPORTANT: Network operations MUST be run off the main thread.
// We will wrap the Jsoup code in a new Thread.
public void search(String url) {
    ProgressDialog pd = new ProgressDialog(this);
    pd.setTitle("Downloading and Analyzing...");
    pd.setCancelable(false);
    pd.show();

    new Thread(() -> {
        try {
            // Clean URL
            String cleanUrl = url.replace("[", "").replace("]", "");

            // --- 1. Fetch the page with Jsoup ---
            Log.d(TAG, "Fetching URL: " + cleanUrl);
            Document doc = Jsoup.connect(cleanUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept-Language", "he-IL,he;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Connection", "keep-alive")
                    .timeout(15000)
                    .get();

            // --- 2. Extract the body HTML ---
            String pageHtml = doc.body().html();
            Log.d(TAG, "HTML length: " + pageHtml.length());

            // --- 3. Inject HTML into Gemini prompt ---
            String finalPrompt = String.format(GET_DATA_FROM_SITE, pageHtml);

            // --- 4. Send to Gemini ---
            geminiManager.sendTextPrompt(finalPrompt, new GeminiCallback() {
                @Override
                public void onSuccess(String result) {
                    runOnUiThread(() -> {
                        pd.dismiss();
                        responseTV.setText(result);
                    });
                }

                @Override
                public void onFailure(Throwable error) {
                    runOnUiThread(() -> {
                        pd.dismiss();
                        responseTV.setText("Gemini Error: " + error.getMessage());
                        Log.e(TAG, "Gemini Failure", error);
                    });
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Network/Jsoup Error", e);
            runOnUiThread(() -> {
                pd.dismiss();
                responseTV.setText("Error accessing site: " + e.getMessage());
            });
        }
    }).start();
}
}

