package com.example.hellouniverse;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import java.io.IOException;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
/**
 * Created by omer on 4/4/15.
 */
public class Media extends android.app.Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Song> songs;
    //current position
    private int songPos;
    private final IBinder musicBind = new MusicBinder();
    public void onCreate(){
        super.onCreate();
        player = new MediaPlayer();
        songPos = 0;
        initMusicPlayer();
    }
    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setSongs(ArrayList<Song> songs){
        this.songs = songs;
    }
    public class MusicBinder extends Binder {
        Media getService() {
            return Media.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }
    public void playSong(){
        player.reset();
        Song s = songs.get(songPos);
        long sID = s.getID();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                sID);
        try
        {
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(IOException e){
            Log.e("Media", "Error setting data source");
        }
        player.prepareAsync();
    }

    public void setSong(int songIndex){
        songPos = songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
