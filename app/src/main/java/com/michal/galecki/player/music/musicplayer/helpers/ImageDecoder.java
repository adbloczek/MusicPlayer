package com.michal.galecki.player.music.musicplayer.helpers;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import com.michal.galecki.player.music.musicplayer.model.Track;

public class ImageDecoder
{
    public void decode(Track track, ImageView imageView)
    {
        if (cancelPotentialDownload(track, imageView))
        {
            DecodeImageTask task = new DecodeImageTask(imageView);
            DecodedDrawable downloadedDrawable = new DecodedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,track);
        }
    }

    private static boolean cancelPotentialDownload(Track track, ImageView imageView)
    {
        DecodeImageTask decodeImageTask = getDecodeImageTask(imageView);
        if (decodeImageTask != null)
        {
            Track otherMp3File = decodeImageTask.track;
            if ((otherMp3File == null) || (!otherMp3File.equals(track)))
                decodeImageTask.cancel(true);
            else
                return false;
        }
        return true;
    }

    private static DecodeImageTask getDecodeImageTask(ImageView imageView)
    {
        if (imageView != null)
        {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DecodedDrawable)
            {
                DecodedDrawable downloadedDrawable = (DecodedDrawable) drawable;
                return downloadedDrawable.getImageDecodeTask();
            }
        }
        return null;
    }

    static class DecodedDrawable extends ColorDrawable
    {
        private final WeakReference<DecodeImageTask> imageDecodeTaskReference;

        public DecodedDrawable(DecodeImageTask imageDecodeTask)
        {
            super(Color.BLACK);
            imageDecodeTaskReference = new WeakReference<>(imageDecodeTask);
        }

        public DecodeImageTask getImageDecodeTask()
        {
            return imageDecodeTaskReference.get();
        }
    }
}

