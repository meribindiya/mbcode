package com.deecoders.meribindiya.adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.util.TinyDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductModel> mList;
    private LayoutInflater mLayoutInflater = null;

    public OrderAdapter(Context context, ArrayList<ProductModel> list) {
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
            v = li.inflate(R.layout.cart_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final ProductModel model = mList.get(position);
        viewHolder.name.setText(model.getName());
        viewHolder.itemPrice.setText(model.getPrice()+" INR");
        viewHolder.buttonPanel.setVisibility(View.GONE);
        viewHolder.quantity.setVisibility(View.VISIBLE);
        viewHolder.quantity.setText(model.getCount()+"X");
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing
            }
        });
        return v;
    }

    static class CompleteListViewHolder {
        public TextView name, itemPrice;
        public ViewGroup addPanel, plusMinusPanel, buttonPanel;
        public TextView count, quantity;
        public Button add;
        public ImageView plus, minus, delete;
        public CompleteListViewHolder(View base) {
            addPanel=base.findViewById(R.id.addPanel);
            plusMinusPanel=base.findViewById(R.id.plusMinusPanel);
            count=base.findViewById(R.id.count);
            add=base.findViewById(R.id.add);
            plus=base.findViewById(R.id.plus);
            minus=base.findViewById(R.id.minus);
            name=base.findViewById(R.id.name);
            itemPrice =base.findViewById(R.id.itemPrice);
            delete =base.findViewById(R.id.delete);
            buttonPanel=base.findViewById(R.id.buttonPanel);
            quantity=base.findViewById(R.id.quantity);
        }
    }
}
