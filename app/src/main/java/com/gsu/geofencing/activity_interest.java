package com.gsu.geofencing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class activity_interest extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CheckBox music,games,arts;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        music=(CheckBox)findViewById(R.id.music);
        games=(CheckBox)findViewById(R.id.games);
        arts=(CheckBox)findViewById(R.id.arts);
        save=(Button)findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> interests = new ArrayList<>();


                if (music.isChecked()) {
                    interests.add(music.getText().toString());
                }
                if (games.isChecked())
                    interests.add(games.getText().toString());

                if (arts.isChecked())
                    interests.add(arts.getText().toString());

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();

                user.put("interests", interests);

                Intent intent = getIntent();

                 String user_name = intent.getStringExtra("email");
                db.collection("users").document(user_name)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_LONG).show();//display the text of button1
                                Intent i = new Intent(activity_interest.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "data not saved", Toast.LENGTH_LONG).show();
                            }
                        });


            }
        });
    }
}
