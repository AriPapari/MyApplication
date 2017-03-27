package com.gecaj.arianit.myapplication.effect;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gecaj.arianit.myapplication.R;
import com.gecaj.arianit.myapplication.colorpicker.ColorActivity;

import java.util.List;

/**
 * Created by Arianit on 05.12.2016.
 */

public class HexColorListAdapter extends ArrayAdapter {
    private List<String> hexColorList;
    private static final String LOG_TAG = ColorActivity.class.toString();
    private int ressource;
    private final ColorActivity adj = new ColorActivity();
    private double ar = adj.getColorAdjust()[0], ag = adj.getColorAdjust()[1], ab = adj.getColorAdjust()[2];
    public HexColorListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        hexColorList = objects;
        this.ressource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.adapter_color_list,parent,false);
        String hexColor = hexColorList.get(position);

        SurfaceView resultColor = (SurfaceView) convertView.findViewById(R.id.listColor);

        Log.i(LOG_TAG, ">>> HEXCOLOR STRING VALUE: "+hexColor);
        resultColor.setBackgroundColor(Color.rgb((int)(Integer.parseInt(hexColor.substring(0,2),16)*adj.getColorAdjust()[0]),
                (int)(Integer.parseInt(hexColor.substring(2,4),16)*adj.getColorAdjust()[1]),
                (int)(Integer.parseInt(hexColor.substring(4,6),16)*adj.getColorAdjust()[2])));

        return convertView;
    }
}
