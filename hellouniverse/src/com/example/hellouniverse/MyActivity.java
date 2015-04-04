package com.example.hellouniverse;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ImageButton;
import android.widget.ListView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Media mediaService;
    private Intent intnt;
    private boolean musicbound = false;
    private ArrayList<Song> songList;
    private ListView songView;
    public static int singelton = 0;
    ImageButton exit, shuf;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if((singelton += 1) > 1)return;
        songView = (ListView)findViewById(R.id.listView);
        songList = new ArrayList<Song>();
        getSongList();
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getName().compareTo(b.getName());
            }
        });
        SongMap sM = new SongMap(this, songList);
        songView.setAdapter(sM);
        exit = (ImageButton)findViewById(R.id.end);
        shuf = (ImageButton)findViewById(R.id.suffle);
        addListenerOnButton();


    }
    public void onStart(){
        super.onStart();
        if(intnt == null){
            intnt = new Intent(this, Media.class);
            bindService(intnt, conn, Context.BIND_AUTO_CREATE);
            startService(intnt);
        }
    }
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Media.MusicBinder binder = (Media.MusicBinder)service;
            //get service
            mediaService = binder.getService();
            //pass list
            mediaService.setSongs(songList);
            musicbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicbound = false;
        }
        };

    private void getSongList() {
        ContentResolver musicResolve = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolve.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst())
        {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisName = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisName, thisArtist,thisId));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();

    }
    public void songPicked(View view){
        mediaService.setSong(Integer.parseInt(view.getTag().toString()));
        mediaService.playSong();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.suffle:
                break;
            case R.id.end:
                stopService(intnt);
                mediaService = null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        stopService(intnt);
        mediaService = null;
        super.onDestroy();
    }

    public void addListenerOnButton() {

        exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stopService(intnt);
                mediaService = null;
                System.exit(0);
            }

        });
    }

}
