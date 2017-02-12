package com.gecaj.arianit.myapplication.colorpicker;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import java.util.ArrayList;

import android.util.Log;

import com.gecaj.arianit.myapplication.DB.DBHandler;
import com.gecaj.arianit.myapplication.R;

public class ColorActivity extends AppCompatActivity {

    private static final String LOG_TAG = ColorActivity.class.toString();
    public static final int DB_VERSION = 2;
    private WebView myWebView;
    private SeekBar red, green, blue, white, brightness;
    private int i_red = 0, i_green = 0, i_blue = 0, i_white = 0;
    private int tr =0, tg=0,tb=0,tw=0;
    private double i_bright = 1.0;
    private SurfaceView resultColor;
    private ListView colorList;
    private ArrayAdapter adapter;
    private double[] rgb;
    private ArrayList<double[]> itemList;
    private DBHandler dbHandler;
    private final double[] colorAdjust = new double[] {1.0,0.55,1.0};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorpicker);
        dbHandler = new DBHandler(this,null,null, DB_VERSION);
        final Cursor result = dbHandler.getAllRGBdata();
        myWebView  = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://192.168.2.107/php/start_LED.php");
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        brightness = (SeekBar) findViewById(R.id.bright);
        white = (SeekBar) findViewById(R.id.white);
        resultColor =  (SurfaceView) findViewById(R.id.listColor);
        colorList = (ListView) findViewById(R.id.colorList);
        itemList = new ArrayList<>();

        //reading from DB
        if(result.getCount() != 0){
            while(result.moveToNext()){
                itemList.add(new double[]{result.getDouble(1),result.getDouble(2),result.getDouble(3),result.getDouble(4),result.getDouble(5)});
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
                set_resultColor(tr,tg,tb,tw);
                myWebView.loadUrl("http://192.168.2.107/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);
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
                tg = (int)(i_bright*i_green);
                set_resultColor(tr,tg,tb,tw);
                myWebView.loadUrl("http://192.168.2.107/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);
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
                tb = (int)(i_bright*i_blue);
                set_resultColor(tr,tg,tb,tw);
                myWebView.loadUrl("http://192.168.2.107/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);
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
                tw = i_white;
                set_resultColor(tr,tg,tb,tw);
                myWebView.loadUrl("http://192.168.2.107/php/LED_OTF.php/?red="+tr+"&green="+tg+"&blue="+tb+"&white="+tw);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_bright = i;
                i_bright /= 100;
                Log.i(LOG_TAG, ">>> Trying to add Brightness i_bright: "+i_bright+" i:"+i);
                tr = (int)(i_bright*i_red);
                tg = (int)(i_bright*i_green);
                tb = (int)(i_bright*i_blue);
                set_resultColor(tr,tg,tb,tw);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //On ListView-Item Click Listeners
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i_red = (int)itemList.get(i)[0];
                i_green = (int)itemList.get(i)[1];
                i_blue = (int)itemList.get(i)[2];
                i_white = (int)itemList.get(i)[3];
                i_bright = itemList.get(i)[4];
                Log.i(LOG_TAG, "ONCLICK >>> i_bright="+i_bright+" red="+i_red+" green="+i_green+" blue="+i_blue+" white="+i_white);
                set_resultColor((int)(i_red*i_bright),(int)(i_green*i_bright),(int)(i_blue*i_bright),i_white);
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
                    dbHandler.addRGBcolor(itemList.get(j));
                return true;
            }
        });

    }
    public ArrayList<double[]> getItemList(){return itemList;}
    public double[] getColorAdjust(){
        return colorAdjust;
    }

    //refresh seekbars
    private void refresh_progress(){
        red.setProgress(i_red);
        green.setProgress(i_green);
        blue.setProgress(i_blue);
        white.setProgress(i_white);
        brightness.setProgress((int)(i_bright*100));
    }

    //turn off all LEDs
    public void reset_LED(View view){
        Log.i(LOG_TAG, "######## BUTTON PRESSED ########");
        myWebView.loadUrl("http://192.168.2.107/php/kill.php/");
        i_red = 0; i_green = 0; i_blue = 0; i_white = 0;
        refresh_progress();
    }


    private void set_resultColor(int red, int green, int blue, int white){
        myWebView.loadUrl("http://192.168.2.107/php/LED_OTF.php/?red="+red+"&green="+green+"&blue="+blue+"&white="+white);
        red = (int)(colorAdjust[0]*red);
        green = (int)(colorAdjust[1]*green);
        blue = (int)(colorAdjust[2]*blue);
        Log.i(LOG_TAG, ">>> i_bright="+i_bright+" red="+red+" green="+green+" blue="+blue+" white="+white);
        resultColor.setBackgroundColor(Color.rgb(red,green,blue));

    }

    public void add_Color(View view){
        //add Color to itemList + DB
        rgb = new double[] {i_red,i_green,i_blue,i_white,i_bright};
        itemList.add(rgb);
        dbHandler.addRGBcolor(rgb);
        //refresh list
        adapter = new ColorListAdapter(ColorActivity.this,R.layout.color_list, itemList);
        colorList.setAdapter(adapter);
    }

}
