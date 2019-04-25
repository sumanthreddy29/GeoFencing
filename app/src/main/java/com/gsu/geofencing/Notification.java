package com.gsu.geofencing;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Notification  extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent intent = getIntent();

        final String user_name = intent.getStringExtra("email");

        DocumentReference docRef = db.collection("users").document(user_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String response = document.get("interests").toString();


                        response = response.substring(1, response.length() - 1);
                        String str[] = response.split(",");
                        List<String> al = new ArrayList<String>();
                        al = Arrays.asList(str);
                        for (String s : al) {
                            String interest = s.trim().toString();
                            getEvents(interest);
                        }

                    }
                }
            }
        });
    }
    public void getEvents(final String interest)
    {
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : task.getResult()) {
                              //  Log.d("events", document.getId() + " => " + document.getData());

                                String eventDetails= "Eventdate:"+document.get("eventdate").toString()+"\n"+
                                        "EventTime:"+document.get("eventStartTime").toString()+"-"+document.get("eventEndTime").toString()+"\n";
                                if(document.get("eventCategory").toString().equals(interest))
                                {
                                    passEvent(interest,eventDetails);
                                    Log.d("events", document.getId() + " => " + document.getData());
                                }



                            }



                        }
                    }
                });

    }


    public void passEvent(String event,String details)
    {
        Intent i = getIntent();

        Log.e("interest",event);


        MyNotificationManager.getInstance(this).displayNotification("Event: "+event, "Details: "+details);

    }






}
