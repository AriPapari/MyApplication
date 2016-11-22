package com.gecaj.arianit.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
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
        LayoutInflater buckyInflater = LayoutInflater.from(getContext());
        convertView = buckyInflater.inflate(R.layout.color_list,parent,false);

        int red = colorList.get(position)[0];
        int green = colorList.get(position)[1];
        int blue = colorList.get(position)[2];
        int white = colorList.get(position)[3];

        SurfaceView resultColor = (SurfaceView) convertView.findViewById(R.id.listColor);

        resultColor.setBackgroundColor(Color.rgb(red,green,blue));

        return convertView;
    }
}