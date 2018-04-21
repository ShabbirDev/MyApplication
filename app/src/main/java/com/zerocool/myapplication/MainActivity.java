package com.zerocool.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crashoverride.orange2.DBOperation.Update;
import com.crashoverride.orange2.Models.ModelCreate;
import com.crashoverride.orange2.Models.ModelDelete;
import com.crashoverride.orange2.Models.ModelRead;
import com.crashoverride.orange2.Models.ModelUpdate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";


    RecyclerView RecyclerView;
    ImageView ProfilePhoto, ChangePhoto;
    TextView MyName, MyNumber;
    ImageButton EditMyName, EditMyNumber;

    ModelCreate modelCreate = new ModelCreate();
    ModelRead modelRead = new ModelRead();
    ModelUpdate modelUpdate = new ModelUpdate();
    ModelDelete modelDelete = new ModelDelete();


    //a list to store all the artist from firebase database
    List<Artist> artists;

    //our database reference object
    DatabaseReference databaseArtists;
    EditText editTextName;
    Spinner spinnerGenre;
    RecyclerView RecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting the reference of artists node
        databaseArtists = FirebaseDatabase.getInstance().getReference("artists");
        InitializeViews();

////        //getting views
//        editTextName = (EditText) findViewById(R.id.editTextName);
//        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenres);
////        RecyclerView =  findViewById(R.id.RecyclerView);
////
//////        buttonAddArtist = (Button) findViewById(R.id.buttonAddArtist);
////
//        //list to store artists
//        artists = new ArrayList<>();
//
//
//        //adding an onclicklistener to button
//        buttonAddArtist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //calling the method addArtist()
//                //the method is defined below
//                //this method is actually performing the write operation
//                addArtist();
//            }
//        });
//
//        //attaching listener to listview
//        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //getting the selected artist
//                Artist artist = artists.get(i);
//
//                //creating an intent
//                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);
//
//                //putting artist name and id to intent
//                intent.putExtra(ARTIST_ID, artist.getArtistId());
//                intent.putExtra(ARTIST_NAME, artist.getArtistName());
//
//                //starting the activity with intent
//                startActivity(intent);
//            }
//        });
//
//        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Artist artist = artists.get(i);
//                showUpdateDeleteDialog(artist.getArtistId(), artist.getArtistName());
//                return true;
//            }
//        });


    }

    private void showUpdateDeleteDialog(final String artistId, String artistName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_dialog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText UpdateField = (EditText) dialogView.findViewById(R.id.UpdateField);
        final Button SaveEdit = (Button) dialogView.findViewById(R.id.SaveEdit);

        dialogBuilder.setTitle(artistName);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        SaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = UpdateField.getText().toString().trim();
                if (!TextUtils.isEmpty(name))
                {
                    modelUpdate.setID(artistId);
                    modelUpdate.setMyPhoto("");
                    modelUpdate.setMyName("");
                    modelUpdate.setMyNumber("");

                    Update.updateProfile(modelUpdate);
//                    updateArtist(modelUpdate);
                    alertDialog.dismiss();
                }
            }
        });


//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                deleteArtist(artistId);
//                alertDialog.dismiss();
//            }
//        });
    }

//    private boolean updateArtist(ModelUpdate modelUpdate) {
//        //getting the specified artist reference
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(modelUpdate.getID());
//
//        //updating artist
////        Artist artist = new Artist(id, name, genre);
//        dR.setValue(modelUpdate);
//        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
//        return true;
//    }



//    private boolean updateArtist(String id, String name, String genre) {
//        //getting the specified artist reference
//        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);
//
//        //updating artist
//        Artist artist = new Artist(id, name, genre);
//        dR.setValue(artist);
//        Toast.makeText(getApplicationContext(), "Artist Updated", Toast.LENGTH_LONG).show();
//        return true;
//    }

    private boolean deleteArtist(String id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("artists").child(id);

        //removing artist
        dR.removeValue();

        //getting the tracks reference for the specified artist
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        //removing all tracks
        drTracks.removeValue();
        Toast.makeText(getApplicationContext(), "Artist Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseArtists.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list
                    artists.add(artist);
                }

                //creating adapter
                ArtistList artistAdapter = new ArtistList(MainActivity.this, artists);
                //attaching adapter to the listview
                RecyclerView.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /*
    * This method is saving a new artist to the
    * Firebase Realtime Database
    * */
    private void addArtist() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();


        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will modelCreate a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            ModelCreate modelCreate = new ModelCreate();
            modelCreate.setID(id);
            modelCreate.setMyPhoto("");
            modelCreate.setMyName("");
            modelCreate.setMyNumber("");

            //creating an Artist Object
            Artist artist = new Artist(id, name, genre);

            //Saving the Artist
//            databaseArtists.child(id).setValue(artist);
            databaseArtists.child(id).setValue(modelCreate);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    private void addArtist2() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String genre = spinnerGenre.getSelectedItem().toString();

        String Input =

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseArtists.push().getKey();

            //creating an Artist Object
            Artist artist = new Artist(id, name, genre);

            //Saving the Artist
            databaseArtists.child(id).setValue(artist);

            //setting edittext to blank again
            editTextName.setText("");

            //displaying a success toast
            Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }


    private void InitializeViews() {
        RecyclerView = findViewById(R.id.RecyclerView);
        EditMyNumber = findViewById(R.id.EditMyNumber);
        MyNumber = findViewById(R.id.MyNumber);
        EditMyName = findViewById(R.id.EditMyName);
        MyName = findViewById(R.id.MyName);
        ChangePhoto = findViewById(R.id.ChangePhoto);
        ProfilePhoto = findViewById(R.id.ProfilePhoto);
    }


}
