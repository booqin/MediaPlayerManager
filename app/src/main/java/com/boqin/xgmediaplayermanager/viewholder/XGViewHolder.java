package com.boqin.xgmediaplayermanager.viewholder;

import com.boqin.bqmediaplayermananger.view.IPlayerAttacher;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by Boqin on 2017/3/10.
 * Modified by Boqin
 *
 * @Version
 */
public class XGViewHolder extends RecyclerView.ViewHolder implements IPlayerAttacher{


    public XGViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onResume(String holderKey, boolean isOnPauseWhenPlaying) {

    }

    @Override
    public boolean onPause(String holderKey) {
        return false;
    }

    @Override
    public boolean isNeedPlayNow() {
        return false;
    }

    @Override
    public void release() {

    }

    @Override
    public String getPlayerKey() {
        return null;
    }
}
