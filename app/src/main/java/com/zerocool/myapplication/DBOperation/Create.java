package com.zerocool.myapplication.DBOperation;

import android.text.TextUtils;
import android.widget.Toast;

import com.crashoverride.orange2.Artist;
import com.crashoverride.orange2.Models.ModelCreate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Technocrats on 02-Apr-18.
 */

public class Create {

    public void createUserProfile( ModelCreate modelCreate) {

        DatabaseReference create = FirebaseDatabase.getInstance().getReference("UserProfile");
        String id = create.push().getKey();
        create.child(id).setValue(modelCreate);

    }

}
