package com.example.projectmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OutletsAdapter extends RecyclerView.Adapter<OutletsAdapter.OutletsViewHolder> {

    private Context mCtx;
    private List<Outlets> outletList;

    public OutletsAdapter(Context mCtx, List<Outlets> outletList) {
        this.mCtx = mCtx;
        this.outletList = outletList;
    }

    @NonNull
    @Override
    public OutletsAdapter.OutletsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.store_outlets, parent, false);
        return new OutletsAdapter.OutletsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OutletsViewHolder holder, int position) {
        Outlets outlets = outletList.get(position);

        holder.outletID.setText(String.valueOf(outlets.getID()));
        holder.phone.setText("Phone Number: " + outlets.getPhone());
        holder.store_name.setText("Store Name: " + outlets.getStoreName());
        holder.branch_name.setText("Branch Name: " + outlets.getBranchName());
        holder.latitude.setText("Latitude: " + outlets.getLatitude());
        holder.longitude.setText("Longitude: " + outlets.getLongitude());

        if(position %2 == 1){
            holder.storeCardView.setCardBackgroundColor(Color.parseColor("#FFC9F9FF"));
        }

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = ((Activity) mCtx).getIntent();
                String phoneNumber = intent.getExtras().getString("number");
                Intent editOutlet = new Intent(mCtx, EditOutlet.class);
                editOutlet.putExtra("number", phoneNumber);
                String outletID = String.valueOf(outlets.getID());
                editOutlet.putExtra("outletID", outletID);
                mCtx.startActivity(editOutlet);

            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setCancelable(true);
                builder.setTitle("Delete Outlet");
                builder.setMessage("Are you sure to delete this outlet?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String id;
                                id = String.valueOf(outlets.getID());

                                if(!id.equals("")){
                                    Handler handler = new Handler();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Starting Write and Read data with URL
                                            //Creating array for parameters
                                            String[] field = new String[1];
                                            field[0] = "id";
                                            //Creating array for data
                                            String[] data = new String[1];
                                            data[0] = id;
                                            PutData putData = new PutData("http://192.168.254.109/fadSystem/delete_outlet.php", "POST", field, data);
                                            if (putData.startPut()) {
                                                if (putData.onComplete()) {
                                                    String result = putData.getResult();
                                                    if(result.equals("Record deleted successfully")){
                                                        Intent intent = ((Activity) mCtx).getIntent();
                                                        String phoneNumber = intent.getExtras().getString("number");
                                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                                        Intent storeOutlets = new Intent(mCtx, StoreOutlets.class);
                                                        storeOutlets.putExtra("number", phoneNumber);
                                                        mCtx.startActivity(storeOutlets);
                                                    }
                                                    else{
                                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }


                            }
                        });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return outletList.size();
    }


    class OutletsViewHolder extends RecyclerView.ViewHolder {

        TextView outletID, phone, store_name, branch_name, latitude, longitude;
        ImageView ivEdit, ivDelete;
        CardView storeCardView;

        public OutletsViewHolder(View itemView) {
            super(itemView);

            outletID = itemView.findViewById(R.id.outletID);
            phone = itemView.findViewById(R.id.outletPhone);
            store_name = itemView.findViewById(R.id.outletStoreName);
            branch_name = itemView.findViewById(R.id.outletBranchName);
            latitude = itemView.findViewById(R.id.outletLatitude);
            longitude = itemView.findViewById(R.id.outletLongitude);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            storeCardView = itemView.findViewById(R.id.storeCardView);

        }
    }
}
