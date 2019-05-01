package com.gsu.geofencing;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Spinner spinner;
    LocationManager locationManager;
    private FirebaseAuth mAuth;
    int m;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String email;
        showNotifications();
         m = random.nextInt(9999 - 1000) + 1000;
        m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        System.out.println(m);

        if (user != null) {

             email = user.getEmail();
             if(email.equals("admin@gmail.com"))
             {
                 Intent i = new Intent(MainActivity.this,AdminActivity.class);
                 startActivity(i);
             }

        }else {
            Intent intent = getIntent();
             email = intent.getStringExtra("email");

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView email_id;
        View navHeaderView;
        navHeaderView= navigationView.getHeaderView(0);
        email_id= (TextView) navHeaderView.findViewById(R.id.email_id);
        email_id.setText(email);

        Button getEvents;
        getEvents=(Button)findViewById(R.id.getEventsNearMe);

        getEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,MapActivity.class);
                startActivity(i);

            }
        });
            }

    private void showNotifications() {


       String   email = user.getEmail();


        DocumentReference docRef = db.collection("users").document(email);
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
                            getEvents(interest,m);
                            m+=1;

                        }

                    }
                }
            }
        });



    }

    public void getEvents(final String interest, final int m)
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
                                    passEvent(interest,eventDetails,m);

                                }



                            }





                        }
                    }
                });

    }

    public void passEvent(String event,String details,int m)
    {


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

    System.out.println("test"+m);

        MyNotificationManager.getInstance(this).displayNotification("Event: "+event, "Details: "+details,m);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView email_id;
        View navHeaderView;
        navHeaderView= navigationView.getHeaderView(0);
        email_id= (TextView) navHeaderView.findViewById(R.id.email_id);


        if (id == R.id.nav_interest) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this,activity_interest.class);
            i.putExtra("email",email_id.getText().toString());
            startActivity(i);
        } else if (id == R.id.nav_notification) {
            Intent i = new Intent(MainActivity.this,Notification.class);
            i.putExtra("email",email_id.getText().toString());
            startActivity(i);

        }  else if (id == R.id.logout) {
            mAuth = FirebaseAuth.getInstance();
               // Toast.makeText(MainActivity.this,"clicked" , Toast.LENGTH_LONG).show();
                mAuth.signOut();
                Intent login = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(login);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
