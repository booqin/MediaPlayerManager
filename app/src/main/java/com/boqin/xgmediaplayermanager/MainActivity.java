package com.boqin.xgmediaplayermanager;

import com.boqin.bqmediaplayermananger.FullScreenActivity;
import com.boqin.bqmediaplayermananger.view.DefaultVideoPlayerView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String PATH = "http://paopao.nosdn.127.net/8d8adc73-eccf-4968-bfa3-e61f2040fbee.mp4";

    private DefaultVideoPlayerView mVideoPlayerView;

    private boolean isStartActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoPlayerView = (DefaultVideoPlayerView) findViewById(R.id.player);
        findViewById(R.id.star).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayerView.resume();
            }
        });
        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoPlayerView.pause();
            }
        });
        mVideoPlayerView.setPath(PATH);
        mVideoPlayerView.activate();
        mVideoPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartActivity = true;
                Intent intent = new Intent(v.getContext(), FullScreenActivity.class);
                intent.putExtra(FullScreenActivity.TAG, PATH);
                startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mVideoPlayerView.recover();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isStartActivity) {
            mVideoPlayerView.pause(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            mVideoPlayerView.activate();
            isStartActivity = false;
        }
    }
}
