package edu.cuny.qc.cs.finalclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;

import edu.cuny.qc.cs.finalclass.lib.FireBaseUtil;

public class Login extends AppCompatActivity {
    protected final String TAG = "Login";

    private WebView webView;
    private final String[] COOKIE_DOMAINS = {"https://home.cunyfirst.cuny.edu", "https://hrsa.cunyfirst.cuny.edu", "https://cunyfirst.cuny.edu", "https://ssologin.cuny.edu", "https://cuny.edu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(Login.this);
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.equals("https://home.cunyfirst.cuny.edu/psp/cnyepprd/EMPLOYEE/EMPL/h/?tab=DEFAULT")) {
                    uploadUserCookies();
                    CookieManager.getInstance().removeAllCookies(null);
                    webView.clearCache(true);
                    finish();
                }
            }
        });

        webSettings.setJavaScriptEnabled(true);

        setContentView(webView);

        webView.loadUrl("https://home.cunyfirst.cuny.edu");
    }

    private void uploadUserCookies() {
        FireBaseUtil.authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            HashMap<String, String> cookies = extractCookies();
            HashMap<String, Object> user = new HashMap<>();
            user.put("cookies", cookies);
            user.put("lastCookieRefresh", new Date());

            db.collection("users").document(authUser.getUid()).set(user, SetOptions.merge());
        });
    }

    private HashMap<String, String> extractCookies() {
        CookieManager cookieManager = CookieManager.getInstance();

        HashMap<String, String> hashMap = new HashMap<>();

        for (String domain : COOKIE_DOMAINS) {
            String cookies = cookieManager.getCookie(domain);

            hashMap.put(domain, cookies);
        }

        return hashMap;
    }
}
