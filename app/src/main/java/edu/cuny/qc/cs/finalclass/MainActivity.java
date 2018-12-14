package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainUtil";

    Spinner schoolSpinner;
    Spinner CareerSpinner;
    Spinner TermSpinner;
    EditText emailText;
    List<String> college;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CareerSpinner = (Spinner) findViewById(R.id.career);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.CareerStr, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CareerSpinner.setAdapter(adapter2);

        TermSpinner = (Spinner) findViewById(R.id.term);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.TermInfo, android.R.layout.simple_spinner_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        TermSpinner.setAdapter(adapter3);

        emailText = (EditText) findViewById(R.id.emailtext);
        college = new ArrayList<>();
        db.collection("colleges").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                college.add(document.getId() + " => " + document.getData());
                                college.add("hello");
                                Log.d("college", document.getId() + " => " + document.getData());
                            }

                        }else {
                            college.add("false");
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//        DocumentReference doc=db.collection("colleges").document("QNS01");

//        Task<QuerySnapshot> doc=db.collection("colleges").get();
//        college.add(doc.getResult().getDocuments().get(0).getId());
//        college.add(doc.getId());
        schoolSpinner = (Spinner) findViewById(R.id.school);
        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, college);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolAdapter);
        Button SubmitBtn = (Button) findViewById(R.id.SubmitBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserInfo();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    public void launchLoginActivity(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void addUserInfo(){
        String college = schoolSpinner.getSelectedItem().toString();
        String email = emailText.getText().toString().trim();
        if (!TextUtils.isEmpty(email)){

        }else{
            Toast.makeText(this, "Please enter an Email!", Toast.LENGTH_LONG).show();
        }
    }
}
