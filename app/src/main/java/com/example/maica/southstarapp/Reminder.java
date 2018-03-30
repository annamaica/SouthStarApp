package com.example.maica.southstarapp;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Reminder extends AppCompatActivity {

    public static AlarmManager alarmManager;
    private PendingIntent pending_intent;

    private TimePicker alarmTimePicker;
    private TextView alarmTextView;

    private AlarmReceiver alarm;

    FirebaseAuth firebaseAuth;


    EditText alarm_name, alarm_quantity, alarm_desc;

    DatabaseReference db_alarm;

    Reminder inst;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        this.context = this;

        firebaseAuth = FirebaseAuth.getInstance();
        alarm_name = findViewById(R.id.alarm_name);
        alarm_quantity = findViewById(R.id.alarm_quantity);
        alarm_desc = findViewById(R.id.alarm_desc);

        db_alarm = FirebaseDatabase.getInstance().getReference("Alarm").child(firebaseAuth.getCurrentUser().getUid());

        Query search = db_alarm.orderByChild("alarm_user").equalTo(firebaseAuth.getCurrentUser().getUid());
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Alarm_details details = snapshot.getValue(Alarm_details.class);
                        try {
                            String title = details.getAlarm_title();
                            String quantity = details.getAlarm_quantity();
                            String desc = details.getAlarm_desc();
                            Integer hour = details.getAlarm_hour();
                            Integer minute = details.getAlarm_minute();
                            alarm_name.setText(title);
                            alarm_quantity.setText(quantity);
                            alarm_desc.setText(desc);
                            alarmTextView.setText("Alarm set to " + hour + ":" +minute);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //alarm = new AlarmReceiver();
        alarmTextView = findViewById(R.id.alarmText);

        final Intent myIntent = new Intent(this.context, AlarmReceiver.class);

        // Get the alarm manager service
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // set the alarm to the time that you picked
        final Calendar calendar = Calendar.getInstance();

        alarmTimePicker = findViewById(R.id.alarmTimePicker);


        Button start_alarm= findViewById(R.id.start_alarm);
        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                String alarm_title = alarm_name.getText().toString().trim();
                String alarm_number = alarm_quantity.getText().toString().trim();
                String alarm_description = alarm_desc.getText().toString().trim();

                if (TextUtils.isEmpty(alarm_title)) {
                    Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(alarm_number)) {
                    Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(alarm_description)) {
                    Toast.makeText(getApplicationContext(), "Please fill up the details", Toast.LENGTH_LONG).show();
                }

                calendar.add(Calendar.SECOND, 3);
                //setAlarmText("You clicked a button");

                final int hour = alarmTimePicker.getCurrentHour();
                final int minute = alarmTimePicker.getCurrentMinute();;

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                setAlarmText("You clicked a " + hour + " and " + minute);


                calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

                myIntent.putExtra("extra", "yes");
                pending_intent = PendingIntent.getBroadcast(Reminder.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);


                // now you should change the set Alarm text so it says something nice


                setAlarmText("Alarm set to " + hour + ":" + minute);
                //Toast.makeText(getApplicationContext(), "You set the alarm", Toast.LENGTH_SHORT).show();

                String alarm_id = db_alarm.push().getKey();
                Alarm_details details = new Alarm_details(alarm_id, firebaseAuth.getCurrentUser().getUid(), alarm_title, alarm_number, alarm_description, hour, minute);
                db_alarm.child(firebaseAuth.getCurrentUser().getUid()).setValue(details);
            }

        });

        Button stop_alarm= (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);

                alarmManager.cancel(pending_intent);
                setAlarmText("Alarm canceled");
                //setAlarmText("You clicked a " + " canceled");
            }
        });

    }


    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }



    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }
}
