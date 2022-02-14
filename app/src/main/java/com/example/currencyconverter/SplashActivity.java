package com.example.currencyconverter;

import static android.view.animation.AnimationUtils.loadAnimation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;


    Animation topAnim, bottomAnim;
    ImageView imageView,imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        topAnim = loadAnimation(this,R.anim.top_animation);
        bottomAnim = loadAnimation(this,R.anim.bottom_animation);


        imageView = findViewById(R.id.imageView);
        imageView2 = findViewById(R.id.imageView2);


        imageView.setAnimation(topAnim);
        imageView2.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(imageView, "CurrencyConverter");
                pairs[1] = new Pair<View,String>(imageView, "CurrencyConverter2");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                startActivity(intent,options.toBundle());



            }
        },SPLASH_SCREEN);

    }
}