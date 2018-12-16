package edu.cuny.qc.cs.finalclass.lib;

import android.util.Log;
import android.webkit.CookieManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.function.Consumer;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class FirebaseUtil {
    private static final String TAG = "FirebaseUtil";

    private static void authUser(Consumer<FirebaseUser> onComplete) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            onComplete.accept(mUser);
            return;
        }

        mAuth.signInAnonymously().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInAnonymously:success");
                onComplete.accept(mAuth.getCurrentUser());
            } else {
                Log.w(TAG, "signInAnonymously:failure", task.getException());
                onComplete.accept(null);
            }
        });
    }

    public static void mergeToFirestoreUser(HashMap<String, Object> map) {
        authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(authUser.getUid()).set(map, SetOptions.merge());
        });
    }

    public static void mergeToUser(HashMap<String, HashMap<String, Object>> map) {
        authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(authUser.getUid()).set(map, SetOptions.merge());
        });
    }

    public static void getFirestoreUser(OnCompleteListener onCompleteListener) {
        authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users").document(authUser.getUid()).get().addOnCompleteListener(onCompleteListener);
        });
    }

    public static String tokenId(){
       final String[] ti={""};
        authUser((authUser) -> {
            ti[0] = authUser.getUid();
        });
        return ti[0];
    }

    public static void uploadUserCookies(Object cookieManager) {
        HashMap<String, String> cookies;
        if (cookieManager instanceof CookieManager) {
            cookies = extractCookiesFromWebKit((CookieManager) cookieManager);
        } else {
            cookies = extractCookiesFromCookieManager((CookieJar) cookieManager);
        }
        HashMap<String, Object> user = new HashMap<>();
        user.put("cookies", cookies);
        user.put("cookieCreatedAt", Timestamp.now());
        user.put("lastCookieRefresh", Timestamp.now());

        mergeToFirestoreUser(user);
    }

    private static final String[] COOKIE_DOMAINS = {"https://home.cunyfirst.cuny.edu", "https://ssologin.cuny.edu"};

    private static HashMap<String, String> extractCookiesFromWebKit(CookieManager cookieManager) {
        HashMap<String, String> hashMap = new HashMap<>();

        for (String domain : COOKIE_DOMAINS) {
            String cookies = cookieManager.getCookie(domain);

            hashMap.put(domain, cookies);
        }

        return hashMap;
    }

    private static HashMap<String, String> extractCookiesFromCookieManager(CookieJar cookieManager) {
        HashMap<String, String> hashMap = new HashMap<>();

        for (String domain : COOKIE_DOMAINS) {
            StringBuilder cookieString = new StringBuilder();

            for (Cookie cookie : cookieManager.loadForRequest(HttpUrl.parse(domain))) {
                cookieString.append(cookie.name()).append("=").append(cookie.value()).append("; ");
            }

            if (cookieString.length() > 0) {
                cookieString.delete(cookieString.length() - 2, cookieString.length());
            }

            hashMap.put(domain, cookieString.toString());
        }

        return hashMap;
    }
}
