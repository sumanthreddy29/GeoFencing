package com.gsu.geofencing;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AdminActivity extends AppCompatActivity {


    Button add,delete,logout;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String DATE_FORMAT_1 = "dd-MM-yyyy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        add = (Button) findViewById(R.id.add);
        delete=(Button)findViewById(R.id.delete);
        logout = (Button)findViewById(R.id.logout);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(AdminActivity.this,AddEvents.class);
            startActivity(i);

            }
        });



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                db.collection("events")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {


                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        try {
                                            Date d= getCurrentDate(document.get("eventdate").toString()+" "+document.get("eventEndTime").toString()+":00" );
                                            Date currentDate=new Date();
                                            System.out.println(currentDate);
                                            if(currentDate.after(d))
                                            {
                                                db.collection("events").document(document.getId().toString())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                                Toast.makeText(getApplicationContext(), "Old Events Added", Toast.LENGTH_LONG).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("TAG", "Error deleting document", e);
                                                            }
                                                        });
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        String eventDetails= "Eventdate:"+document.get("eventdate").toString()+
                                                "EventTime:"+document.get("eventStartTime").toString()+"-"+document.get("eventEndTime").toString()+"";




                                    }




                                }
                            }
                        });

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

    public static Date getCurrentDate(String dateInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss", Locale.ENGLISH);


        Date date = formatter.parse(dateInString);
        return date;
    }
}
