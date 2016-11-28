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
import android.widget.TextView;

import java.util.List;
/**
 * Created by Arianit on 21.11.2016.
 */

public class ColorListAdapter extends ArrayAdapter{
    private List<double[]> colorList;
    private ColorActivity adj = new ColorActivity();
    private int ressource;
    public ColorListAdapter(Context context, int resource, List<double[]> objects) {
        super(context, resource, objects);
        colorList = objects;
        this.ressource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.color_list,parent,false);

        double red = colorList.get(position)[0]*colorList.get(position)[4];
        double green = colorList.get(position)[1]*colorList.get(position)[4];
        double blue = colorList.get(position)[2]*colorList.get(position)[4];
        double white = colorList.get(position)[3];
        if (white!=0){
            TextView whiteView = (TextView) convertView.findViewById(R.id.whiteView);
            whiteView.setText((int)(white/2.55)+"% WHITE");
            if(red+green+blue<=400)
                whiteView.setTextColor(Color.WHITE);
        }

        SurfaceView resultColor = (SurfaceView) convertView.findViewById(R.id.listColor);

        resultColor.setBackgroundColor(Color.rgb((int)(red*adj.getAdjustedColors()[0]),(int)(green*adj.getAdjustedColors()[1]),(int)(blue*adj.getAdjustedColors()[2])));

        return convertView;
    }
}