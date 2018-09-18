package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.BookingDetails;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.BookingModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BookingModel> mList;
    private LayoutInflater mLayoutInflater = null;

    public BookingAdapter(Context context, ArrayList<BookingModel> list) {
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
            v = li.inflate(R.layout.booking_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final BookingModel model = mList.get(position);
        viewHolder.orderDate.setText("Order Date: "+model.getBookedat());
        viewHolder.orderId.setText("Order ID: "+model.getId());
        viewHolder.title.setText(model.getServices());
        viewHolder.price.setText(model.getTotal() + " INR");
        viewHolder.bookingDate.setText(model.getBookingdate());
        viewHolder.bookingTime.setText(model.getBooking_time());
        viewHolder.paymentMode.setText(model.getPymnt_source());
        viewHolder.orderStatus.setText(model.getOrderstatus());
        if (model.getOrderstatus().toLowerCase().contains(Constants.newOrder)) {
            viewHolder.imagePanel.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.confirm_icon);
        } else if (model.getOrderstatus().toLowerCase().contains(Constants.complete)) {
            viewHolder.imagePanel.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.complete_icon);
        } else if (model.getOrderstatus().toLowerCase().contains(Constants.cancel)) {
            viewHolder.imagePanel.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.cancel_icon);
        } else{
            viewHolder.imagePanel.setVisibility(View.VISIBLE);
            viewHolder.image.setImageResource(R.drawable.expert_icon);
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.clickEffect(v);
                Intent intent = new Intent(mContext, BookingDetails.class);
                Log.e("tag", "oid: " + model.getId());
                intent.putExtra("order_id", "" + model.getId());
                mContext.startActivity(intent);
            }
        });

        return v;
    }

    static class CompleteListViewHolder {
        @BindView(R.id.orderDate)
        TextView orderDate;
        @BindView(R.id.orderId)
        TextView orderId;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.bookingDate)
        TextView bookingDate;
        @BindView(R.id.bookingTime)
        TextView bookingTime;
        @BindView(R.id.paymentMode)
        TextView paymentMode;
        @BindView(R.id.orderStatus)
        TextView orderStatus;
        @BindView(R.id.serviceType)
        TextView serviceType;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.imagePanel)
        LinearLayout imagePanel;
        @BindView(R.id.card_view)
        CardView cardView;

        public CompleteListViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
