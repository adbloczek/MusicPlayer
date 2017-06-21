package com.michal.galecki.player.music.musicplayer.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.michal.galecki.player.music.musicplayer.R;
import com.michal.galecki.player.music.musicplayer.model.Track;

import java.lang.ref.WeakReference;

public class DecodeImageTask extends AsyncTask<Track, Void, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;
    public Track track;

    public DecodeImageTask(ImageView imageView)
    {
        this.imageViewReference = new WeakReference<>(imageView);
    }


    @Override
    protected Bitmap doInBackground(Track... params)
    {
        track = params[0];
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(track.getPath());
        byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
        Bitmap bitmap = null;
        if (embeddedPicture != null)
            bitmap = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (imageViewReference != null)
        {
            ImageView imageView = imageViewReference.get();
            if (bitmap == null)
                bitmap = BitmapFactory.decodeResource(imageView.getResources(), R.drawable.cover);
            imageView.setImageBitmap(bitmap);
        }
    }
}
