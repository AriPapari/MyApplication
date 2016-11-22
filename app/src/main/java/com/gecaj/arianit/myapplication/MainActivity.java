package com.gecaj.arianit.myapplication;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.toString();
    private WebView myWebView;
    private SeekBar red, green, blue, white;
    private int i_red = 0, i_green = 0, i_blue = 0, i_white = 0;
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
        Cursor result = dbHandler.getAllData();
        myWebView  = (WebView) findViewById(R.id.webview);
        red = (SeekBar) findViewById(R.id.red);
        green = (SeekBar) findViewById(R.id.green);
        blue = (SeekBar) findViewById(R.id.blue);
        white = (SeekBar) findViewById(R.id.white);
        resultColor =  (SurfaceView) findViewById(R.id.listColor);
        colorList = (ListView) findViewById(R.id.colorList);
        itemList = new ArrayList<>();

        //reading from DB
        if(result.getCount() != 0){
            while(result.moveToNext()){
                itemList.add(new int[]{result.getInt(1),result.getInt(2),result.getInt(3),result.getInt(4)});
            }
        }
        adapter = new ColorListAdapter(MainActivity.this,R.layout.color_list, itemList);
        colorList.setAdapter(adapter);

        //SeekBar ChangeListeners
        red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_red = i;
                resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                green.setProgress(i_green); blue.setProgress(i_blue); white.setProgress(i_white);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);

            }
        });
        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_green = i;
                resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                red.setProgress(i_red); blue.setProgress(i_blue); white.setProgress(i_white);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);

            }
        });
        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_blue = i;
                resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                red.setProgress(i_red); green.setProgress(i_green); white.setProgress(i_white);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);

            }
        });
        white.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                i_white = i;
                resultColor.setBackgroundColor(Color.rgb(i_red,i_green,i_blue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                red.setProgress(i_red); green.setProgress(i_green); blue.setProgress(i_blue);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);

            }
        });

    }

    public void reset_LED(View view){
        Log.i(LOG_TAG, "######## BUTTON PRESSED ########");
        myWebView.loadUrl("http://raspberrypi/reset.php/");
        int pos = 0;
        itemList.remove(pos);
        colorList.setAdapter(adapter);
        dbHandler.deleteData();
        //tabelle neu füllen
        for(int i = 0; i < itemList.size(); i++)
            dbHandler.addColor(itemList.get(i));
        //new JSONTask().execute("http://raspberrypi/reset.php/");
    }

    public void add_Color(View view){
        rgb = new int[] {i_red,i_green,i_blue,i_white};
        itemList.add(rgb);                                                                          //add Object to itemList
        dbHandler.addColor(rgb);
        adapter = new ColorListAdapter(MainActivity.this,R.layout.color_list, itemList);
        colorList.setAdapter(adapter);
        colorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i_red = itemList.get(i)[0];
                i_green = itemList.get(i)[1];
                i_blue = itemList.get(i)[2];
                i_white = itemList.get(i)[3];
                myWebView.loadUrl("http://raspberrypi/LED_OTF.php/?red="+i_red+"&green="+i_green+"&blue="+i_blue+"&white="+i_white);
                red.setProgress(i_red); green.setProgress(i_green); blue.setProgress(i_blue); white.setProgress(i_white);
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
}
