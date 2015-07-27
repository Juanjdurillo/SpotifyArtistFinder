package com.example.android.spotifyartistfinder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by JuanJose on 6/20/2015.
 */
public class TopTracksArrayAdapter extends ArrayAdapter<TrackItem> {
    Context context;

    public TopTracksArrayAdapter(Context context, int resourceId, ArrayList<TrackItem> objects) {
        super(context, resourceId, objects);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView   image;
        TextView    name;
        TextView    album;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TrackItem trackItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.advanced_track_item, null);
            holder = new ViewHolder();
            holder.name =  (TextView) convertView.findViewById(R.id.track_song_name);
            holder.album = (TextView) convertView.findViewById(R.id.track_album_name);
            holder.image = (ImageView) convertView.findViewById(R.id.track_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.name.setText(trackItem.getName());
        holder.album.setText(trackItem.getAlbum());
        if (trackItem.getImage()!=null) {
            Picasso.with(this.context).load(trackItem.getImage().url).into(holder.image);
            holder.image.setVisibility(ImageView.VISIBLE);
        } else
            holder.image.setVisibility(ImageView.INVISIBLE); //needed because the adapter reuses the view
        return convertView;
    }
}