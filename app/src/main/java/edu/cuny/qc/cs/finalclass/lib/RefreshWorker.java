package edu.cuny.qc.cs.finalclass.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RefreshWorker extends Worker {
    private static final String TAG = "RefreshWorker";

    public RefreshWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        boolean success = false;
        try {
            success = refreshCookie();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success ? Result.success() : Result.retry();
    }

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36";

    private boolean refreshCookie() throws IOException {
        String[] userInfo = getStoredLogin();

        if (userInfo == null) {
            return true;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private HashMap<String, HashMap<String, Cookie>> cookieMap = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        String domain = "https://" + url.host();
                        HashMap<String, Cookie> storedCookies = cookieMap.getOrDefault(domain, new HashMap<>());
                        cookies.forEach(cookie -> storedCookies.put(cookie.name(), cookie));
                        cookieMap.put(domain, storedCookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        String domain = "https://" + url.host();

                        HashMap<String, Cookie> storedCookies = cookieMap.get(domain);
                        if (storedCookies == null) {
                            return Collections.emptyList();
                        } else {
                            return new ArrayList<>(storedCookies.values());
                        }
                    }
                })
                .build();

        Request initialRequest = new Request.Builder()
                .url("https://home.cunyfirst.cuny.edu")
                .addHeader("User-Agent", USER_AGENT)
                .get()
                .build();

        Response response = client.newCall(initialRequest).execute();
        Log.d(TAG, response.toString());

        RequestBody formBody = new FormBody.Builder()
                .add("usernameH", userInfo[0])
                .add("username", userInfo[0].substring(0, userInfo[0].indexOf("@")))
                .add("password", userInfo[1])
                .add("submit", "")
                .build();

        Request loginRequest = new Request.Builder()
                .url("https://ssologin.cuny.edu/oam/server/auth_cred_submit")
                .addHeader("User-Agent", USER_AGENT)
                .post(formBody)
                .build();

        response = client.newCall(loginRequest).execute();

        Log.d(TAG, response.toString());

        if (response.isSuccessful()) {
            FirebaseUtil.uploadUserCookies(client.cookieJar());
            return true;
        } else {
            Log.d(TAG, response.body().toString());
            return false;
        }
    }

    private String[] getStoredLogin() {
        File file = new File(getApplicationContext().getFilesDir(), "user");

        if (!file.exists()) {
            return null;
        }

        byte[] bytes = new byte[(int) file.length()];

        FileInputStream inputStream;
        try {
            inputStream = getApplicationContext().openFileInput("user");
            inputStream.read(bytes);
            inputStream.close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return new String(bytes).split("\n");
    }

}
