package com.meribindiyaemployee.meribindiyaemployee.adaptors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meribindiyaemployee.meribindiyaemployee.R;
import com.meribindiyaemployee.meribindiyaemployee.responses.CustomerOrderService;


import java.util.List;

public class JobServiceAdaptor extends RecyclerView.Adapter<JobServiceAdaptor.JobServiceViewHolder> {

    private Context context;
    private List<CustomerOrderService> customerOrderServices;
    private LayoutInflater layoutInflater;

    public JobServiceAdaptor(Context context, List<CustomerOrderService> customerOrderServices) {
        this.context = context;
        this.customerOrderServices = customerOrderServices;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public JobServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.service_item, parent, false);
        return new JobServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobServiceViewHolder holder, int position) {
        CustomerOrderService customerOrderService = customerOrderServices.get(position);
        holder.serviceName.setText(customerOrderService.getServiceName());
        holder.serviceQuantity.setText(String.valueOf(customerOrderService.getQuantity()));
        holder.serviceTotalAmount.setText(String.valueOf(customerOrderService.getTotal_service_amount()));

    }

    @Override
    public int getItemCount() {
        return customerOrderServices.size();
    }

    class JobServiceViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName, serviceQuantity, serviceTotalAmount;

        public JobServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.service_name);
            serviceQuantity = itemView.findViewById(R.id.service_quantity);
            serviceTotalAmount = itemView.findViewById(R.id.service_total_amount);
        }
    }
}
