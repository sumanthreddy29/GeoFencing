package com.gsu.geofencing;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEvents extends AppCompatActivity implements   View.OnClickListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnDatePicker, btnTimePicker,btnToTimePicker,save;
    EditText txtDate, txtTime,txttoTime,address,txtEventName;
    Spinner category;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private double lat,lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        btnToTimePicker=(Button)findViewById(R.id.btn_out_time);
        category=(Spinner)findViewById(R.id.events);
        save=(Button)findViewById(R.id.btn_save);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txttoTime=(EditText)findViewById(R.id.out_time);
        txtEventName=(EditText)findViewById(R.id.eventname);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnToTimePicker.setOnClickListener(this);
        save.setOnClickListener(this);
        address=(EditText)findViewById(R.id.address);

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



    }


    public String getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.e("log",""+lng+"- "+lat);
            return lat + "," + lng;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker ) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == btnToTimePicker ) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            System.out.println(mHour+mMinute);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {


                                txttoTime.setText(hourOfDay + ":" + minute);

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        if(v==save)
        {

            Map<String, String> event = new HashMap<>();
            String eventName=txtEventName.getText().toString();
            String eventCategory=category.getSelectedItem().toString();
            String eventdate=txtDate.getText().toString();
            String eventStartTime=txtTime.getText().toString();
            String eventEndTime=txttoTime.getText().toString();
            String eventAddress= address.getText().toString();
            String latlong= getLocationFromAddress(eventAddress);

            String[] arrOfStr = latlong.split(",");
            lat=Double.parseDouble(arrOfStr[0]);
            lng=Double.parseDouble(arrOfStr[1]);
            Double latitude=lat;
            Double longitude=lng;
            event.put("eventName",eventName);
            event.put("eventCategory",eventCategory);
            event.put("eventdate",eventdate);
            event.put("eventStartTime",eventStartTime);
            event.put("eventEndTime",eventEndTime);
            event.put("latitude",latitude.toString());
            event.put("longitude",longitude.toString());
            event.put("Address",eventAddress);



            db.collection("events").document(eventName)
                    .set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_LONG).show();//display the text of button1
                            Intent i = new Intent(AddEvents.this, AdminActivity.class);
                            startActivity(i);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "data not saved", Toast.LENGTH_LONG).show();
                        }
                    });

            passEvent(event);
        }

    }
    public void passEvent(Map<String, String> x)
    {
        Log.e("interest",x.toString());

        MyNotificationManager.getInstance(this).displayNotification("Event:"+x.get("eventName"), "Category"+x.get("eventCategory"));

    }


}

