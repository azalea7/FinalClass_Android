package edu.cuny.qc.cs.finalclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileOutputStream;

import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;
import edu.cuny.qc.cs.finalclass.lib.JavascriptInterface;

public class Login extends AppCompatActivity {
    protected final String TAG = "Login";
    private WebView webView;

    private String[] userInfo;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains("https://ssologin.cuny.edu/cuny.html?resource_url=")) {
                    webView.loadUrl("javascript:var _ZZE = submitCheck; submitCheck = function(){window.Android.sendFormValues(document.querySelector('#CUNYfirstUsernameH').value, document.querySelector('#CUNYfirstPassword').value); return _ZZE();}");
                } else if (url.equals("https://home.cunyfirst.cuny.edu/psp/cnyepprd/EMPLOYEE/EMPL/h/?tab=DEFAULT")) {
                    storeLogin();
                    FirebaseUtil.uploadUserCookies(CookieManager.getInstance());
                    Intent intent = new Intent(getApplicationContext(), UserInfo.class);
                    finish();
                    startActivity(intent);
                }
            }
        });

        webView.addJavascriptInterface(new JavascriptInterface((info) -> {
            userInfo = info;
        }), "Android");

        CookieManager.getInstance().removeAllCookies(null);
        webView.clearCache(true);

        webView.loadUrl("https://home.cunyfirst.cuny.edu");

        setContentView(webView);
    }

    private void storeLogin() {
        String username = userInfo[0];
        String password = userInfo[1];

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("user", Context.MODE_PRIVATE);
            outputStream.write(username.getBytes());
            outputStream.write('\n');
            outputStream.write(password.getBytes());
            outputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
