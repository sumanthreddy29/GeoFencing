package com.gsu.geofencing;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class AdminActivity extends AppCompatActivity {


    Button add,logout;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        add = (Button) findViewById(R.id.add);

        logout = (Button)findViewById(R.id.logout);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(AdminActivity.this,AddEvents.class);
            startActivity(i);

            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                // Toast.makeText(MainActivity.this,"clicked" , Toast.LENGTH_LONG).show();
                mAuth.signOut();
                Intent login = new Intent(AdminActivity.this,LoginActivity.class);
                startActivity(login);

            }
        });
    }
}
