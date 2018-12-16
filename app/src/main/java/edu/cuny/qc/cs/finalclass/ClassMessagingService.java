package edu.cuny.qc.cs.finalclass;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;
import edu.cuny.qc.cs.finalclass.lib.RefreshWorker;

public class ClassMessagingService extends FirebaseMessagingService {
    private final String TAG = "ClassMessagingService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        HashMap<String, Object> user = new HashMap<>();
        user.put("fcmToken", token);
        FirebaseUtil.mergeToFirestoreUser(user);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (data.get("cookie_expired") != null) {
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build();

                OneTimeWorkRequest refreshWorkRequest = new OneTimeWorkRequest.Builder(RefreshWorker.class).setConstraints(constraints).build();
                WorkManager.getInstance().enqueue(refreshWorkRequest);
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
