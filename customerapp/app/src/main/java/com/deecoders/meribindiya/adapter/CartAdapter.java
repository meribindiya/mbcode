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

public class CartAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductModel> mList;
    private LayoutInflater mLayoutInflater = null;

    public CartAdapter(Context context, ArrayList<ProductModel> list) {
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

        DrawableCompat.setTint(viewHolder.minus.getDrawable(), ContextCompat.getColor(mContext, R.color.white));
        DrawableCompat.setTint(viewHolder.plus.getDrawable(), ContextCompat.getColor(mContext, R.color.white));

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                mList.get(position).setShowCount(true);
                int count = mList.get(position).getCount();
                count++;
                mList.get(position).setCount(count);
                notifyDataSetChanged();
                addProductToCart(mContext, mList.get(position));
            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                int count = mList.get(position).getCount();
                count++;
                mList.get(position).setCount(count);
                notifyDataSetChanged();
                addProductToCart(mContext, mList.get(position));
            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                int count = mList.get(position).getCount();
                count--;
                if(count < 0) {
                    count = 0;
                    mList.get(position).setShowCount(false);
                }
                mList.get(position).setCount(count);
                notifyDataSetChanged();
                addProductToCart(mContext, mList.get(position));
            }
        });

        if(model.getCount() > 0){
            viewHolder.addPanel.setVisibility(View.GONE);
            viewHolder.plusMinusPanel.setVisibility(View.VISIBLE);
            viewHolder.count.setText(""+model.getCount());
        }
        else{
            viewHolder.addPanel.setVisibility(View.VISIBLE);
            viewHolder.plusMinusPanel.setVisibility(View.GONE);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                model.setCount(0);
                updateProductToCart(mContext, model);
                mList.remove(model);
                notifyDataSetChanged();
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nothing
            }
        });

        return v;
    }

    private void addProductToCart(Context context, ProductModel productModel) {
        Log.e("tag", "addProductToCart");
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        // check if already added
        for (ProductModel model : models) {
            if(model.getId() == productModel.getId()){
                updateProductToCart(context, productModel);
                return;
            }
        }
        models.add(productModel);
        tinyDB.putListObject("products", models);
        context.sendBroadcast(new Intent("cart"));
    }

    private void updateProductToCart(Context context, ProductModel productModel) {
        Log.e("tag", "updateProductToCart");
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        for (int i = 0; i < models.size(); i++) {
            if(models.get(i).getId() == productModel.getId()){
                if(productModel.getCount() > 0) {
                    models.get(i).setCount(productModel.getCount());
                    models.get(i).setScheduleDate(productModel.getScheduleDate());
                }
                else {
                    models.remove(i);
                }
            }
        }
        tinyDB.putListObject("products", models);
        context.sendBroadcast(new Intent("cart"));
    }

    static class CompleteListViewHolder {
        public TextView name, itemPrice;
        public ViewGroup addPanel, plusMinusPanel;
        public TextView count;
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
        }
    }

    private void showDatePicker(final Context context, final ProductModel productModel){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String date = new SimpleDateFormat(myFormat).format(calendar.getTimeInMillis());
                showTimePicker(context, productModel, date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void showTimePicker(final Context context, final ProductModel productModel, final String date){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM ;
                if(selectedHour < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }
                String time = selectedHour + ":" + selectedMinute + " " + AM_PM;
                productModel.setScheduleDate(date+" "+time);
                for (int i = 0; i < mList.size(); i++) {
                    if(productModel.getId() == mList.get(i).getId()){
                        mList.set(i, productModel);
                    }
                }
                notifyDataSetChanged();
                updateProductToCart(context, productModel);
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
