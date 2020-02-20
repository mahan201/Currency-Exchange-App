package com.example.currentcyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreenActity.this)
                .withBackgroundResource(R.drawable.ic_background)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(1000)
                .withAfterLogoText("Simple Currency Converter")
                .withLogo(R.drawable.ic_money);
        config.getLogo().setMaxWidth(450);
        config.getLogo().setScaleType(ImageView.ScaleType.FIT_CENTER);

        config.getAfterLogoTextView().setTextColor(Color.WHITE);

        View easySplash = config.create();
        setContentView(easySplash);

    }
}
