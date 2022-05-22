package com.example.projectmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FoodProductsAdapter extends RecyclerView.Adapter<FoodProductsAdapter.FoodProductsViewHolder> {

    private Context mCtx;
    private List<Product> productList;

    public FoodProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public FoodProductsAdapter.FoodProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.food_product_list, parent, false);
        return new FoodProductsAdapter.FoodProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FoodProductsAdapter.FoodProductsViewHolder holder, int position) {
        Product product = productList.get(position);

        String imageUrl = product.getProductImage();
        byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.productImage.setImageBitmap(decodedByte);
        holder.productID.setText(String.valueOf(product.getId()));
        holder.productName.setText(product.getProductName());
        holder.productDesc.setText(product.getProductDesc());
        holder.productPrice.setText("₱" + String.valueOf(product.getProductPrice()));

        holder.editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ((Activity) mCtx).getIntent();
                String phoneNumber = intent.getExtras().getString("number");
                SharedPreferences sp = mCtx.getSharedPreferences("credentials", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                Intent editFoodProduct = new Intent(mCtx, EditFoodProduct.class);
                editor.putString("prodID", holder.productID.getText().toString());
                editFoodProduct.putExtra("number", phoneNumber);
                editor.commit();
                mCtx.startActivity(editFoodProduct);
            }
        });

        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setCancelable(true);
                builder.setTitle("Delete Product");
                builder.setMessage("Are you sure to delete this product?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                final String product_id;
                                product_id = String.valueOf(product.getId());

                                if(!product_id.equals("")){
                                    Handler handler = new Handler();
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Starting Write and Read data with URL
                                            //Creating array for parameters
                                            String[] field = new String[1];
                                            field[0] = "product_id";
                                            //Creating array for data
                                            String[] data = new String[1];
                                            data[0] = product_id;
                                            PutData putData = new PutData("http://192.168.254.109/fadSystem/delete_food.php", "POST", field, data);
                                            if (putData.startPut()) {
                                                if (putData.onComplete()) {
                                                    String result = putData.getResult();
                                                    if(result.equals("Record deleted successfully")){
                                                        Intent intent = ((Activity) mCtx).getIntent();
                                                        String phoneNumber = intent.getExtras().getString("number");
                                                        Toast.makeText(mCtx, result, Toast.LENGTH_SHORT).show();
                                                        Intent FoodProducts = new Intent(mCtx, FoodProducts.class);
                                                        FoodProducts.putExtra("number", phoneNumber);
                                                        mCtx.startActivity(FoodProducts);
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
        return productList.size();
    }

    class FoodProductsViewHolder extends RecyclerView.ViewHolder {

        TextView productID, productName, productDesc, productPrice, rating;
        ImageView productImage, editProduct, deleteProduct;

        public FoodProductsViewHolder(View itemView) {
            super(itemView);

            productID = itemView.findViewById(R.id.productId);
            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            editProduct = itemView.findViewById(R.id.editProduct);
            deleteProduct = itemView.findViewById(R.id.deleteProduct);

        }
    }
}
