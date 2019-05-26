package com.example.allerger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class TabFragment2 extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout tab2 = (RelativeLayout)inflater.inflate(R.layout.tab_fragment_2, container, false);
        webView = tab2.findViewById(R.id.information);
        setWebView();
        return tab2;
    }
    public void setWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.qcheck.co.kr/kor/images/doc/narch1.png\n");
    }
}