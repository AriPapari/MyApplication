package com.gecaj.arianit.myapplication.effect;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gecaj.arianit.myapplication.DB.DBHandler;
import com.gecaj.arianit.myapplication.R;
import com.gecaj.arianit.myapplication.colorpicker.ColorActivity;
import com.gecaj.arianit.myapplication.colorpicker.ColorListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Effect extends AppCompatActivity {
    EditText name,pw;
    TextView viewResponse;
    String server_url = "http://192.168.178.64/php/json_db.php";
    private ListView colorList, hexcol;
    private ArrayAdapter adapter;
    private ArrayList<double[]> colorItemList;
    public static final int DB_VERSION = 2;
    private final DBHandler dbHandler = new DBHandler(this,null,null, DB_VERSION);
    private Cursor resultRGB, resultHEX;
    private WebView myWebView;
    private final ColorActivity adj = new ColorActivity();
    private SurfaceView resultColor;
    int red = 0, green = 0, blue = 0, white = 0;
    double bright = 1.0;
    private ArrayList<String> hexColors;
    private double speed;
    private int effect;
    private SeekBar speedBar;

    private void init(){
        setContentView(R.layout.activity_effect);
        colorList = (ListView) findViewById(R.id.colorList);
        colorItemList = new ArrayList<>();
        hexColors = new ArrayList<>();
        myWebView  = (WebView) findViewById(R.id.webview);
        speedBar = (SeekBar) findViewById(R.id.speedBar);
        viewResponse = (TextView)findViewById(R.id.textView);


        //reading from DB
        resultRGB = dbHandler.getAllRGBdata();
        if(resultRGB.getCount() != 0) {
            while (resultRGB.moveToNext()) {
                colorItemList.add(new double[]{resultRGB.getDouble(1), resultRGB.getDouble(2), resultRGB.getDouble(3), resultRGB.getDouble(4), resultRGB.getDouble(5)});
                adapter = new ColorListAdapter(Effect.this, R.layout.color_list, colorItemList);
                colorList.setAdapter(adapter);
            }
        }

        resultHEX = dbHandler.getAllRGBdata();
        if(resultHEX.getCount() != 0) {
            while (resultHEX.moveToNext()) {
                hexColors.add(resultHEX.getString(1));
                adapter = new ColorListAdapter(Effect.this, R.layout.color_list, colorItemList);
                .setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        //On ListView-Item Click Listeners
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        colorList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                colorItemList.remove(i);
                colorList.setAdapter(adapter);
                dbHandler.deleteData();
                //tabelle neu füllen
                for(int j = 0; j < colorItemList.size(); j++)
                    dbHandler.addRGBcolor(colorItemList.get(j));
                return true;
            }
        });


        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = i;
                //change speed value in json file
                myWebView.loadUrl("http://raspberrypi/php/LED_speed.php");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dbHandler.setColumnSpeed(speed);
            }
        });




        //name = (EditText)findViewById(R.id.editText2);
        //pw = (EditText)findViewById(R.id.editText3);
    }

    public void gibIhm(final View view){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewResponse.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        viewResponse.setText("VolleyError while Post: "+ error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                for(int i = 0; i < hexColors.length; i++)
                    params.put("color"+i,hexColors[i]);
                params.put("speed",Double.toString(speed));
                params.put("effect", Integer.toString(effect));
                //key same as param name in php!
                return params;
            }
        };
        MySingleton.getInstance(Effect.this).addToRequestQue(stringRequest);
    }

    public void startColorpicker(View view){
        final Dialog dialog = new Dialog(Effect.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //custom xml for dialog here
        dialog.setContentView(R.layout.dialog_colorpicker);
        dialog.show();
        resultColor = (SurfaceView)dialog.findViewById(R.id.listColor);
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
                //add Color to colorItemList + DB
                double []rgb = new double[] {red,green,blue,white,bright};
                colorItemList.add(rgb);
                dbHandler.addRGBcolor(rgb);
                //refresh list
                adapter = new ColorListAdapter(Effect.this,R.layout.color_list, colorItemList);
                colorList.setAdapter(adapter);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setResultColor(int red, int green, int blue, int white, double bright){
        double ar = adj.getColorAdjust()[0], ag = adj.getColorAdjust()[1], ab = adj.getColorAdjust()[2];
        resultColor.setBackgroundColor(Color.rgb((int)(red*bright*ar),(int)(green*bright*ag),(int)(blue*bright*ab)));
        myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+(int)(red*bright*ar)+
                "&green="+(int)(green*bright*ag)+
                "&blue="+(int)(blue*bright*ab)+
                "&white="+white);
    }
}
