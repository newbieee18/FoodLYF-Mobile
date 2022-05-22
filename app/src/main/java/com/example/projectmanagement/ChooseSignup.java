package com.example.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseSignup extends AppCompatActivity {

    private final AppCompatActivity activity = ChooseSignup.this;
    ViewPager mViewPager;
    LinearLayout mDotLayout;
    SliderAdapter sliderAdapter;
    TextView[] mDots;
    Button btnBack, btnNext;
    int mCurrentPage;
    Animation fadeIn;

    @Override
    public void onBackPressed() {
        Intent login = new Intent(getApplicationContext(), Login.class);
        startActivity(login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_signup);

        mViewPager = findViewById(R.id.ViewPager);
        mDotLayout = findViewById(R.id.DotLayout);

        sliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(sliderAdapter);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1500);
        mViewPager.startAnimation(fadeIn);

        addDotsIndicator(0);

        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);

        mViewPager.addOnPageChangeListener(viewListener);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCurrentPage + 1);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

    }

    public void addDotsIndicator(int position){

            mDots = new TextView[3];
            mDotLayout.removeAllViews();

            for(int i=0; i<mDots.length; i++){

                mDots[i] = new TextView(this);
                mDots[i].setText(Html.fromHtml("&#8226;"));
                mDots[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mDots[i].setTextSize(35);
                mDots[i].setTextColor(Color.parseColor("#FB8C00"));

                mDotLayout.addView(mDots[i]);
            }

            if(mDots.length>0){
                mDots[position].setTextColor(Color.parseColor("#00ACC1"));
            }
    }
    
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int il) {

        }

        @Override
        public void onPageSelected(int i) {

            mViewPager.startAnimation(fadeIn);
            addDotsIndicator(i);
            mCurrentPage = i;
            
            if(i == 0){
                btnNext.setEnabled(true);
                btnBack.setEnabled(false);
                btnBack.setVisibility(View.INVISIBLE);

                btnNext.setText("NEXT");
                btnBack.setText("");
            }

            else if(i == mDots.length -1){
                btnNext.setEnabled(true);
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);

                btnNext.setText("");
                btnBack.setText("BACK");
            }

            else{
                btnNext.setEnabled(true);
                btnBack.setEnabled(true);
                btnBack.setVisibility(View.VISIBLE);

                btnNext.setText("NEXT");
                btnBack.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void fadeIn(){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);
    }




}