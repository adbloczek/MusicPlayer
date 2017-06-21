package com.michal.galecki.player.music.musicplayer.model;

import java.io.Serializable;

public class Track implements Serializable
{
    private static final String UNDEFINED = "Undefined";
    private String path = UNDEFINED;
    private String track = UNDEFINED;
    private String title = UNDEFINED;
    private String album = UNDEFINED;
    private String artist = UNDEFINED;
    private String year = UNDEFINED;
    private long duration = 0;
    private String cover = null;

    public Track(String path)
    {
        this.path = path;
    }

    public String getTrack()
    {
        return track;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getCover()
    {
        return cover;
    }

    public void setCover(String cover)
    {
        this.cover = cover;
    }

    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }
}
