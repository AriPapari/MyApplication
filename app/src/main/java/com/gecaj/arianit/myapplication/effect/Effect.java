package com.gecaj.arianit.myapplication.effect;

import android.app.Dialog;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Effect extends AppCompatActivity {
    private TextView viewResponse;
    private String server_url = "http://192.168.178.64/php/json_db.php";
    private ListView rgbColView, hexColView, effectView;
    private SurfaceView resultColorPicker;
    private WebView myWebView;
    private ArrayList<double[]> rgbColARL;
    private ArrayList<String> hexColARL;
    private ArrayList<Integer> effectARL;
    public static final int DB_VERSION = 2;
    private final DBHandler dbHandler = new DBHandler(this,null,null, DB_VERSION);
    private Cursor resultRGB, resultHEX;
    int red = 0, green = 0, blue = 0, white = 0;
    double bright = 1.0;
    private double speed;
    private int effectType;
    private SeekBar speedBar, whiteBar;
    private final ColorActivity adj = new ColorActivity();
    private static final String LOG_TAG = ColorActivity.class.toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setListeners();
    }

    private void init(){
        setContentView(R.layout.activity_effect);
        rgbColView = (ListView)findViewById(R.id.colorList);
        hexColView = (ListView)findViewById(R.id.effectColors);
        effectView = (ListView)findViewById(R.id.effectListView);
        rgbColARL = new ArrayList<>();
        hexColARL = new ArrayList<>();
        effectARL = new ArrayList<>();
        myWebView  = (WebView)findViewById(R.id.webview);
        speedBar = (SeekBar)findViewById(R.id.speedBar);
        whiteBar = (SeekBar)findViewById(R.id.whiteBar);
        viewResponse = (TextView)findViewById(R.id.textView);


        //fill effect type with data out of DB
        effectARL.add(0);
        effectARL.add(1);
        effectView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,effectARL));



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
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+Integer.parseInt(hexColView.getItemAtPosition(i).toString().substring(0,2),16)+
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
        effectView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j = 0; j < effectView.getAdapter().getCount(); j++)
                    getViewByPosition(j,effectView).setBackgroundColor(Color.WHITE);
                view.setBackgroundColor(Color.CYAN);
                effectType = effectARL.get(i);
            }
        });

        //SpeedBar//////////////
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

        //WhiteBar//////////////
        whiteBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                white = i;
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
                JSONObject jsnmain = new JSONObject();
                JSONArray jsnar = new JSONArray();
                JSONObject jsnobj = new JSONObject();
                try {
                    for(int i = 0; i < hexColARL.size(); i++)
                        jsnar.put(hexColARL.get(i));
                    jsnobj.put("hexCol",jsnar);
                    jsnobj.put("speed",speed);
                    jsnobj.put("white",white);
                    jsnobj.put("effect",effectType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("effectOBJ", jsnobj.toString());

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
        myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+(int)(red*bright)+
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


    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
