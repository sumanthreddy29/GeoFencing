package com.gsu.geofencing;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class Notification  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        FirebaseMessaging.getInstance().subscribeToTopic("Music");
        FirebaseMessaging.getInstance().subscribeToTopic("Games");
        FirebaseMessaging.getInstance().subscribeToTopic("Arts");
        FirebaseMessaging.getInstance().subscribeToTopic("Sports");
        FirebaseMessaging.getInstance().subscribeToTopic("Film");
        FirebaseMessaging.getInstance().subscribeToTopic("Food");
        FirebaseMessaging.getInstance().subscribeToTopic("Books");
        FirebaseMessaging.getInstance().subscribeToTopic("Test");

    }
}
