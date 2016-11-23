package com.gecaj.arianit.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Arianit on 21.11.2016.
 */

public class JSONTask extends AsyncTask<String, String, String>{
    private static final String LOG_TAG = ColorActivity.class.toString();
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        try {
            Log.i(LOG_TAG, "######## D0 ######## "+strings[0]);
            URL url = new URL(strings[0]);
            Log.i(LOG_TAG, "######## D1 ########");
            connection = (HttpURLConnection) url.openConnection();
            Log.i(LOG_TAG, "######## D2 ######## " + connection.toString());
            connection.connect();
            Log.i(LOG_TAG, "######## CONNECTED ########");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "######## E1 ########");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(LOG_TAG, "######## E2 ########");
        }
        return null;
    }

}

