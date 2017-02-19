package com.gecaj.arianit.myapplication.wakeup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.R;
import com.gecaj.arianit.myapplication.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WakeUpLight extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private TimePicker timePicker;
    private ToggleButton alarmSwitch;
    private TextView alarmStatus;
    private SurfaceView disabler;
    private String server_url, server_url_check_alarm, time,raspIP;
    private static final String LOG_TAG = WakeUpLight.class.toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeuplight);
        init();
        setListeners();
    }
    private void init(){
        raspIP = getIntent().getStringExtra("RASP_IP");
        server_url = "http://"+raspIP+"/php/json_alarm.php";
        server_url_check_alarm = "http://"+raspIP+"/php/check_alarm.php";
        alarmStatus = (TextView)findViewById(R.id.alarmText);
        alarmStatus.setTextColor(Color.DKGRAY);
        alarmSwitch = (ToggleButton)findViewById(R.id.alarmSwitch);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        spinner = (Spinner)findViewById(R.id.spinner);
        disabler = (SurfaceView) findViewById(R.id.disabler);

        adapter = ArrayAdapter.createFromResource(this,R.array.spinner_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        checkAlarm();
    }

    private void setListeners(){
        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    time = (timePicker.getHour()+":"+timePicker.getMinute());
                    Log.d(LOG_TAG,time);
                    alarmStatus.setTextColor(Color.GREEN);
                    alarmStatus.setText("Alarm set for "+time+"\nEarly Bird set to "+spinner.getSelectedItem());
                    timePicker.setEnabled(false);
                    timePicker.setVerticalScrollBarEnabled(false);
                    spinner.setEnabled(false);
                    timePicker.setAlpha((float)(0.6));
                    spinner.setAlpha((float)(0.6));
                    disabler.setVisibility(View.VISIBLE);
                }
                else if(!b){
                    alarmStatus.setTextColor(Color.DKGRAY);
                    alarmStatus.setText(R.string.alarmHintText);
                    timePicker.setEnabled(true);
                    timePicker.setVerticalScrollBarEnabled(true);
                    spinner.setEnabled(true);
                    timePicker.setAlpha((float)1);
                    spinner.setAlpha((float)1);
                    disabler.setVisibility(View.INVISIBLE);
                }
                sendRequest(b);
            }
        });
    }
    private void checkAlarm(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url_check_alarm,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            alarmStatus.setTextColor(Color.GREEN);
                            alarmStatus.setText(response);
                            timePicker.setEnabled(false);
                            timePicker.setVerticalScrollBarEnabled(false);
                            spinner.setEnabled(false);
                            timePicker.setAlpha((float)(0.6));
                            spinner.setAlpha((float)(0.6));
                            disabler.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alarmStatus.setTextColor(Color.RED);
                        alarmStatus.setText("VolleyError while Post: "+error.toString()+"..");
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };

        MySingleton.getInstance(WakeUpLight.this).addToRequestQue(stringRequest);
    }

    private void sendRequest(final boolean alarmset){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        alarmStatus.setText("Response: "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        alarmStatus.setTextColor(Color.RED);
                        alarmStatus.setText("VolleyError while Post: "+error.toString()+"..");
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                JSONObject jsnobj = new JSONObject();
                try {
                    jsnobj.put("hour",timePicker.getHour());
                    jsnobj.put("minutes",timePicker.getMinute());
                    jsnobj.put("earlybird",spinner.getSelectedItem().toString().substring(0,2));
                    jsnobj.put("alarmset",alarmset);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("alarmConfig", jsnobj.toString());
                return params;
            }
        };

        MySingleton.getInstance(WakeUpLight.this).addToRequestQue(stringRequest);
    }
}
