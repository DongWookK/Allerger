package com.example.allerger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class TabFragment1 extends Fragment {
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout tab1 = (RelativeLayout) inflater.inflate(R.layout.tab_fragment_1, container, false);
        webView = tab1.findViewById(R.id.content);
        setWebView();
        return tab1;
    }
    public void setWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://user-images.githubusercontent.com/44195740/58384547-cf30ce80-801d-11e9-8667-7f0cdc0c4c2a.png\n");
    }
}