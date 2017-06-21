package com.michal.galecki.player.music.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.michal.galecki.player.music.musicplayer.R;
import com.michal.galecki.player.music.musicplayer.helpers.ImageDecoder;
import com.michal.galecki.player.music.musicplayer.model.Track;
import com.michal.galecki.player.music.musicplayer.model.Utils;

public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    private List<Track> songList;

    public MusicAdapter(Context context, List<Track> songList)
    {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.media_list_row, parent, false);
        return new SongHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Track song = songList.get(position);
        SongHolder sholder = (SongHolder) holder;
        sholder.title.setText(song.getTitle());
        sholder.artist.setText(song.getArtist());
        sholder.album.setText(song.getAlbum() + " (" + song.getYear() + ")");
        sholder.duration.setText(Utils.durationFromMillis(song.getDuration()));
        new ImageDecoder().decode(song,sholder.image);

    }

    public Track getSong(int position)
    {
        return songList.get(position);
    }

    @Override
    public int getItemCount()
    {
        return songList.size();
    }

    private class SongHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView artist;
        TextView album;
        TextView duration;
        ImageView image;

        SongHolder(View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);
            album = (TextView) itemView.findViewById(R.id.album);
            duration = (TextView) itemView.findViewById(R.id.duration);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
