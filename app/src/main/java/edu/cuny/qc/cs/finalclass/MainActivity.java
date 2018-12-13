package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Spinner schoolSpinner;
    Spinner CareerSpinner;
    Spinner TermSpinner;
    EditText emailText;

    DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        schoolSpinner = (Spinner) findViewById(R.id.school);
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
//                R.array.SchoolList, android.R.layout.simple_spinner_item);
//
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        schoolSpinner.setAdapter(adapter1);

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

        userInfo.child("colleges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> college = new ArrayList<String>();
                for (DataSnapshot collegeSnapshot: dataSnapshot.getChildren() ){
                    String collegeName = collegeSnapshot.child("name").getValue(String.class);
                    college.add(collegeName);
//                    User collegeName = collegeSnapshot.getValue(User.class);
//                    college.add(collegeName.name);

                }
                schoolSpinner = (Spinner) findViewById(R.id.school);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, college);

                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                schoolSpinner.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
