package com.gecaj.arianit.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.toString();
    private WebView myWebView;
    private SeekBar red, green, blue, white;
    private int i_red = 0, i_green = 0, i_blue = 0, i_white = 0;
    private SurfaceView resultColor;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView  = (WebView) findViewById(R.id.webview);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        white = (SeekBar) findViewById(R.id.white);
        resultColor =  (SurfaceView) findViewById(R.id.resultColor);
        resetButton = (Button) findViewById(R.id.button);

        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_red = i;
                //resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_green = i;
                //resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_blue = i;
               // resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        white.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_white = i;
                //resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void reset_LED(View view){
        Log.i(LOG_TAG, "######## BUTTON PRESSED ########");
        myWebView.loadUrl("http://raspberrypi/reset.php/");
    }
}
