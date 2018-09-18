package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.model.ProductModel;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.util.TinyDB;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductModel> mList;
    private LayoutInflater mLayoutInflater = null;

    public ProductAdapter(Context context, ArrayList<ProductModel> list) {
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
            v = li.inflate(R.layout.product_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final ProductModel model = mList.get(position);

        viewHolder.title.setText(model.getName());
        viewHolder.time.setText(model.getTime()+" min");
        viewHolder.price.setText(model.getPrice()+" INR");

        DrawableCompat.setTint(viewHolder.minus.getDrawable(), ContextCompat.getColor(mContext, R.color.white));
        DrawableCompat.setTint(viewHolder.plus.getDrawable(), ContextCompat.getColor(mContext, R.color.white));

        viewHolder.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                showAlert(mContext, model);
            }
        });

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

        if(model.isShowCount()){
            viewHolder.addPanel.setVisibility(View.GONE);
            viewHolder.plusMinusPanel.setVisibility(View.VISIBLE);
            viewHolder.count.setText(""+model.getCount());
        }
        else{
            viewHolder.addPanel.setVisibility(View.VISIBLE);
            viewHolder.plusMinusPanel.setVisibility(View.GONE);
        }
        return v;
    }

    private void addProductToCart(Context context, ProductModel productModel) {
        Log.e("tag", "addProductToCart");
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        // check if already added
        for (ProductModel model : models) {
            if(model.getId() == productModel.getId()){
                updateProductCountToCart(context, productModel);
                return;
            }
        }
        models.add(productModel);
        tinyDB.putListObject("products", models);
        context.sendBroadcast(new Intent("cart"));
    }

    private void updateProductCountToCart(Context context, ProductModel productModel) {
        Log.e("tag", "updateProductCountToCart");
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ProductModel> models = tinyDB.getListObject("products", ProductModel.class);
        for (int i = 0; i < models.size(); i++) {
            if(models.get(i).getId() == productModel.getId()){
                if(productModel.getCount() > 0)
                    models.get(i).setCount(productModel.getCount());
                else
                    models.remove(i);
            }
        }
        tinyDB.putListObject("products", models);
        context.sendBroadcast(new Intent("cart"));
    }

    static class CompleteListViewHolder {
        public TextView title, time, price;
        public TextView about;
        public ViewGroup addPanel, plusMinusPanel;
        public TextView count;
        public Button add;
        public ImageView plus, minus;
        public CompleteListViewHolder(View base) {
            title = (TextView) base.findViewById(R.id.name);
            time = (TextView) base.findViewById(R.id.time);
            price = (TextView) base.findViewById(R.id.price);
            about=base.findViewById(R.id.about);
            addPanel=base.findViewById(R.id.addPanel);
            plusMinusPanel=base.findViewById(R.id.plusMinusPanel);
            count=base.findViewById(R.id.count);
            add=base.findViewById(R.id.add);
            plus=base.findViewById(R.id.plus);
            minus=base.findViewById(R.id.minus);
        }
    }

    private void showAlert(Context context, ProductModel model){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(model.getName())
        .setMessage(model.getShortdesc())
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .show();
    }

    private String getTimeFromSecond (long seconds) {
        if (seconds < 60) {
            return seconds + "sec";
        } else if (seconds < 3600) {
            return "" + (seconds / 60) + "min of service";
        } else if (seconds < 86400) {
            return "" + (seconds / 3600) + "H of service";
        } else if (seconds < 604800) {
            return "" + (seconds / 86400) + "D of service";
        } else if (seconds < 2419200){
            return "" + (seconds / 604800) + "W of service";
        } else if(seconds < 29030400){
            return "" + (seconds / 2419200) + "M of service";
        } else {
            return "" + (seconds / 29030400) + "Y of service";
        }
    }
}