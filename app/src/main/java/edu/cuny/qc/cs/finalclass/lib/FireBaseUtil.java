package edu.cuny.qc.cs.finalclass.lib;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

import edu.cuny.qc.cs.finalclass.Login;

public class FireBaseUtil {
    private static final String TAG = "FirebaseUtil";

    public static void authUser(Consumer<FirebaseUser> onComplete) {
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
}
