package com.example.hellouniverse;

/**
 * Created by omer on 4/3/15.
 */
public class Song {
    private String name, artist;
    private long id;

    Song(String name, String artist, long id)
    {
        this.name = name;
        this.artist = artist;
        this.id = id;
    }

    public long getID(){return id;}
    public String getName(){return name;}
    public String getArtist(){return artist;}
}

