package com.example.projectmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.IdRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Product> productList;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog dialog;
    View chooseDrink;
    RadioGroup rgDrinks;
    Button btnClose, btnSave;
    String fProductName;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_list, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        String imageUrl = product.getProductImage();
        byte[] decodedString = Base64.decode(imageUrl, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.productImage.setImageBitmap(decodedByte);
        holder.productImage.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.productName.setText(product.getProductName());
        holder.productDesc.setText(product.getProductDesc());
        holder.productPrice.setText(String.format("₱%.2f", product.getProductPrice()));

        Intent intent = ((Activity) mCtx).getIntent();
        String phoneNumber = intent.getExtras().getString("number");

        holder.addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFound = product.getProductDesc().contains("Drink");
                if(isFound) {
                    getProducts();
                    alertDialogBuilder = new AlertDialog.Builder((Activity) mCtx);
                    chooseDrink = ((Activity) mCtx).getLayoutInflater().inflate(R.layout.choose_drink, null);
                    rgDrinks = (RadioGroup) chooseDrink.findViewById(R.id.rgDrinks);
                    btnClose = (Button) chooseDrink.findViewById(R.id.btnClose);
                    btnSave = (Button) chooseDrink.findViewById(R.id.btnSaveDrink);

                    alertDialogBuilder.setView(chooseDrink);
                    dialog = alertDialogBuilder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final String pName, fQuantity, fSubtotal, encodedImage, price;
                            final int quantity;
                            final Double subtotal;
                            pName = String.valueOf(product.getProductName()) + " (" + fProductName + ")";
                            price = String.valueOf(product.getProductPrice());
                            quantity = 1;
                            subtotal = product.getProductPrice() * quantity;
                            fQuantity = String.valueOf(quantity);
                            fSubtotal = String.valueOf(subtotal);
                            encodedImage = Base64.encodeToString(decodedString, Base64.DEFAULT);

                            if(!pName.equals("") && !price.equals("") && !fQuantity.equals("") && !fSubtotal.equals("") && !encodedImage.equals("")){
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Starting Write and Read data with URL
                                        //Creating array for parameters
                                        String[] field = new String[6];
                                        field[0] = "product_name";
                                        field[1] = "product_price";
                                        field[2] = "quantity";
                                        field[3] = "subtotal";
                                        field[4] = "product_image";
                                        field[5] = "phone";
                                        //Creating array for data
                                        String[] data = new String[6];
                                        data[0] = pName;
                                        data[1] = price;
                                        data[2] = fQuantity;
                                        data[3] = fSubtotal;
                                        data[4] = encodedImage;
                                        data[5] = phoneNumber;
                                        PutData putData = new PutData("http://192.168.254.109/fadSystem/addCart.php", "POST", field, data);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String result = putData.getResult();
                                                if(result.equals("Successfully Added!")){
                                                    Intent cart = new Intent(mCtx, Cart.class);
                                                    cart.putExtra("number", phoneNumber);
                                                    cart.putExtra("product_name", pName);
                                                    cart.putExtra("product_price", price);
                                                    cart.putExtra("quantity", quantity);
                                                    cart.putExtra("subtotal", fSubtotal);
                                                    cart.putExtra("encodedImage", encodedImage);

                                                    LayoutInflater inflater = ((Activity) mCtx).getLayoutInflater();
                                                    View layout = inflater.inflate(R.layout.custom_toast, ((Activity) mCtx).findViewById(R.id.custom_toast_container));

                                                    TextView text = layout.findViewById(R.id.text);
                                                    text.setText(R.string.custom_toast_message);

                                                    Toast toast = new Toast(mCtx);
                                                    toast.setGravity(Gravity.BOTTOM, 0, 40);
                                                    toast.setDuration(Toast.LENGTH_LONG);
                                                    toast.setView(layout);
                                                    toast.show();

                                                    dialog.dismiss();

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
                }
                else{
                    Intent intent = ((Activity) mCtx).getIntent();
                    String phoneNumber = intent.getExtras().getString("number");

                    final String pName, fQuantity, fSubtotal, encodedImage, price;
                    final int quantity;
                    final Double subtotal;
                    pName = String.valueOf(product.getProductName());
                    price = String.valueOf(product.getProductPrice());
                    quantity = 1;
                    subtotal = product.getProductPrice() * quantity;
                    fQuantity = String.valueOf(quantity);
                    fSubtotal = String.valueOf(subtotal);
                    encodedImage = Base64.encodeToString(decodedString, Base64.DEFAULT);

                    if(!pName.equals("") && !price.equals("") && !fQuantity.equals("") && !fSubtotal.equals("") && !encodedImage.equals("")){
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[6];
                                field[0] = "product_name";
                                field[1] = "product_price";
                                field[2] = "quantity";
                                field[3] = "subtotal";
                                field[4] = "product_image";
                                field[5] = "phone";
                                //Creating array for data
                                String[] data = new String[6];
                                data[0] = pName;
                                data[1] = price;
                                data[2] = fQuantity;
                                data[3] = fSubtotal;
                                data[4] = encodedImage;
                                data[5] = phoneNumber;
                                PutData putData = new PutData("http://192.168.254.109/fadSystem/addCart.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        if(result.equals("Successfully Added!")){
                                            Intent cart = new Intent(mCtx, Cart.class);
                                            cart.putExtra("number", phoneNumber);
                                            cart.putExtra("product_name", pName);
                                            cart.putExtra("product_price", price);
                                            cart.putExtra("quantity", quantity);
                                            cart.putExtra("subtotal", fSubtotal);
                                            cart.putExtra("encodedImage", encodedImage);

                                            LayoutInflater inflater = ((Activity) mCtx).getLayoutInflater();
                                            View layout = inflater.inflate(R.layout.custom_toast, ((Activity) mCtx).findViewById(R.id.custom_toast_container));

                                            TextView text = layout.findViewById(R.id.text);
                                            text.setText(R.string.custom_toast_message);

                                            Toast toast = new Toast(mCtx);
                                            toast.setGravity(Gravity.BOTTOM, 0, 40);
                                            toast.setDuration(Toast.LENGTH_LONG);
                                            toast.setView(layout);
                                            toast.show();

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
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productDesc, productPrice;
        ImageView productImage;
        Button addCart;
        CardView productCardView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productDesc = itemView.findViewById(R.id.productDesc);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            addCart = itemView.findViewById(R.id.addCart);
            productCardView = itemView.findViewById(R.id.productCardView);

        }
    }

    private void getProducts() {
        Intent intent = ((Activity) mCtx).getIntent();
        String phoneNumber = intent.getExtras().getString("number");
        String storeName = intent.getExtras().getString("storeName");
        String url = Config.PRODUCT_URL + storeName + "&category=Drinks";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                showJSONS(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mCtx, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);

    }

    private void showJSONS(String response) {
        String product_name = "";
        String product_desc = "";
        String product_image = "";
        Integer product_id;
        Double ratings, product_price = null;

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY2);


            for(int i=0; i<result.length(); i++) {

                Typeface typeface = ResourcesCompat.getFont(mCtx,R.font.chakra_petch_medium);

                JSONObject products = result.getJSONObject(i);
                product_name = products.getString(Config.PRODUCT_NAME);
                product_desc = products.getString(Config.PRODUCT_DESC);
                product_image = products.getString(Config.PRODUCT_IMAGE);
                product_id = products.getInt(Config.PRODUCT_ID);
                product_price = products.getDouble(Config.PRODUCT_PRICE);
                ratings = products.getDouble(Config.RATINGS);

                RadioButton rdBtn = new RadioButton(mCtx);
                rdBtn.setText(product_name);
                rdBtn.setTextSize(17);
                rdBtn.setTextColor(Color.parseColor("#FB8C00"));
                rdBtn.setTypeface(typeface);
                rgDrinks.addView(rdBtn);
                rgDrinks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        fProductName = radioButton.getText().toString();
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}