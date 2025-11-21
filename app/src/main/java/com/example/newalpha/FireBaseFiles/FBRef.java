package com.example.newalpha.FireBaseFiles;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBRef
{
    public static FirebaseDatabase FBDB = FirebaseDatabase.getInstance();
    public static DatabaseReference userRef = FBDB.getReference("User");
    public static DatabaseReference keyRef = FBDB.getReference("Key");
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
}

