package com.example.iot_sensorjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ToggleButton buttonLampu1;
    ToggleButton buttonLampu2;

    TextView jarak;
    TextView ldr;
    TextView statusPir;

    String valueJarak;
    String valueLdr;
    String valuePir;
    String valueLampu1;
    String valueLampu2;

    DatabaseReference dref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLampu1 = (ToggleButton) findViewById(R.id.toggleButton);
        buttonLampu2 = (ToggleButton) findViewById(R.id.toggleButton2);

        statusPir = (TextView) findViewById(R.id.txtPIR);
        ldr = (TextView) findViewById(R.id.txtCahaya);
        jarak = (TextView) findViewById(R.id.txtJarak);


        dref = FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                valuePir = dataSnapshot.child("ESP32/pir").getValue().toString();
                if(valuePir.equals("0"))
                    statusPir.setText("No Motion");
                else
                    statusPir.setText("Motion Detect");

                valueLdr = dataSnapshot.child("ESP32/cahaya").getValue().toString();
                ldr.setText(valueLdr);

                valueJarak = dataSnapshot.child("ESP32/jarak").getValue().toString();
                jarak.setText(valueJarak);

                valueLampu1 = dataSnapshot.child("ESP32/led1").getValue().toString();
                if(valueLampu1.equals("0"))
                    buttonLampu1.setChecked(false);
                else
                    buttonLampu1.setChecked(true);

                valueLampu2 = dataSnapshot.child("ESP32/led2").getValue().toString();
                if(valueLampu2.equals("0"))
                    buttonLampu2.setChecked(false);
                else
                    buttonLampu2.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLampu1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("ESP32/led1");
                    lampu1Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu1Ref = FirebaseDatabase.getInstance().getReference("ESP32/led1");
                    lampu1Ref.setValue(0);
                }
            }
        });

        buttonLampu2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("ESP32/led2");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(1);
                }
                else
                {
                    DatabaseReference lampu2Ref = FirebaseDatabase.getInstance().getReference("ESP32/led2");
                    lampu2Ref.keepSynced(true);

                    lampu2Ref.setValue(0);
                }
            }
        });
    }
}