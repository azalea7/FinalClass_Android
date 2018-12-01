package edu.cuny.qc.cs.finalclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

import edu.cuny.qc.cs.finalclass.lib.FireBaseUtil;

public class Login extends AppCompatActivity {
    protected final String TAG = "Login";

    private WebView webView;
    private final String[] COOKIE_DOMAINS = {"home.cunyfirst.cuny.edu", "hrsa.cunyfirst.cuny.edu", "cunyfirst.cuny.edu", "cuny.edu"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(Login.this);
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient() {
            private int visitCounter = 0;

            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.equals("https://home.cunyfirst.cuny.edu/psp/cnyepprd/EMPLOYEE/EMPL/h/?tab=DEFAULT")) {
                    if (visitCounter == 2) {
                        uploadUserCookies();
                        finish();
                        return true;
                    } else {
                        visitCounter++;
                    }
                }

                view.loadUrl(url);
                return true;
            }
        });

        webSettings.setJavaScriptEnabled(true);

        setContentView(webView);

        CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {
                webView.loadUrl("https://home.cunyfirst.cuny.edu");
            }
        });
    }

    private void uploadUserCookies() {
        FireBaseUtil.authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            HashMap<String, String> cookies = extractCookies();
            HashMap<String, Object> user = new HashMap<>();
            user.put("cookies", cookies);

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
