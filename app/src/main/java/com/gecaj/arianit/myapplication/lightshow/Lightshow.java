package com.gecaj.arianit.myapplication.lightshow;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.MySingleton;
import com.gecaj.arianit.myapplication.R;
import com.gecaj.arianit.myapplication.colorpicker.ColorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Lightshow extends AppCompatActivity {
    private String raspIP;
    private WebView myWebView;
    private final ColorActivity adj = new ColorActivity();
    private SurfaceView resultColorPicker;
    private SurfaceView surfaceView;
    private TextView state;
    private Button startShow, stopShow;
    private RadioButton rb1, rb2, rb3;
    int red = 0, green = 0, blue = 0, white = 0;
    double bright = 1.0;
    private int style;
    private String server_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightshow);
        init();
        setListeners();
    }
    private void init(){
        raspIP = getIntent().getStringExtra("RASP_IP");
        server_url = "http://"+raspIP+"/php/json_lightshow.php";
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        myWebView  = (WebView)findViewById(R.id.webview);
        state = (TextView)findViewById(R.id.textView16);
        startShow = (Button)findViewById(R.id.lightshowStart);
        stopShow = (Button)findViewById(R.id.lightshowStop);
        rb1 = (RadioButton)findViewById(R.id.radioButton);
        rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rb3 = (RadioButton)findViewById(R.id.radioButton3);
        rb1.setChecked(true);
        style = 1;
        surfaceView.setBackgroundColor(Color.rgb((int)(red*bright*adj.getColorAdjust()[0]),
                (int)(green*bright*adj.getColorAdjust()[1]),
                (int)(blue*bright*adj.getColorAdjust()[2])));
    }
    private void setListeners(){
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startColorpicker(view);
            }
        });
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                style = 1;
            }
        });
        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                style = 2;
            }
        });
        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                style = 3;
            }
        });
        startShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        stopShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWebView.loadUrl("http://"+raspIP+"/php/stop_lightshow.php");
            }
        });
    }

    public void startColorpicker(View view){
        final Dialog dialog = new Dialog(Lightshow.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //custom xml for dialog here
        dialog.setContentView(R.layout.dialog_colorpicker);
        dialog.show();
        resultColorPicker = (SurfaceView)dialog.findViewById(R.id.listColor);
        final SeekBar blue_seek = (SeekBar)dialog.findViewById(R.id.blue);
        final SeekBar green_seek = (SeekBar)dialog.findViewById(R.id.green);
        final SeekBar red_seek = (SeekBar)dialog.findViewById(R.id.red);
        final SeekBar white_seek = (SeekBar)dialog.findViewById(R.id.white);
        final SeekBar bright_seek = (SeekBar)dialog.findViewById(R.id.bright);

        red_seek.setProgress(red);
        green_seek.setProgress(green);
        blue_seek.setProgress(blue);
        white_seek.setProgress(white);
        bright_seek.setProgress((int)(bright*100));
        //SeekBar ChangeListeners
        red_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                setResultColor(red,green,blue,white,bright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        green_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                setResultColor(red,green,blue,white,bright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        blue_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                setResultColor(red,green,blue,white,bright);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        white_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                white = i;
                setResultColor(red,green,blue,white,bright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        bright_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bright = i;
                bright /= 100;
                setResultColor(red,green,blue,white,bright);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.findViewById(R.id.addColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                white = red = green = blue = 0;
                bright = 1.0;
                setResultColor(0,0,0,0,1.0);
                red_seek.setProgress(0);
                green_seek.setProgress(0);
                blue_seek.setProgress(0);
                white_seek.setProgress(0);
                bright_seek.setProgress(100);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
    }

    private void setResultColor(int red, int green, int blue, int white, double bright){
        resultColorPicker.setBackgroundColor(Color.rgb((int)(red*bright*adj.getColorAdjust()[0]),
                (int)(green*bright*adj.getColorAdjust()[1]),
                (int)(blue*bright*adj.getColorAdjust()[2])));
        surfaceView.setBackgroundColor(Color.rgb((int)(red*bright*adj.getColorAdjust()[0]),
                (int)(green*bright*adj.getColorAdjust()[1]),
                (int)(blue*bright*adj.getColorAdjust()[2])));
        myWebView.loadUrl("http://"+raspIP+"/php/LED_OTF.php/?red="+(int)(red*bright)+
                "&green="+(int)(green*bright)+
                "&blue="+(int)(blue*bright)+
                "&white="+white);
    }

    private void sendRequest(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        state.setText("Enjoy!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        state.setText("Failed!");
                    }
                }){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                JSONObject jsnobj = new JSONObject();
                try {
                    jsnobj.put("style",style);
                    jsnobj.put("red",(int)(red*bright));
                    jsnobj.put("green",(int)(green*bright));
                    jsnobj.put("blue",(int)(blue*bright));
                    jsnobj.put("white",(int)(white*bright));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("lightshowConfig", jsnobj.toString());
                return params;
            }
        };

        MySingleton.getInstance(Lightshow.this).addToRequestQue(stringRequest);
    }
}
