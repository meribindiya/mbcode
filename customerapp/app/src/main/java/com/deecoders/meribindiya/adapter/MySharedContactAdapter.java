package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Home;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.UserModel;
import com.deecoders.meribindiya.util.MyPref;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySharedContactAdapter extends RecyclerView.Adapter<MySharedContactAdapter.MySharedContactViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<UserModel> mMySharedContacts;


    public MySharedContactAdapter(Context context, List<UserModel> list) {
        mContext = context;
        mMySharedContacts = list;
    }

    @NonNull
    @Override
    public MySharedContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_shared_contact_item, parent, false);
        return new MySharedContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySharedContactViewHolder viewHolder, int position) {
        final UserModel model = mMySharedContacts.get(position);
        viewHolder.deleteContactBtn.setTag(model);
        if(model.getUser_id() == null) {
            viewHolder.activeBtn.setVisibility(View.INVISIBLE);
            viewHolder.contactStatus.setText(mContext.getResources().getText(R.string.not_registered));
            viewHolder.contactStatus.setVisibility(View.VISIBLE);
            viewHolder.contactName.setText(mContext.getResources().getText(R.string.unknown_number));
            viewHolder.contactName.setTextColor(mContext.getResources().getColor(R.color.disabled_grey));
            viewHolder.deleteContactBtn.setBackground(mContext.getResources().getDrawable(R.drawable.red_border_rounder_corner));
            viewHolder.deleteContactBtn.setText(mContext.getResources().getText(R.string.delete));
            viewHolder.deleteContactBtn.setOnClickListener(this);
            viewHolder.deleteContactBtn.setTextColor(Color.WHITE);
        } else {
            viewHolder.deleteContactBtn.setOnClickListener(null);
            viewHolder.contactName.setText(model.getName());
            viewHolder.contactName.setTextColor(mContext.getResources().getColor(R.color.contact_name_color));
            if(model.isActivated()) {
                viewHolder.activeBtn.setVisibility(View.VISIBLE);
                viewHolder.contactStatus.setVisibility(View.INVISIBLE);
                if(model.isBeautyNetworkActivated()) {
                    viewHolder.deleteContactBtn.setBackground(mContext.getResources().getDrawable(R.drawable.green_border_rounded_corner));
                    viewHolder.deleteContactBtn.setText(mContext.getResources().getText(R.string.network_active));
                    viewHolder.deleteContactBtn.setTextColor(ContextCompat.getColor(mContext, R.color.green_border_color));
                } else {
                    viewHolder.deleteContactBtn.setBackground(mContext.getResources().getDrawable(R.drawable.red_border));
                    viewHolder.deleteContactBtn.setText(mContext.getResources().getText(R.string.network_not_active));
                    viewHolder.deleteContactBtn.setTextColor(ContextCompat.getColor(mContext,R.color.welcome));
                }

            } else {
                viewHolder.contactStatus.setVisibility(View.VISIBLE);
                viewHolder.activeBtn.setVisibility(View.INVISIBLE);
                viewHolder.contactStatus.setText(mContext.getResources().getText(R.string.registered));
                viewHolder.deleteContactBtn.setBackground(mContext.getResources().getDrawable(R.drawable.red_border));
                viewHolder.deleteContactBtn.setText(mContext.getResources().getText(R.string.not_active));
                viewHolder.deleteContactBtn.setTextColor(ContextCompat.getColor(mContext,R.color.welcome));
            }
        }
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

    @Override
    public void onClick(View view) {
        Constants.clickEffect(view);
        deleteSharedContact((UserModel)view.getTag());
        //mMySharedContacts.remove(model);

    }

    private void deleteSharedContact(final UserModel userModel) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Constants.deleteReferContact
                + userModel.getMobile(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("tag", "getUserData " + response.toString());
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                if(userModel != null) {
                                    mMySharedContacts.remove(userModel);
                                }
                                notifyDataSetChanged();
                                Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Error in deleting contact!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(mContext, "Network Problem!", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(mContext).add(req);
    }

    public class MySharedContactViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.deleteContactBtn)
        Button deleteContactBtn;
        @BindView(R.id.activeBtn)
        Button activeBtn;
        @BindView(R.id.contactName)
        TextView contactName;
        @BindView(R.id.contactNumber)
        TextView contactNumber;
        @BindView(R.id.contactStatus)
        TextView contactStatus;

        public MySharedContactViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


}
