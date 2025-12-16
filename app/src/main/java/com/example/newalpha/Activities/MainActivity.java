package com.example.newalpha.Activities;

import static com.example.newalpha.FireBaseFiles.FBRef.refAuth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newalpha.MasterActivity;
import com.example.newalpha.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends MasterActivity
{

    EditText passwordED, mailED;
    TextView resultsTV;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        passwordED = findViewById(R.id.passwordED);
        mailED = findViewById(R.id.mailED);
        resultsTV = findViewById(R.id.resultsTV);

    }

    public void submit(View view)
    {
        String password = passwordED.getText().toString();
        String mail = mailED.getText().toString();
        if (password == "" || mail == "")
        {
            Toast.makeText(MainActivity.this, "please enter valid data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("creating user");
            progressDialog.setMessage("please wait...");
            progressDialog.show();

            refAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    progressDialog.dismiss();
                    if (task.isSuccessful())
                    {
                        Log.i("MainActivity", "createUserWithEmailAndPassword:success");
                        FirebaseUser user = refAuth.getCurrentUser();
                        String text = "";
                        text += "user id: " + user.getUid() + "\n";
                        text += "email: " + user.getEmail() + "\n";
                        text += "display name: " + user.getDisplayName() + "\n";
                        resultsTV.setText(text);
                    }
                    else
                    {
                        Exception exp = task.getException();
                        if (exp instanceof FirebaseAuthInvalidUserException)
                        {
                            resultsTV.setText("Invalid email address.");
                        }
                        else if (exp instanceof FirebaseAuthWeakPasswordException)
                        {
                            resultsTV.setText("Password too weak.");
                        }
                        else if (exp instanceof FirebaseAuthUserCollisionException)
                        {
                            resultsTV.setText("User already exists.");
                        }
                        else if (exp instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            resultsTV.setText("General authentication failure.\n" + exp.getMessage());
                        }
                        else if (exp instanceof FirebaseNetworkException)
                        {
                            resultsTV.setText("Network error. Please check your connection.");
                        }
                        else
                        {
                            resultsTV.setText("An error occurred. Please try again later.");
                        }
                    }
                }
            });
        }
    }
}