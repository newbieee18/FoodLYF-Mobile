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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private Context mCtx;
    private List<OrderList> orderList;

    public OrderListAdapter(Context mCtx, List<OrderList> orderList){
        this.mCtx = mCtx;
        this.orderList = orderList;
    }


    @NonNull
    @Override
    public OrderListAdapter.OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.order_list, parent, false);
        return new OrderListAdapter.OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        OrderList orders = orderList.get(position);

        holder.orders.setText("Total of Orders: " + orders.getProductQuantity());
        holder.customer_name.setText(orders.getCustomerName());
        holder.phone.setText(orders.getCustomerPhone());
        holder.total.setText(String.format("Total Price: ₱%.2f ", orders.getTotal()));
        holder.location.setText("Address: " + orders.getCustomerAddress());

        Intent intent = ((Activity) mCtx).getIntent();
        String phoneNumber = intent.getExtras().getString("number");

        holder.btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String customerPhone = orders.getCustomerPhone();
                final String status = "Accepted";
                final String customer_phone = customerPhone;
                final String branch_phone = phoneNumber;

                if (!status.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            String[] field = new String[3];
                            field[0] = "status";
                            field[1] = "customer_phone";
                            field[2] = "branch_phone";

                            String[] data = new String[3];
                            data[0] = status;
                            data[1] = customer_phone;
                            data[2] = branch_phone;

                            PutData putData = new PutData("http://192.168.254.109/fadSystem/update_order.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if (result.equals("Record updated successfully")) {
                                        Intent viewOrder = new Intent(mCtx, ViewOrder.class);
                                        viewOrder.putExtra("number", phoneNumber);
                                        viewOrder.putExtra("customerPhone", customerPhone);
                                        mCtx.startActivity(viewOrder);
                                        ((Activity) mCtx).overridePendingTransition(0, 0);
                                        ((Activity) mCtx).finish();
                                    } else {
                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {

        TextView orders, phone, customer_name, total, location;
        LinearLayout linearLayout;
        Button btnAcceptOrder;

        public OrderListViewHolder(View itemView) {
            super(itemView);

            orders = itemView.findViewById(R.id.orders);
            phone = itemView.findViewById(R.id.phoneNumber);
            customer_name = itemView.findViewById(R.id.customerName);
            total = itemView.findViewById(R.id.total);
            location = itemView.findViewById(R.id.location);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            btnAcceptOrder = itemView.findViewById(R.id.btnAcceptOrder);

        }
    }
}
