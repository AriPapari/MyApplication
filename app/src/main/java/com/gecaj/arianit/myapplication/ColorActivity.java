package com.gecaj.arianit.myapplication;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import java.util.ArrayList;

import android.util.Log;

public class ColorActivity extends AppCompatActivity {

    private static final String LOG_TAG = ColorActivity.class.toString();
    private WebView myWebView;
    private SeekBar red, green, blue, white;
    private int i_red = 0, i_green = 0, i_blue = 0, i_white = 0;
    private int tr =0, tg=0,tb=0,tw=0;
    private double i_bright;
    private SurfaceView resultColor;
    private ListView colorList;
    private ArrayAdapter adapter;
    private int[] rgb;
    private ArrayList<int[]> itemList;
    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DBHandler(this,null,null,1);
        final Cursor result = dbHandler.getAllData();
        myWebView  = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://raspberrypi/php/start_LED.php");
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        white = (SeekBar) findViewById(R.id.white);
        resultColor =  (SurfaceView) findViewById(R.id.listColor);
        colorList = (ListView) findViewById(R.id.colorList);
        itemList = new ArrayList<>();

        //SurfaceView transparency
        resultColor.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = resultColor.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);


        //reading from DB
        if(result.getCount() != 0){
            while(result.moveToNext()){
                itemList.add(new int[]{result.getInt(1),result.getInt(2),result.getInt(3),result.getInt(4)});
            }
        }
        adapter = new ColorListAdapter(ColorActivity.this,R.layout.color_list, itemList);
        colorList.setAdapter(adapter);

        //SeekBar ChangeListeners
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_red = i;
                tr = (int)(i_bright*i_red);
                tg = (int)(i_bright*i_green);
                tb = (int)(i_bright*i_blue);
                tw = (int)(i_bright*i_white);
                set_resultColor(tr,tg,tb,tw);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);

            }
        });
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_green = i;
                tr = (int)(i_bright*i_red);
                tg = (int)(i_bright*i_green);
                tb = (int)(i_bright*i_blue);
                tw = (int)(i_bright*i_white);
                set_resultColor(tr,tg,tb,tw);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);

            }
        });
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_blue = i;
                tr = (int)(i_bright*i_red);
                tg = (int)(i_bright*i_green);
                tb = (int)(i_bright*i_blue);
                tw = (int)(i_bright*i_white);
                set_resultColor(tr,tg,tb,tw);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);

            }
        });
        white.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_bright = i;
                i_bright /= 100;
                Log.i(LOG_TAG, ">>> Trying to add Brightness i_bright: "+i_bright+" i:"+i);
                tr = (int)(i_bright*i_red);
                tg = (int)(i_bright*i_green);
                tb = (int)(i_bright*i_blue);
                tw = (int)(i_bright*i_white);
                set_resultColor(tr,tg,tb,tw);
                refresh_progress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);

            }
        });

        //On ListView-Item Click Listeners
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i_red = itemList.get(i)[0];
                i_green = itemList.get(i)[1];
                i_blue = itemList.get(i)[2];
                i_white = itemList.get(i)[3];
                myWebView.loadUrl("http://raspberrypi/php/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
                refresh_progress();
            }
        });
        colorList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(LOG_TAG, ">>> Trying to delete Color NR: "+i);
                itemList.remove(i);
                colorList.setAdapter(adapter);
                dbHandler.deleteData();
                //tabelle neu füllen
                for(int j = 0; j < itemList.size(); j++)
                    dbHandler.addColor(itemList.get(j));
                return true;
            }
        });

    }

    //refresh seekbars
    private void refresh_progress(){
        red.setProgress(tr);
        green.setProgress(tg);
        blue.setProgress(tb);
    }

    //turn off all LEDs
    public void reset_LED(View view){
        Log.i(LOG_TAG, "######## BUTTON PRESSED ########");
        myWebView.loadUrl("http://raspberrypi/php/kill.php/");
        i_red = 0; i_green = 0; i_blue = 0; i_white = 0;
        refresh_progress();
    }


    private void set_resultColor(int red, int green, int blue, int white){
        red = (int)(0.7*red);
        green = (int)(1.0*green);
        blue = (int)(0.9*blue);
        Log.i(LOG_TAG, ">>> i_bright="+i_bright+" red="+red+" green="+green+" blue="+blue+" white="+white);
        resultColor.setBackgroundColor(Color.argb(255-white,red,green,blue));

    }

    //add Color to itemList + DB
    public void add_Color(View view){
        rgb = new int[] {i_red,i_green,i_blue,i_white};
        itemList.add(rgb);
        dbHandler.addColor(rgb);
        adapter = new ColorListAdapter(ColorActivity.this,R.layout.color_list, itemList);
        colorList.setAdapter(adapter);
    }
}
