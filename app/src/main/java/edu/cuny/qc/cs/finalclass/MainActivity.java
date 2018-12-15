package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainUtil";

    Spinner schoolSpinner;
    Spinner subjectSpinner;
    Spinner TermSpinner;
    Spinner CareerSpinner;
    String schoolValue;
    String majorValue;
    List<String> college = new ArrayList<>();
    List<String> major = new ArrayList<>();
    List<String> term = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference selectedSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        schoolSpinner = (Spinner) findViewById(R.id.school);
        db.collection("colleges").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            college.add(document.getId()+" - "+document.getData().get("name"));
//                            college.add((String) document.getData().get("name"));
                        }


                        ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_item, college);
                        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        schoolSpinner.setAdapter(schoolAdapter);
                        schoolSpinner.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                        Object item = parent.getItemAtPosition(pos);
                                        schoolValue = item.toString();

                                        selectedSchool = db.collection("colleges").document(schoolValue.split("\\s*-\\s*")[0]);
                                        major.clear();
                                        selectedSchool.collection("majors").get().
                                                addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        for (DocumentSnapshot document : task1.getResult()) {
                                                            major.add((String) document.getData().get(document.getId()));
                                                        }
                                                    } else {

                                                    }
                                                    subjectSpinner = (Spinner) findViewById(R.id.subject);
                                                    ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(MainActivity.this,
                                                            android.R.layout.simple_spinner_item, major);
                                                    subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    subjectSpinner.setAdapter(subjectAdapter);

                                                    subjectSpinner.setOnItemSelectedListener(
                                                            new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> parent1, View view1, int position1, long id1) {
                                                                    Object maj = parent1.getItemAtPosition(position1);
                                                                    majorValue = maj.toString();
                                                                    term.clear();
                                                                    DocumentReference selectedMajor = selectedSchool.collection("majors").document(majorValue.split("\\s*-\\s*")[0]);
                                                                    selectedMajor.collection("terminfo").get().
                                                                            addOnCompleteListener(task2 -> {
                                                                                if (task2.isSuccessful()){
                                                                                    for (DocumentSnapshot document : task2.getResult()) {
                                                                                        term.add(document.getId()+" - "+document.getData().get("name"));
                                                                                    }
                                                                                }else{}

                                                                                TermSpinner = (Spinner) findViewById(R.id.term);

                                                                                ArrayAdapter<String> termAdapter =new ArrayAdapter<String>(MainActivity.this,
                                                                                        android.R.layout.simple_spinner_item, term);

                                                                                termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                                TermSpinner.setAdapter(termAdapter);
















                                                                            });
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> parent) {

                                                                }
                                                            }
                                                    );
                                                });
                                    }

                                    public void onNothingSelected(AdapterView<?> parent) {
                                        System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
                                    }
                                });
                    }
                    else{
                        //TODO: else part;
                    }
                });


          //  System.out.println("0000000000000000000000000000000000000");


        CareerSpinner = (Spinner) findViewById(R.id.career);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.CareerStr, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CareerSpinner.setAdapter(adapter2);


//        DocumentReference doc=db.collection("colleges").document("QNS01");

//        Task<QuerySnapshot> doc=db.collection("colleges").get();
//        college.add(doc.getResult().getDocuments().get(0).getId());
//        college.add(doc.getId());

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

    }
}
