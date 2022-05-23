package com.example.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobListViewHolder> {

    private Context mCtx;
    private List<OrderList> jobList;

    public JobListAdapter(Context mCtx, List<OrderList> jobList){
        this.mCtx = mCtx;
        this.jobList = jobList;
    }


    @NonNull
    @Override
    public JobListAdapter.JobListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.order_list, parent, false);
        return new JobListAdapter.JobListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobListViewHolder holder, int position) {
        OrderList jobs = jobList.get(position);

        holder.orders.setText("Total of Orders: " + jobs.getProductQuantity());
        holder.customer_name.setText("Customer Name: " + jobs.getCustomerName() + "\nBranch: " + jobs.getBranchName());
        holder.phone.setText("Customer Contact#: " + jobs.getCustomerPhone());
        holder.total.setText(String.format("Total Price: ₱%.2f ", jobs.getTotal()));
        holder.location.setText(String.format("Branch Location Distance: %.2fkm", jobs.getDistance()));
        holder.btnAcceptJob.setText("VIEW JOB");

        holder.btnAcceptJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customerPhone = jobs.getCustomerPhone();

                Intent intent = ((Activity) mCtx).getIntent();
                String phoneNumber = intent.getExtras().getString("number");
                Intent riderDelivery = new Intent(mCtx, RiderDeliver.class);
                riderDelivery.putExtra("number", phoneNumber);
                riderDelivery.putExtra("customerPhone", customerPhone);
                mCtx.startActivity(riderDelivery);
                ((Activity) mCtx).overridePendingTransition(0, 0);
                ((Activity) mCtx).finish();


            }
        });

    }


    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public class JobListViewHolder extends RecyclerView.ViewHolder {

        TextView orders, phone, customer_name, total, location;
        LinearLayout linearLayout;
        Button btnAcceptJob;

        public JobListViewHolder(View itemView) {
            super(itemView);

            orders = itemView.findViewById(R.id.orders);
            phone = itemView.findViewById(R.id.phoneNumber);
            customer_name = itemView.findViewById(R.id.customerName);
            total = itemView.findViewById(R.id.total);
            location = itemView.findViewById(R.id.location);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            btnAcceptJob = itemView.findViewById(R.id.btnAcceptOrder);


        }
    }
}
