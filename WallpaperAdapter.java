package com.jackapps.wallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Jack on 2/28/2017.
 */
public class WallpaperAdapter extends ArrayAdapter<Wallpaper>{

    private Context context;

    public static class ViewHolder {
        //TextView myText;
        ImageView myWallpaper;
    }

    public WallpaperAdapter(Context context, List<Wallpaper> mWallpaper) {
        super(context, -1, mWallpaper);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        Wallpaper id = getItem(position);
        ViewHolder viewHolder;
        View convertView = convertview;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,parent,false);
            viewHolder.myWallpaper = (ImageView) convertView.findViewById(R.id.mWallpaper);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            Glide
                    .with(context)
                    .load(id.getwebformatURL())
                    .asBitmap()
                    .placeholder(R.drawable.loading)
                    .into(viewHolder.myWallpaper);
        }
        catch (Exception e){}
       // viewHolder.myText.setText(id.getHashId());
        return convertView;
    }



}
