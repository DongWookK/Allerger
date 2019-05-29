package com.example.allerger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splash = (ImageView)findViewById(R.id.splash);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(splash);
        Glide.with(this).load(R.drawable.splash_source).into(gifImage);
        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 3000);
    }
    private class splashHandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class));
            Splash.this.finish();
        }
    }
    @Override
    public void onBackPressed(){

    }
}