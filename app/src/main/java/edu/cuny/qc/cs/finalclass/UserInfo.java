package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;

public class UserInfo extends AppCompatActivity {
    protected final String TAG = "Login";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        tv = (TextView) findViewById(R.id.courses);
        String tokenID = FirebaseUtil.tokenId();
//        String row = "";


        final DocumentReference docRef = db.collection("users").document(tokenID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot,
                                FirebaseFirestoreException e) {
                Map<String,Object> user = snapshot.getData();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    user.forEach((k,v)->{
                            if(k.length()==5){
                                Map<String,Object> section= (Map<String, Object>) user.get(k);
                                String row ="Course number: "+section.get("course number")
                                        +"\nSection Number: " + k
                                        +"\nInstructor: "+ section.get("instructor")
                                        +"\nTime: " + section.get("time")
                                        +"\nStatus: " + section.get("status") + "\n\n";
                                tv.append(row);
                            }
                        });
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        Button addCourse =(Button) findViewById(R.id.addBtn);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
