package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.model.TimeSlotModel;

import java.util.ArrayList;

public class TimeSlotAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TimeSlotModel> mList;
    private LayoutInflater mLayoutInflater = null;
    TimeSlotModel selectedModel;

    public TimeSlotModel getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(TimeSlotModel selectedModel) {
        this.selectedModel = selectedModel;
    }

    public TimeSlotAdapter(Context context, ArrayList<TimeSlotModel> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.time_slot_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        TimeSlotModel model = mList.get(position);

        // set views data here
        viewHolder.time.setText(model.getTime());
        if(model.isSelected()){
            selectedModel = model;
            viewHolder.time.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.fill_red));
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        else{
            viewHolder.time.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.border_gray));
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
        }
        viewHolder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TimeSlotModel timeSlotModel : mList) {
                    timeSlotModel.setSelected(false);
                }
                mList.get(position).setSelected(true);
                notifyDataSetChanged();
            }
        });

        return v;
    }

    static class CompleteListViewHolder {
        // declare views here
        TextView time;
        public CompleteListViewHolder(View base) {
            //initialize views here
            time=base.findViewById(R.id.time);
        }
    }
}