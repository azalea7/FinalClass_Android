package edu.cuny.qc.cs.finalclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;

public class UserInfo extends AppCompatActivity {
    protected final String TAG = "Login";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
//        String row = "";
        listView=findViewById(R.id.courselist);

        ArrayList<String> courses=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,courses);
        listView.setAdapter(arrayAdapter);

        FirebaseUtil.authUser((authUser) -> {
            final DocumentReference docRef = db.collection("users").document(authUser.getUid());
            docRef.addSnapshotListener((snapshot, e) -> {
                Map<String,Object> user = snapshot.getData();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    user.forEach((k,v)->{
                        if(k.length()==5 || k.length()==4){
                                Map<String,Object> section= (Map<String, Object>) v;
                                String row ="Course number: "+section.get("course number")
                                        +"\nSection Number: " + k
                                        +"\nInstructor: "+ section.get("instructor")
                                        +"\nTime: " + section.get("time")
                                        +"\nStatus: " + section.get("status") + "\n";
                            courses.add(row);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    });
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            });

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(UserInfo.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete this section?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        courses.remove(positionToRemove);
                        arrayAdapter.notifyDataSetChanged();
                    }});
                adb.show();
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
