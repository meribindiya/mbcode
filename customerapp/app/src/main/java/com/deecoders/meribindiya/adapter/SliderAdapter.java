package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.model.SliderModel;
import com.deecoders.meribindiya.network.LruBitmapCache;
import com.deecoders.meribindiya.util.CustomNetworkImageView;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;
    Context context;
    List<SliderModel> models;

    public SliderAdapter(Context context, List<SliderModel> models) {
        this.context = context;
        this.models = models;
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
    }

    public void setData(List<SliderModel> models) {
        this.models = models;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new MyViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.image.setImageUrl(models.get(position).getHttppath(), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CustomNetworkImageView image;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            image = (CustomNetworkImageView) itemView.findViewById(R.id.image);
        }
    }
}
