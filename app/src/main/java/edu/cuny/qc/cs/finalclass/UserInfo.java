package edu.cuny.qc.cs.finalclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfo extends AppCompatActivity {
    protected final String TAG = "Login";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv = (TextView) findViewById(R.id.courses);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

//        db.collection("users").document(FirebaseUtil.tokenId()).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if(documentSnapshot.exists()){
//                        String s = documentSnapshot.toString();
//
//                        tv.setText(s);
//                    }
//                });


    }
}
