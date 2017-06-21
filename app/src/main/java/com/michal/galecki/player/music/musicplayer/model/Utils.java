package com.michal.galecki.player.music.musicplayer.model;

import java.util.Locale;

public class Utils
{
    public static String durationFromMillis(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return hours < 1 ? String.format(Locale.US, "%02d:%02d", minutes, seconds) : String.format(Locale.US,
                "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
