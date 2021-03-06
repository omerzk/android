package com.example.hellouniverse;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Created by omer on 4/3/15.
 */
public class SongMap extends BaseAdapter {
    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongMap(Context c, ArrayList<Song> songs){
        this.songs = songs;
        this.songInf = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLay = (LinearLayout)songInf.inflate(R.layout.song, parent, false);
        TextView titleView =  (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        Song currSong = songs.get(position);
        titleView.setText(currSong.getName());
        artistView.setText(currSong.getArtist());
        songLay.setTag(position);
        return songLay;
    }
}
