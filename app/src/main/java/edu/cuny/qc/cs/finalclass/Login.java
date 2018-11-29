package edu.cuny.qc.cs.finalclass;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Login extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(getApplicationContext());
        webView.loadUrl("https://cunyfirst.cuny.edu");
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("Login", request.getUrl().toString());
                if (request.getUrl().toString().contains("https://home.cunyfirst.cuny.edu")) {
                    String cookies = CookieManager.getInstance().getCookie(request.getUrl().toString());
                    Log.d("Login", cookies);
                    cookies = CookieManager.getInstance().getCookie("https://cuny.edu");
                    Log.d("Login", cookies);
                }

                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        webSettings.setJavaScriptEnabled(true);

        setContentView(webView);
    }
}
