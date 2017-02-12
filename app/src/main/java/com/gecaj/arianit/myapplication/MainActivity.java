package com.gecaj.arianit.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView  = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://192.168.2.107/php/start_LED.php");
    }
    public void startColorpicker(View view){
        Intent colorpicker = new Intent(this, com.gecaj.arianit.myapplication.colorpicker.ColorActivity.class);
        startActivity(colorpicker);
    }
    public void startEffect(View view){
        Intent effect = new Intent(this, com.gecaj.arianit.myapplication.effect.Effect.class);
        startActivity(effect);
    }
    public void startAlarm(View view){
        Intent alarm = new Intent(this, com.gecaj.arianit.myapplication.wakeup.WakeUpLight.class);
        startActivity(alarm);
    }
    public void startThief(View view){
        Intent thief = new Intent(this, com.gecaj.arianit.myapplication.antithief.AntiThief.class);
        startActivity(thief);
    }
}
