package edu.cuny.qc.cs.finalclass;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.concurrent.Executor;

import edu.cuny.qc.cs.finalclass.lib.FireBaseUtil;

public class ClassMessagingService extends FirebaseMessagingService implements Executor {
    private final String TAG = "ClassMessagingService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        FireBaseUtil.authUser((authUser) -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            HashMap<String, Object> user = new HashMap<>();
            user.put("fcmToken", token);
            db.collection("users").document(authUser.getUid()).set(user, SetOptions.merge());
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
