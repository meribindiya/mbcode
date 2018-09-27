package com.deecoders.meribindiya.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.BookingDetails;
import com.deecoders.meribindiya.constants.Constants;
import com.deecoders.meribindiya.model.BookingModel;
import com.deecoders.meribindiya.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<ContactModel> mList;
    private LayoutInflater mLayoutInflater = null;
    private ArrayList<ContactModel> mSelectedList = new ArrayList<>();

    private List<ContactModel>originalData = null;
    private ItemFilter mFilter = new ItemFilter();

    public ContactAdapter(Context context, List<ContactModel> list) {
        mContext = context;
        mList = list;
        originalData = list;
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
        ContactViewHolder viewHolder;
        if (convertView == null) {
            v = mLayoutInflater.inflate(R.layout.contacts_list_item, null);
            viewHolder = new ContactViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ContactViewHolder) v.getTag();
        }

        final ContactModel model = mList.get(position);
        viewHolder.contactName.setText(model.getName());
        viewHolder.contactNumber.setText("Phone No: " +model.getPhoneNumber());
        viewHolder.contactCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mSelectedList.add(model);
                } else {
                    mSelectedList.remove(model);
                }
            }
        });
        return v;
    }



    public List<ContactModel> getSelectedList() {
        return mSelectedList;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    static class ContactViewHolder {
        @BindView(R.id.contactCheckbox)
        CheckBox contactCheckbox;
        @BindView(R.id.contactName)
        TextView contactName;
        @BindView(R.id.contactNumber)
        TextView contactNumber;

        public ContactViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = originalData.size();
            final ArrayList<ContactModel> nlist = new ArrayList<ContactModel>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = originalData.get(i).getName();
                if (filterableString.toLowerCase().startsWith(filterString)) {
                    nlist.add(originalData.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList = (ArrayList<ContactModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
