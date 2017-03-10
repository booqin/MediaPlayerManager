package com.boqin.xgmediaplayermanager.adapter;

import com.boqin.xgmediaplayermanager.R;
import com.boqin.xgmediaplayermanager.viewholder.XGViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * TODO
 * Created by Boqin on 2017/3/10.
 * Modified by Boqin
 *
 * @Version
 */
public class XGAdapter extends RecyclerView.Adapter{

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_video, parent, false);
        return new XGViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
