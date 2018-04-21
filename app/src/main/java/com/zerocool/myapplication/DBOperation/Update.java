package com.zerocool.myapplication.DBOperation;

import android.widget.Toast;

import com.crashoverride.orange2.Artist;
import com.crashoverride.orange2.Models.ModelUpdate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Technocrats on 02-Apr-18.
 */

public class Update {
    public static boolean updateProfile(ModelUpdate modelUpdate) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("OrangeDB").child(modelUpdate.getID());
//        Artist artist = new Artist(id, name, genre);
        dR.setValue(modelUpdate);
        return true;
    }
}
