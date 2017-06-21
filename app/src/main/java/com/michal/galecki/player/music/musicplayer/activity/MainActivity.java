package com.michal.galecki.player.music.musicplayer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.michal.galecki.player.music.musicplayer.R;
import com.michal.galecki.player.music.musicplayer.adapter.MusicAdapter;
import com.michal.galecki.player.music.musicplayer.listener.RecyclerTouchListener;
import com.michal.galecki.player.music.musicplayer.model.MediaManager;
import com.michal.galecki.player.music.musicplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;


public class MainActivity extends Activity
{
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ArrayList<Track> songList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MusicAdapter sAdapter;
    private MediaManager mediaManager;
    public static final String SONG_NUMBER = "song_number";
    public static final String SONG_LIST = "song_list";

    private boolean checkAndRequestPermissions()
    {
        if (SDK_INT >= Build.VERSION_CODES.M)
        {
            int permissionStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionStorage != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (!listPermissionsNeeded.isEmpty())
            {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            } else
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAndRequestPermissions();
        loadMusic();
        setUpRecyclerView();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.about_menu:
                Intent intent = new Intent(this, AboutAuthorActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        sAdapter = new MusicAdapter(this, songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new MovieTouchlistener()));
    }

    private void loadMusic()
    {
        mediaManager = new MediaManager(this);
        songList =(ArrayList<Track>) mediaManager.getMp3Files();
    }

    private void startSongActivity(Context context, int position)
    {
        Intent i = new Intent(context, MusicActivity.class);
        i.putExtra(MainActivity.SONG_NUMBER, position);
        i.putExtra(MainActivity.SONG_LIST, songList);
        startActivity(i);
    }

    class MovieTouchlistener implements RecyclerTouchListener.ClickListener
    {
        @Override
        public void onClick(View view, int position)
        {
            startSongActivity(view.getContext(), position);//, songNumeber, songList);
        }

        @Override
        public void onLongClick(View view, int position)
        {
        }
    }
}
