package com.zerocool.myapplication.DBOperation;

import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Technocrats on 02-Apr-18.
 */

public class Delete {

    private boolean deleteAItem(String id) {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("OrangeDB").child(id);
        dR.removeValue();
//        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();
        return true;
    }
}
