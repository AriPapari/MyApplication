package com.gecaj.arianit.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.toString();
    private WebView myWebView;
    private TextView textView;
    private SeekBar red, green, blue, white;
    private int i_red = 0, i_green = 0, i_blue = 0, i_white = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView  = (WebView) findViewById(R.id.webview);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        white = (SeekBar) findViewById(R.id.white);
        textView = (TextView) findViewById(R.id.textView3);

        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_red = i;
                myWebView.loadUrl("http://raspberrypi/test.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
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
                myWebView.loadUrl("http://raspberrypi/test.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
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
                myWebView.loadUrl("http://raspberrypi/test.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
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
                myWebView.loadUrl("http://raspberrypi/test.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void runScript(View view){
        Log.i(LOG_TAG, "######## BUTTON PRESSED ########");
        myWebView.loadUrl("http://raspberrypi/test.php/?red=0&green=0&blue=0&white=0");
        /*

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://raspberrypi/";

// Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(LOG_TAG, "######## onResponse ########");
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        */
    }
}
