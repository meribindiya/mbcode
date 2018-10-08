package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyActiveMembersAdapter extends RecyclerView.Adapter<MyActiveMembersAdapter.MyActiveMembertViewHolder> {

    private Context mContext;
    private List<UserModel> mMySharedContacts;


    public MyActiveMembersAdapter(Context context, List<UserModel> list) {
        mContext = context;
        mMySharedContacts = list;
    }

    @NonNull
    @Override
    public MyActiveMembertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_active_members_item, parent, false);
        return new MyActiveMembertViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyActiveMembertViewHolder viewHolder, int position) {
        final UserModel model = mMySharedContacts.get(position);
        viewHolder.contactName.setText(model.getName());
        viewHolder.contactNumber.setText(model.getMobile()+"");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMySharedContacts.size();
    }

    public class MyActiveMembertViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.contactName)
        TextView contactName;
        @BindView(R.id.contactNumber)
        TextView contactNumber;

        public MyActiveMembertViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


}
