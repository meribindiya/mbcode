package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.model.NotificationModel;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NotificationModel> mList;
    private LayoutInflater mLayoutInflater = null;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            v = li.inflate(R.layout.notification_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        NotificationModel model = mList.get(position);
        //viewHolder.title.setText(model.getTitle());

        return v;
    }

    static class CompleteListViewHolder {
        //public TextView title;
        public CompleteListViewHolder(View base) {
            //title = (TextView) base.findViewById(R.id.txt);
        }
    }
}
