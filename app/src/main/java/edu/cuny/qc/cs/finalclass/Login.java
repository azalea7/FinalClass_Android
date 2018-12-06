package edu.cuny.qc.cs.finalclass;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import edu.cuny.qc.cs.finalclass.lib.FireBaseUtil;
import edu.cuny.qc.cs.finalclass.lib.JavascriptInterface;

public class Login extends AppCompatActivity {
    protected final String TAG = "Login";

    private WebView webView;
    private final String[] COOKIE_DOMAINS = {"https://home.cunyfirst.cuny.edu", "https://hrsa.cunyfirst.cuny.edu", "https://cunyfirst.cuny.edu", "https://ssologin.cuny.edu", "https://cuny.edu"};

    private String[] userInfo;

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
                    uploadUserCookies();
                    finish();
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


            File file = new File(getFilesDir(), "user");

            byte[] bytes = new byte[(int) file.length()];

            FileInputStream inputStream = openFileInput("user");
            inputStream.read(bytes);
            inputStream.close();

            System.out.println(new String(bytes));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadUserCookies() {
        FireBaseUtil.authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            HashMap<String, String> cookies = extractCookies();
            HashMap<String, Object> user = new HashMap<>();
            user.put("cookies", cookies);
            user.put("cookieCreatedAt", Timestamp.now());
            user.put("lastCookieRefresh", Timestamp.now());

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
