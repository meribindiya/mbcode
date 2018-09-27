package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.model.EarningModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.EarningDetailsViewHolder>{

    private Context mContext;
    private List<EarningModel> myEarnings;
    private SimpleDateFormat formatter;


    public EarningAdapter(Context context, List<EarningModel> list) {
        mContext = context;
        myEarnings = list;
        formatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);
    }

    @NonNull
    @Override
    public EarningDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earning_details_item, parent, false);
        return new EarningDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningDetailsViewHolder viewHolder, int position) {
        final EarningModel model = myEarnings.get(position);
        viewHolder.amountTV.setText("\u20B9 " + model.getAmount());
        viewHolder.dateTV.setText(formatter.format(model.getCreatedat()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return myEarnings.size();
    }


    public class EarningDetailsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.dateTV)
        TextView dateTV;
        @BindView(R.id.amountTV)
        TextView amountTV;

        public EarningDetailsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


}