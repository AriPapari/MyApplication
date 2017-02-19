package com.gecaj.arianit.myapplication.effect;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.DB.DBHandler;
import com.gecaj.arianit.myapplication.MySingleton;
import com.gecaj.arianit.myapplication.R;
import com.gecaj.arianit.myapplication.colorpicker.ColorActivity;
import com.gecaj.arianit.myapplication.colorpicker.ColorListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Effect extends AppCompatActivity {
    private Intent intent = getIntent();
    private TextView viewResponse;
    private String server_url;
    private String raspIP;
    private ListView rgbColView, hexColView, effectView;
    private SurfaceView resultColorPicker;
    private WebView myWebView;
    private final ColorActivity adj = new ColorActivity();
    private ArrayList<double[]> rgbColARL;
    private ArrayList<String> hexColARL, rndhexColARL;
    private ArrayList<String> effectARL;
    public static final int DB_VERSION = 2;
    private final DBHandler dbHandler = new DBHandler(this,null,null, DB_VERSION);
    private Cursor resultRGB, resultHEX;
    int red = 0, green = 0, blue = 0, white = 0;
    double bright = 1.0;
    private double speed;
    private int effectType;
    private SeekBar speedBar, whiteBar;
    private static final String LOG_TAG = ColorActivity.class.toString();
    private CheckBox rndColors, rndEffect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect);
        init();
        setListeners();
    }

    private void init(){
        raspIP = getIntent().getStringExtra("RASP_IP");
        server_url = "http://"+raspIP+"/php/json_post.php";
        rgbColView = (ListView)findViewById(R.id.colorList);
        hexColView = (ListView)findViewById(R.id.effectColors);
        effectView = (ListView)findViewById(R.id.effectListView);
        rgbColARL = new ArrayList<>();
        hexColARL = new ArrayList<>();
        rndhexColARL = new ArrayList<>();
        myWebView  = (WebView)findViewById(R.id.webview);
        speedBar = (SeekBar)findViewById(R.id.speedBar);
        whiteBar = (SeekBar)findViewById(R.id.whiteBar);
        viewResponse = (TextView)findViewById(R.id.textView6);
        rndColors = (CheckBox)findViewById(R.id.rndColCheck);
        rndEffect = (CheckBox)findViewById(R.id.rndEffCheck);





        //reading from DB
        resultRGB = dbHandler.getAllRGBdata();
        if(resultRGB.getCount() != 0) {
            while (resultRGB.moveToNext()) {
                rgbColARL.add(new double[]{resultRGB.getDouble(1), resultRGB.getDouble(2), resultRGB.getDouble(3), resultRGB.getDouble(4), resultRGB.getDouble(5)});
                rgbColView.setAdapter(new ColorListAdapter(Effect.this, R.layout.color_list, rgbColARL));
            }
        }

        resultHEX = dbHandler.getAllRGBdata();
        /*
        if(resultHEX.getCount() != 0) {
            while (resultHEX.moveToNext()) {
                hexColARL.add(resultHEX.getString(1));
                hexColView.setAdapter(new HexColorListAdapter(Effect.this, R.layout.color_list, hexColARL));
            }
        }
        */
    }

    private void setListeners(){

        //HexColCheck/////////////
        rndEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    effectView.setEnabled(false);
                    effectView.setAlpha((float)(0.4));
                }else{
                    effectView.setEnabled(true);
                    effectView.setAlpha((float)(1));
                }
            }
        });
        //EffectCheck/////////////
        rndColors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    hexColView.setEnabled(false);
                    hexColView.setAlpha((float)(0.4));
                }else{
                    hexColView.setEnabled(true);
                    hexColView.setAlpha((float)(1));
                }
            }
        });

        //RGBColView//////////////
        rgbColView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                hexColARL.add(toHexColor(new double[]{rgbColARL.get(i)[0], rgbColARL.get(i)[1], rgbColARL.get(i)[2]}));
                //refresh list
                hexColView.setAdapter(new HexColorListAdapter(Effect.this,R.layout.color_list, hexColARL));
            }
        });
        rgbColView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                rgbColARL.remove(i);
                rgbColView.setAdapter(new ColorListAdapter(Effect.this, R.layout.color_list, rgbColARL));
                dbHandler.deleteData();
                //tabelle neu füllen
                for(int j = 0; j < rgbColARL.size(); j++)
                    dbHandler.addRGBcolor(rgbColARL.get(j));
                return true;
            }
        });

        //HexColView//////////////
        hexColView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                myWebView.loadUrl("http://"+raspIP+"/php/LED_OTF.php/?red="+Integer.parseInt(hexColView.getItemAtPosition(i).toString().substring(0,2),16)+
                        "&green="+Integer.parseInt(hexColView.getItemAtPosition(i).toString().substring(2,4),16)+
                        "&blue="+Integer.parseInt(hexColView.getItemAtPosition(i).toString().substring(4,6),16)+
                        "&white= 0");
            }
        });
        hexColView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                hexColARL.remove(i);
                hexColView.setAdapter(new HexColorListAdapter(Effect.this,R.layout.color_list, hexColARL));
                return true;
            }
        });

        //EffectView//////////////
        //fill effect type with data out of DB
        effectView.setAdapter(new ArrayAdapter<>(this,R.layout.adapter_effectview,R.id.effectItem,new String[]{"Strobe","Fade","Flash"}));
        effectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                effectType = i;
            }
        });

        //SpeedBar//////////////
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
                //change speed value in json file
                myWebView.loadUrl("http://"+raspIP+"/php/LED_speed.php/?speed="+(100-speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dbHandler.setColumnSpeed(speed);
            }
        });

        //WhiteBar//////////////
        whiteBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                white = i;
                myWebView.loadUrl("http://"+raspIP+"/php/LED_white.php/?white="+white);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void sendRequest(final View view){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //do smth...
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(LOG_TAG, "VolleyError while Post: "+ error.toString());

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                JSONArray jsnar = new JSONArray();
                JSONObject jsnobj = new JSONObject();
                try {
                    if(rndColors.isChecked()) {
                        for (int i = 0; i <15; i++){
                            double[] rgb = new double[]{Math.random()*255,Math.random()*255,Math.random()*255};
                            rndhexColARL.add(toHexColor(rgb));
                        }
                        for (int i = 0; i < rndhexColARL.size(); i++)
                            jsnar.put(rndhexColARL.get(i));
                        rndhexColARL.clear();
                    }
                    else
                        for(int i = 0; i < hexColARL.size(); i++)
                            jsnar.put(hexColARL.get(i));
                    jsnobj.put("hexCol",jsnar);
                    jsnobj.put("speed",100-speed);
                    jsnobj.put("white",white);
                    if(rndEffect.isChecked())
                        jsnobj.put("effect",(int)(3*Math.random()));
                    else
                        jsnobj.put("effect",effectType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("effectOBJ", jsnobj.toString());
                return params;
            }
        };
        /*
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20),
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                */
        MySingleton.getInstance(Effect.this).addToRequestQue(stringRequest);
    }

    public void startColorpicker(View view){
        final Dialog dialog = new Dialog(Effect.this);
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
                //add to db here and refresh colorlist!
                //add Color to rgbColARL + DB
                double []rgb = new double[] {red,green,blue,white,bright};
                rgbColARL.add(rgb);
                dbHandler.addRGBcolor(rgb);
                //refresh list
                rgbColView.setAdapter(new ColorListAdapter(Effect.this,R.layout.color_list, rgbColARL));
                dialog.cancel();
                white = red = green = blue = 0;
                bright = 1.0;
            }
        });

        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                white = red = green = blue = 0;
                bright = 1.0;
            }
        });
    }

    private void setResultColor(int red, int green, int blue, int white, double bright){
        resultColorPicker.setBackgroundColor(Color.rgb((int)(red*bright*adj.getColorAdjust()[0]),
                (int)(green*bright*adj.getColorAdjust()[1]),
                (int)(blue*bright*adj.getColorAdjust()[2])));
        myWebView.loadUrl("http://"+raspIP+"/php/LED_OTF.php/?red="+(int)(red*bright)+
                "&green="+(int)(green*bright)+
                "&blue="+(int)(blue*bright)+
                "&white="+white);
    }

    private String toHexColor(double[] rgb){
        String hexValue = "";
        for(int i = 0; i <= 2; i++){
            if(rgb[i]<16)
                hexValue+="0"+Integer.toHexString((int)rgb[i]);
            else
                hexValue+=Integer.toHexString((int)rgb[i]);
            Log.i(LOG_TAG, ">>> RGBTOHEX "+i+": "+(int)rgb[i]+" HEXVALUE: "+hexValue);
        }
        Log.i(LOG_TAG, ">>> RETURNING HEXSTRING: "+hexValue);
        return hexValue;
    }
}
