package com.example.projectmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.customer,
            R.drawable.store,
            R.drawable.rider
    };

    public String[] slide_desc = {
            "FoodLYF's will give you a better satisfaction and will not let you disappoint. Register now!",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                    "when an unknown printer took a galley of type and scrambled it to make a type specimen book."
    };

    @Override
    public int getCount() {
        return slide_desc.length;
    }




    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_images);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_desc);
        Button btnRegister = (Button) view.findViewById(R.id.btnRegister);

        slideImageView.setImageResource(slide_images[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        if(position == 0) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cRegistration = new Intent(context, CustomerRegistration.class);
                    context.startActivity(cRegistration);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
            });
        }

        if(position == 2) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent rRegistration = new Intent(context, RiderApplication.class);
                    context.startActivity(rRegistration);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
            });
        }

        if(position == 1) {
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sRegistration = new Intent(context, StoreApplication.class);
                    context.startActivity(sRegistration);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
            });
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        container.removeView((RelativeLayout)object);

    }



}
