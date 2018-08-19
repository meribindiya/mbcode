package com.meribindiyaemployee.meribindiyaemployee.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meribindiyaemployee.meribindiyaemployee.R;
import com.meribindiyaemployee.meribindiyaemployee.activities.JobDetailsActivity;
import com.meribindiyaemployee.meribindiyaemployee.responses.CustomerOrder;

import java.util.List;

public class JobAdaptor extends RecyclerView.Adapter<JobAdaptor.JobViewHolder> {


    private List<CustomerOrder> customerOrderList;

    private final Context context;

    private final LayoutInflater layoutInflater;

    public JobAdaptor(List<CustomerOrder> orderList, Context context) {
        this.customerOrderList = orderList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.job_item, parent, false);
        return new JobViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        CustomerOrder customerOrder = customerOrderList.get(position);
        holder.jobservices.setText(customerOrder.getServices());
        holder.jobdate.setText(customerOrder.getBookingdate());
        holder.jobtime.setText(customerOrder.getBooking_time());
        holder.jobamount.setText(String.valueOf(customerOrder.getTotal()));
        holder.itemView.setTag(customerOrder.getId());
        holder.paymentSoource.setText(customerOrder.getPymnt_source());

    }

    @Override
    public int getItemCount() {
        return customerOrderList.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobservices, jobdate, jobtime, jobamount, paymentSoource;


        public JobViewHolder(@NonNull final View itemView) {
            super(itemView);
            jobservices = itemView.findViewById(R.id.job_services);
            jobdate = itemView.findViewById(R.id.job_date);
            jobtime = itemView.findViewById(R.id.job_time);
            jobamount = itemView.findViewById(R.id.job_final_amount);
            paymentSoource = itemView.findViewById(R.id.payment_source);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Long id = (Long) itemView.getTag();
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("orderid", id);
                    context.startActivity(intent);

                }
            });

        }
    }

}
