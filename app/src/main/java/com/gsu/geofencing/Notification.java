package com.gsu.geofencing;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private ListView mListView;
    private List<String> eventList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mListView = (ListView) findViewById(R.id.list_view);
        Intent intent = getIntent();

        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);
                Intent i = new Intent(Notification.this,MapActivity.class);

                startActivity(i);
            }
        });


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

                                String eventDetails= "Eventdate:"+document.get("eventdate").toString()+
                                        "EventTime:"+document.get("eventStartTime").toString()+"-"+document.get("eventEndTime").toString()+"";
                                if(document.get("eventCategory").toString().equals(interest))
                                {
                                    passEvent(interest,eventDetails);
                                    Log.d("events", document.getId() + " => " + document.getData());
                                    eventList.add("EventName:"+document.getId()+" "+"Eventdate:"+document.get("eventdate").toString()+"\n"+
                                    "EventTime:"+document.get("eventStartTime").toString()+"-"+document.get("eventEndTime").toString()+"");
                                }



                            }
                            ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_selectable_list_item,eventList);
                            adapter.notifyDataSetChanged();
                            mListView.setAdapter(adapter);




                        }
                    }
                });

    }


    public void passEvent(String event,String details)
    {
        Intent i = getIntent();

        Log.e("interest",event);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }


        MyNotificationManager.getInstance(this).displayNotification("Event: "+event, "Details: "+details);

    }





}
