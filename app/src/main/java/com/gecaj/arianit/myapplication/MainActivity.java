package com.gecaj.arianit.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText raspIP;
    private TextView showIPview;
    private String server_url;
    private TextView resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showIPview = (TextView)findViewById(R.id.showIP);
        raspIP = (EditText)findViewById(R.id.raspIP);
        resp = (TextView)(findViewById(R.id.response));
        showIPview.setText(raspIP.getText().toString());
    }
    public void startColorpicker(View view){
        Intent colorpicker = new Intent(this, com.gecaj.arianit.myapplication.colorpicker.ColorActivity.class);
        colorpicker.putExtra("RASP_IP",showIPview.getText());
        startActivity(colorpicker);
    }
    public void startEffect(View view){
        Intent effect = new Intent(this, com.gecaj.arianit.myapplication.effect.Effect.class);
        effect.putExtra("RASP_IP",showIPview.getText());
        startActivity(effect);
    }
    public void startAlarm(View view){
        Intent alarm = new Intent(this, com.gecaj.arianit.myapplication.wakeup.WakeUpLight.class);
        alarm.putExtra("RASP_IP",showIPview.getText());
        startActivity(alarm);
    }
    public void startThief(View view){
        Intent thief = new Intent(this, com.gecaj.arianit.myapplication.antithief.AntiThief.class);
        thief.putExtra("RASP_IP",showIPview.getText());
        startActivity(thief);
    }
    public void startLightshow(View view){
        Intent lightshow = new Intent(this, com.gecaj.arianit.myapplication.lightshow.Lightshow.class);
        lightshow.putExtra("RASP_IP",showIPview.getText());
        startActivity(lightshow);
    }

    public void test(View view){
        showIPview.setText(raspIP.getText().toString());
        server_url = "http://"+raspIP.getText().toString()+"/php/response.php";
        resp.setText("");
        sendRequest();
    }

    private void sendRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resp.setTextColor(Color.GREEN);
                        resp.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        resp.setText("ERROR:\nRaspberry Pi not reachable!");
                        resp.setTextColor(Color.RED);
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
    }
}
