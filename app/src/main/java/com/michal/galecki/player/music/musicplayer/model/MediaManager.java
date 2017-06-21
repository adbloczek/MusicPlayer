package com.michal.galecki.player.music.musicplayer.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class MediaManager
{
    private Context context;
    private List<Track> mp3Files = null;

    public MediaManager(Context context)
    {
        this.context = context;
    }

    public List<Track> getMp3Files()
    {
        if (mp3Files == null)
            fetchMp3Files();
        return mp3Files;
    }

    private void fetchMp3Files()
    {
        mp3Files = new ArrayList<>();
        Uri externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media
                .ALBUM, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DURATION};
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.ARTIST + ", " + MediaStore.Audio.Media.ALBUM + ", " + MediaStore
                .Audio.Media.TRACK + " ASC";
        Cursor cursor = context.getContentResolver().query(externalContentUri, projection, selection, null, sortOrder);
        if (cursor != null && cursor.getCount() > 0)
            while (cursor.moveToNext())
            {
                String absPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                Track track = new Track(absPath);
                track.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                track.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                track.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                track.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
                track.setTrack(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK)));
                track.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                mp3Files.add(track);
            }
        if (cursor != null)
            cursor.close();
    }
}
