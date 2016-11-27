package com.gecaj.arianit.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
/**
 * Created by Arianit on 21.11.2016.
 */

public class ColorListAdapter extends ArrayAdapter{
    private List<int[]> colorList;
    private int ressource;
    public ColorListAdapter(Context context, int resource, List<int[]> objects) {
        super(context, resource, objects);
        colorList = objects;
        this.ressource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.color_list,parent,false);

        int red = colorList.get(position)[0];
        int green = colorList.get(position)[1];
        int blue = colorList.get(position)[2];
        int white = colorList.get(position)[3];

        SurfaceView resultColor = (SurfaceView) convertView.findViewById(R.id.listColor);
        //SurfaceView transparency
        resultColor.setZOrderOnTop(true);    // necessary
        SurfaceHolder sfhTrackHolder = resultColor.getHolder();
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT);

        resultColor.setBackgroundColor(Color.argb(255-white,(int)(red*0.3),(int)(green*0.6),(int)(blue*0.1)));

        return convertView;
    }
}