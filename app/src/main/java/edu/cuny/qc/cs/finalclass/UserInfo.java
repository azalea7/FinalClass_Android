package edu.cuny.qc.cs.finalclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;

public class UserInfo extends AppCompatActivity {
    protected final String TAG = "Login";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        final String[] tokenID = {""};
        listView=findViewById(R.id.courselist);
        ArrayList<String> secNum = new ArrayList<>();
        ArrayList<String> information=new ArrayList<>();
        ArrayList<String> courses=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,courses);
        listView.setAdapter(arrayAdapter);

        FirebaseUtil.authUser((authUser) -> {
            docRef = db.collection("users").document(authUser.getUid());
            tokenID[0] = authUser.getUid();
            docRef.addSnapshotListener((snapshot, e) -> {
                if(snapshot==null) return;
                Map<String,Object> user = snapshot.getData();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    arrayAdapter.clear();
                    user.forEach((k,v)->{
                        if(k.length()==5 || k.length()==4){
                                Map<String,Object> section= (Map<String, Object>) v;
                                String row ="Course number: "+section.get("course number")
                                        +"\nSection Number: " + k
                                        +"\nInstructor: "+ section.get("instructor")
                                        +"\nTime: " + section.get("time")
                                        +"\nStatus: " + section.get("status") + "\n";
                                String info=section.get("college")+"&"+section.get("term")+"&"+section.get("course career");
                                System.out.println(info+"*********************************");
                                information.add(info);
                                secNum.add(k);
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
                adb.setMessage("Are you sure you want to delete this section "+secNum.get(position)+"?");

                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        courses.remove(positionToRemove);
                        String sectionID = secNum.get(positionToRemove);
                        secNum.remove(positionToRemove);
                        arrayAdapter.notifyDataSetChanged();
                        Map<String,Object> updates = new HashMap<>();
                        String[] info =information.get(positionToRemove).split("&");

                        updates.put(sectionID, FieldValue.delete());
                        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        db.collection("priority1").document(info[0].split("\\s*-\\s*")[0])
                        .collection(info[1].split("\\s*-\\s*")[0]).document(info[2].split("\\s*-\\s*")[0])
                        .collection(sectionID).document(tokenID[0])
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
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
