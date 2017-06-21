package com.michal.galecki.player.music.musicplayer.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import com.michal.galecki.player.music.musicplayer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutAuthorActivity extends Activity {
    @BindView(R.id.about_version)
    TextView textViewVersion;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_author);
        ButterKnife.bind(this);
        textViewVersion.setText(getString(R.string.version));
    }
}
