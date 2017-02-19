package com.gecaj.arianit.myapplication.antithief;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.MySingleton;
import com.gecaj.arianit.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AntiThief extends AppCompatActivity {
    private Button tv, lights;
    private WebView myWebView;
    private TextView state;
    private String raspIP;
    private String server_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_thief);
        init();
        setListeners();
    }
    private void init(){
        raspIP = getIntent().getStringExtra("RASP_IP");
        server_url = "http://"+raspIP+"/php/json_thief.php";
        myWebView = (WebView)findViewById(R.id.myWebView);
        state = (TextView)findViewById(R.id.textView11);
        tv = (Button)findViewById(R.id.tv);
        lights = (Button)findViewById(R.id.lights);
    }

    private void setListeners(){
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest("tv");
            }
        });
        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest("lights");
            }
        });
    }
    private void sendRequest(final String scene){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        state.setText("Response:\n"+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        state.setText("VolleyError while Post: \n"+error.toString()+"..");
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                JSONObject jsnobj = new JSONObject();
                try {
                    jsnobj.put("scene",scene);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("thiefConfig", jsnobj.toString());
                return params;
            }
        };

        MySingleton.getInstance(AntiThief.this).addToRequestQue(stringRequest);
    }

    public void turnOFF(View view){
        myWebView.loadUrl("http://"+raspIP+"/php/kill.php/");
    }
}
