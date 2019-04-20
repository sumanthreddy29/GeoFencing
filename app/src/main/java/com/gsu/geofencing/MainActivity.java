package com.gsu.geofencing;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String email;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            Toast.makeText(MainActivity.this, email, Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this, "need to add map activity", Toast.LENGTH_LONG).show();
            }
        });
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

        Toast.makeText(MainActivity.this, email_id.getText().toString() , Toast.LENGTH_LONG).show();
        if (id == R.id.nav_interest) {
            // Handle the camera action
            Intent i = new Intent(MainActivity.this,activity_interest.class);
            i.putExtra("email",email_id.getText().toString());
            startActivity(i);
        } else if (id == R.id.nav_notification) {
            Intent i = new Intent(MainActivity.this,Notification.class);
            //i.putExtra("email",email);
            startActivity(i);

        } else if (id == R.id.nav_places) {
            Intent i = new Intent(MainActivity.this,PlacesVisited.class);
            //i.putExtra("email",email);
            startActivity(i);

        } else if (id == R.id.logout) {
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
