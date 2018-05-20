package com.media.tf.ung_dung_doc_bao.ModelLoad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.media.tf.ung_dung_doc_bao.R;

public class WebviewNewsActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webviewnews);
        webView = (WebView)findViewById(R.id.webview);
        Intent intent = getIntent();
        String duonglink = intent.getStringExtra("link");

        webView.loadUrl(duonglink);
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.demo_face_out,R.anim.demo_face_out);
        super.onBackPressed();
    }
}
