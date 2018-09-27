package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Category;
import com.deecoders.meribindiya.model.CategoryModel;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.network.LruBitmapCache;
import com.deecoders.meribindiya.util.CircularNetworkImageView;
import com.deecoders.meribindiya.util.CustomNetworkImageView;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CategoryModel> mList;
    private LayoutInflater mLayoutInflater = null;
    ImageLoader mImageLoader;
    RequestQueue mRequestQueue;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache());
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int pos) {
        return mList.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.category_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final CategoryModel model = mList.get(position);
        viewHolder.title.setText(model.getName());
        Typeface type = Typeface.createFromAsset(mContext.getAssets(),"font1.ttf");
        viewHolder.title.setTypeface(type);

        viewHolder.imageView.setImageUrl(model.getImage(), mImageLoader);
        viewHolder.imageView.setColorFilter(applyLightness(-30));

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                Intent intent = new Intent(mContext, Category.class);
                intent.putExtra("model", model);
                intent.putExtra("all_models", mList);
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    static class CompleteListViewHolder {
        public TextView title;
        public NetworkImageView imageView;
        public ImageView about;
        public CompleteListViewHolder(View base) {
            title = (TextView) base.findViewById(R.id.title);
            imageView=base.findViewById(R.id.image);
            about=base.findViewById(R.id.about);
        }
    }

    public static PorterDuffColorFilter applyLightness(int progress)
    {
        if (progress > 0)
        {
            int value = (int) progress * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        }
        else
        {
            int value = (int) (progress * -1) * 255 / 100;
            return new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
