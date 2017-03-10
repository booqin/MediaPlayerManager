package com.boqin.xgmediaplayermanager;

import com.boqin.bqmediaplayermananger.view.DefaultVideoPlayerView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DefaultVideoPlayerView mVideoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoPlayerView = (DefaultVideoPlayerView) findViewById(R.id.player);
        mVideoPlayerView.setPath("http://paopao.nosdn.127.net/8d8adc73-eccf-4968-bfa3-e61f2040fbee.mp4");
        mVideoPlayerView.activate();
    }
}
