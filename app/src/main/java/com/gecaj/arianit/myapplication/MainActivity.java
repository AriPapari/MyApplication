package com.gecaj.arianit.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startColorpicker(View view){
        Intent colorpicker = new Intent(this, com.gecaj.arianit.myapplication.colorpicker.ColorActivity.class);
        startActivity(colorpicker);
    }
    public void startEffect(View view){
        Intent effect = new Intent(this, com.gecaj.arianit.myapplication.effect.Effect.class);
        startActivity(effect);
    }
}
