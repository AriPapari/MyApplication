package com.gecaj.arianit.myapplication.wakeup;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.gecaj.arianit.myapplication.R;

import java.sql.Time;

public class WakeUpLight extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    TimePicker timePicker;
    ToggleButton alarmSwitch;
    TextView alarmStatus;
    String time;
    private static final String LOG_TAG = WakeUpLight.class.toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeuplight);
        init();
    }
    private void init(){
        alarmStatus = (TextView)findViewById(R.id.alarmText);
        alarmSwitch = (ToggleButton)findViewById(R.id.alarmSwitch);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        spinner = (Spinner)findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(this,R.array.spinner_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    time = (timePicker.getHour()+":"+timePicker.getMinute());
                    Log.d(LOG_TAG,time);
                    alarmStatus.setText("Alarm set for "+time+"\nEarly Bird set to "+spinner.getSelectedItem());
                    timePicker.setEnabled(false);
                    timePicker.setVerticalScrollBarEnabled(false);
                    spinner.setEnabled(false);
                    timePicker.setAlpha((float)(0.6));
                    spinner.setAlpha((float)(0.6));
                }
                else if(!b){
                    alarmStatus.setText(R.string.alarmHintText);
                    timePicker.setEnabled(true);
                    timePicker.setVerticalScrollBarEnabled(true);
                    spinner.setEnabled(true);
                    timePicker.setAlpha((float)1);
                    spinner.setAlpha((float)1);
                }
            }
        });
    }
}
