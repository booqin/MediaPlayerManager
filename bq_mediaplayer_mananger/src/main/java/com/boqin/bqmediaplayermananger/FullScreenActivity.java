package com.boqin.bqmediaplayermananger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.boqin.bqmediaplayermananger.view.DefaultVideoPlayerView;

/**
 * 全屏显示
 * Created by vitozhang on 2018/6/15.
 */

public class FullScreenActivity extends Activity{

    public static final String TAG = "PATH_KEY";
    private DefaultVideoPlayerView mVideoPlayerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = getIntent().getStringExtra(TAG);
        setContentView(R.layout.activity_full_screen);
        mVideoPlayerView = findViewById(R.id.player);
        mVideoPlayerView.setPath(path);
        mVideoPlayerView.activate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
