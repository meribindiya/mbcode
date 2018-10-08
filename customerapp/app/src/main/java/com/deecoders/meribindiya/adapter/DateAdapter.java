package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.SelectDateTime;
import com.deecoders.meribindiya.model.DateModel;

import java.util.ArrayList;

public class DateAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DateModel> mList;
    private LayoutInflater mLayoutInflater = null;
    public DateModel selectedModel;

    public DateAdapter(Context context, ArrayList<DateModel> list) {
        if(list.size()>0)
            selectedModel=list.get(0);
        mContext = context;
        mList = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void reset(){
        for (DateModel dateModel : mList) {
            dateModel.setSelected(false);
        }
        selectedModel = null;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.date_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final DateModel model = mList.get(position);

        // set views data here
        viewHolder.dateTxt.setText(model.getDate());
        viewHolder.dayTxt.setText(model.getDay());
        if(model.isSelected()){
            selectedModel = model;
            viewHolder.image.setVisibility(View.VISIBLE);
            viewHolder.dateTxt.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        else{
            viewHolder.image.setVisibility(View.GONE);
            viewHolder.dateTxt.setTextColor(mContext.getResources().getColor(R.color.text));
            viewHolder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        viewHolder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DateModel dateModel : mList) {
                    dateModel.setSelected(false);
                }
                mList.get(position).setSelected(true);
                selectedModel = mList.get(position);
                notifyDataSetChanged();
                SelectDateTime selectDateTime=(SelectDateTime)mContext;
                selectDateTime.setTimeSlots();
            }
        });

        return v;
    }

    static class CompleteListViewHolder {
        // declare views here
        public ViewGroup panel;
        public TextView dateTxt, dayTxt;
        public ImageView image;
        public CompleteListViewHolder(View base) {
            //initialize views here
            panel=base.findViewById(R.id.panel);
            dateTxt=base.findViewById(R.id.date);
            dayTxt=base.findViewById(R.id.day);
            image=base.findViewById(R.id.image);
        }
    }
}