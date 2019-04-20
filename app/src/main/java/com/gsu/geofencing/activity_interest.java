package com.gsu.geofencing;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_interest extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CheckBox music,games,arts,sports,films,food,books;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        music=(CheckBox)findViewById(R.id.music);
        games=(CheckBox)findViewById(R.id.games);
        arts=(CheckBox)findViewById(R.id.arts);
        save=(Button)findViewById(R.id.save);
        sports=(CheckBox)findViewById(R.id.sports);
        films=(CheckBox)findViewById(R.id.Film);
        food=(CheckBox)findViewById(R.id.food);
        books=(CheckBox)findViewById(R.id.Books);
        Intent intent = getIntent();

        String user_name = intent.getStringExtra("email");
        DocumentReference docRef = db.collection("users").document(user_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String  response=document.get("interests").toString();


                        response=response.substring(1,response.length()-1);
                        String str[] = response.split(",");
                        List<String> al = new ArrayList<String>();
                        al = Arrays.asList(str);
                        for(String s: al){
                          String  interest=s.trim().toString();


                            if(interest.equals("Music"))
                            {
                                music.setChecked(true);
                            }
                            if(interest.equals("Arts"))
                            {
                                arts.setChecked(true);
                            }
                            if(interest.equals("Games"))
                            {
                                games.setChecked(true);
                            }
                            if(interest.equals("Books"))
                            {
                                books.setChecked(true);
                            }
                            if(interest.equals("Sports"))
                            {
                                sports.setChecked(true);
                            }
                            if(interest.equals("Film"))
                            {
                                films.setChecked(true);
                            }
                            if(interest.equals("Food"))
                            {
                                food.setChecked(true);
                            }
                        }



                    } else {
                        Log.d("message", "No such document");
                    }
                } else {
                    Log.d("message", "get failed with ", task.getException());
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> interests = new ArrayList<>();
                Intent intent = getIntent();

                String user_name = intent.getStringExtra("email");

                if (music.isChecked()) {
                    interests.add(music.getText().toString());
                }
                if (games.isChecked())
                    interests.add(games.getText().toString());

                if (arts.isChecked())
                    interests.add(arts.getText().toString());
                if (sports.isChecked())
                    interests.add(sports.getText().toString());
                if (films.isChecked())
                    interests.add(films.getText().toString());
                if (food.isChecked())
                    interests.add(food.getText().toString());
                if (books.isChecked())
                    interests.add(books.getText().toString());


                Map<String, Object> user = new HashMap<>();

                user.put("interests", interests);


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
