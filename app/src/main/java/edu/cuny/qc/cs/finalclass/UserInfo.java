package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

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


        db.collection("users").document(tokenID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                       Map<String,Object> user=task.getResult().getData();
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
                    }});

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
