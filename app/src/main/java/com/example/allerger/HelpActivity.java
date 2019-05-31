package com.example.allerger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        ImageView help1 = findViewById(R.id.help1);
        ImageView help2 = findViewById(R.id.help2);
        ImageView help3 = findViewById(R.id.help3);
        Glide.with(this).load(R.drawable.help1).into(help1);
        Glide.with(this).load(R.drawable.help2).into(help2);
        Glide.with(this).load(R.drawable.help3).into(help3);
    }
}
