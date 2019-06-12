package com.Allergerapp.allerger;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

public class TabFragment2 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout tab2 = (LinearLayout) inflater.inflate(R.layout.tab_fragment_2, container, false);
        ImageView frag2 = (ImageView) tab2.findViewById(R.id.frag2_main);
        Glide.with(this).load(R.drawable.fra2).into(frag2);
        return tab2;
    }
}