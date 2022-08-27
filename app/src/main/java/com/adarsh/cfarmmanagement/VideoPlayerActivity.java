package com.adarsh.cfarmmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.khizar1556.mkvideoplayer.MKPlayer;

public class VideoPlayerActivity extends AppCompatActivity {
    MKPlayer mkplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mkplayer = new MKPlayer(VideoPlayerActivity.this);
        mkplayer.play(getIntent().getStringExtra("videoUrl"));
        mkplayer.onComplete(this::onBackPressed);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mkplayer.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}