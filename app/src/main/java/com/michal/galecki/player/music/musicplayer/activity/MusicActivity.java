package com.michal.galecki.player.music.musicplayer.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.michal.galecki.player.music.musicplayer.R;
import com.michal.galecki.player.music.musicplayer.helpers.ImageDecoder;
import com.michal.galecki.player.music.musicplayer.model.Track;
import com.michal.galecki.player.music.musicplayer.model.Utils;

public class MusicActivity extends Activity implements View.OnClickListener
{
    @BindView(R.id.sartist)
    protected TextView artistTV;
    @BindView(R.id.salbum)
    protected TextView albumTV;
    @BindView(R.id.stitle)
    protected TextView titleTV;
    @BindView(R.id.send)
    protected TextView endTV;
    @BindView(R.id.snow)
    protected TextView nowTV;
    @BindView(R.id.scover)
    protected ImageView coverIV;
    @BindView(R.id.button_playPause)
    protected Button playButton;
    @BindView(R.id.button_forward)
    protected Button forwardButton;
    @BindView(R.id.button_backward)
    protected Button buttonBackward;
    @BindView(R.id.button_next)
    protected Button nextButton;
    @BindView(R.id.button_previous)
    protected Button previousButton;
    @BindView(R.id.seekBar)
    protected SeekBar seekBar;

    private String title;
    private String artist;
    private String album;
    private long duration;
    private String path;
    private MediaPlayer mediaPlayer;
    private int BUTTON_SEEK_MILLISECONDS = 10000;
    private Handler handler;
    private Vibrator vibrator;
    private Runnable viewUpdater = new Runnable()
    {
        @Override
        public void run()
        {
            int duration = mediaPlayer.getDuration();
            int currentDuration = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currentDuration * seekBar.getMax() / duration);
            nowTV.setText(Utils.durationFromMillis(currentDuration));
            performViewUpdates();
        }
    };

    private static int songNumeber;
    private ArrayList<Track> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        readAndSetDataFromArraylist();
        initializeMediaPlayer();
        handler = new Handler();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setOnCLick();
        playMusic();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        mediaPlayer.pause();
    }

    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacks(viewUpdater);
    }

    private void readAndSetDataFromArraylist()
    {
        Intent intent = getIntent();
        songList = (ArrayList) intent.getSerializableExtra(MainActivity.SONG_LIST);
        songNumeber = intent.getIntExtra(MainActivity.SONG_NUMBER, 0);
        title = songList.get(songNumeber).getTitle();
        artist = songList.get(songNumeber).getArtist();
        album = songList.get(songNumeber).getAlbum();
        duration = songList.get(songNumeber).getDuration();
        path = songList.get(songNumeber).getPath();

        titleTV.setText(title);
        artistTV.setText(artist);
        albumTV.setText(album);
        endTV.setText(Utils.durationFromMillis(duration));
        Track track = new Track(path);
        new ImageDecoder().decode(track, coverIV);
    }

    private void initializeMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        try
        {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e)
        {
            Toast.makeText(this, R.string.could_not_load_file, Toast.LENGTH_SHORT).show();
        }
    }

    private void setOnCLick()
    {
        playButton.setOnClickListener(this);
        forwardButton.setOnClickListener(this);
        buttonBackward.setOnClickListener(this);
        forwardButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    private void playPauseMusic()
    {
        if (mediaPlayer.isPlaying())
            pauseMusic();
        else
            playMusic();
    }

    private void playMusic()
    {
        mediaPlayer.start();
        playButton.setText("||");
        performViewUpdates();
    }

    private void pauseMusic()
    {
        mediaPlayer.pause();
        playButton.setText(">");
    }

    private void seekAudio(int milliseconds)
    {
        if (mediaPlayer.getDuration() > milliseconds && milliseconds > 0)
            mediaPlayer.seekTo(milliseconds);
    }

    private void seekAudioFromCurrentPosition(int milliseconds)
    {

        if (0 > mediaPlayer.getCurrentPosition() + milliseconds)
            mediaPlayer.seekTo(0);
        else if (mediaPlayer.getCurrentPosition() + milliseconds > mediaPlayer.getDuration())
        {
            mediaPlayer.pause();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
        } else
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + milliseconds);
        performViewUpdates();
    }

    private void performViewUpdates()
    {
        handler.postDelayed(viewUpdater, 100);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button_playPause:
                playPauseMusic();
                break;
            case R.id.button_forward:
                seekAudioFromCurrentPosition(BUTTON_SEEK_MILLISECONDS);
                break;
            case R.id.button_backward:
                seekAudioFromCurrentPosition(-BUTTON_SEEK_MILLISECONDS);
                break;
            case R.id.button_next:
                if(songNumeber+1 < songList.size()) {
                    songNumeber++;
                } else {
                    songNumeber = 0;
                }
                startSongActivity(this);
                break;
            case R.id.button_previous:
                if(songNumeber > 0) {
                    songNumeber--;
                } else {
                    songNumeber = songList.size() - 1;
                }
                startSongActivity(this);
                break;
            default:
                break;
        }
    }

    private void startSongActivity(Context context)
    {
        Intent i = new Intent(context, MusicActivity.class);
        i.putExtra(MainActivity.SONG_NUMBER, songNumeber);
        i.putExtra(MainActivity.SONG_LIST, songList);
        startActivity(i);
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
            handler.removeCallbacks(viewUpdater);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            handler.removeCallbacks(viewUpdater);
            if (seekBar != null)
                seekAudio(seekBar.getProgress() * mediaPlayer.getDuration() / seekBar.getMax());
            performViewUpdates();
        }
    }

    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }

}
