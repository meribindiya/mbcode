package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Addresses;
import com.deecoders.meribindiya.activity.Otp;
import com.deecoders.meribindiya.activity.OtpVerification;
import com.deecoders.meribindiya.activity.Signup;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.AddressModel;
import com.deecoders.meribindiya.network.CustomRequest;
import com.deecoders.meribindiya.network.VolleyLibrary;
import com.devspark.appmsg.AppMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AddressModel> mList;
    public AddressModel selectedModel;
    private LayoutInflater mLayoutInflater = null;

    public AddressAdapter(Context context, ArrayList<AddressModel> list) {
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
        final CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.address_item, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        }
        else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }

        final AddressModel model = mList.get(position);
        viewHolder.title.setText(""+model.getAddressType());
        viewHolder.buildingName.setText(""+model.getBuildingName());
        viewHolder.landmark.setText(""+model.getLandmark());
        viewHolder.address.setText(""+model.getAddress());

        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(mContext, v, model);
            }
        });

        if(model.isSelected()){
            selectedModel = model;
            viewHolder.radioButton.setChecked(true);
            Addresses addresses=(Addresses)mContext;
            addresses.addressSelected(selectedModel);
        }
        else{
            viewHolder.radioButton.setChecked(false);
        }

        viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    setCheck(model);
                }
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.isSelected())
                    return;
                setCheck(model);
            }
        });

        return v;
    }

    private void setCheck(AddressModel model) {
        for (AddressModel addressModel : mList) {
            addressModel.setSelected(false);
        }
        model.setSelected(true);
        notifyDataSetChanged();
    }

    private void showPopupMenu(final Context context, View v, final AddressModel model) {
        PopupMenu popup = new PopupMenu(context, v);
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.delete){
                    showAlert(context, model);
                }
                return true;
            }
        });
        popup.show();
    }

    private void showAlert(final Context context, final AddressModel model){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Address")
            .setMessage("Are you sure you want to delete this address?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    deleteAddress(context, model);
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private void deleteAddress(final Context context, final AddressModel model) {
        context.sendBroadcast(new Intent("showProgress"));
        String url = Constants.deleteAddress+model.getId();
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("success", response.toString());
                context.sendBroadcast(new Intent("hideProgress"));
                try {
                    String status = response.getString("status");
                    if(status.equals("success")){
                        for (int i = 0; i < mList.size(); i++) {
                            if(model.getId() == mList.get(i).getId()){
                                mList.remove(i);
                            }
                        }
                        notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(context, "Unable to delete!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                context.sendBroadcast(new Intent("hideProgress"));
                Log.e("error", error.toString());
                Toast.makeText(context, "Network problem!", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyLibrary.getInstance(context).addToRequestQueue(customRequest, "", false);
    }

    static class CompleteListViewHolder {
        public RadioButton radioButton;
        public TextView title, buildingName, landmark, address;
        public ImageView overflow;
        public CompleteListViewHolder(View base) {
            radioButton=base.findViewById(R.id.radio);
            title = (TextView) base.findViewById(R.id.title);
            buildingName = (TextView) base.findViewById(R.id.buildingName);
            landmark = (TextView) base.findViewById(R.id.landmark);
            address = (TextView) base.findViewById(R.id.address);
            overflow=base.findViewById(R.id.overflow);
        }
    }
}
