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
import java.util.HashMap;
import java.util.List;

import edu.cuny.qc.cs.finalclass.lib.FirebaseUtil;

public class MainActivity extends AppCompatActivity {

    Spinner schoolSpinner;
    Spinner subjectSpinner;
    Spinner TermSpinner;
    Spinner CareerSpinner;
    Spinner coursenumSpinner;
    Spinner sectionSpinner;
    String schoolValue;
    String majorValue;
    String termValue;
    String careerValue;
    String numValue;
    String secValue;
    List<String> college = new ArrayList<>();
    List<String> major = new ArrayList<>();
    List<String> term = new ArrayList<>();
    List<String> careerv = new ArrayList<>();
    List<String> nums = new ArrayList<>();
    List<String> sec = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference selectedSchool;
    DocumentReference selectedMajor;
    DocumentReference selectedTerm;
    DocumentReference selectedCareer;
    DocumentReference selectedNum;

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
                                                                    selectedMajor = selectedSchool.collection("majors").document(majorValue.split("\\s*-\\s*")[0]);
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

                                                                                TermSpinner.setOnItemSelectedListener(
                                                                                        new AdapterView.OnItemSelectedListener() {
                                                                                            @Override
                                                                                            public void onItemSelected(AdapterView<?> parent2, View view2, int position2, long id2) {
                                                                                                Object trm = parent2.getItemAtPosition(position2);
                                                                                                termValue = trm.toString();
                                                                                                careerv.clear();
                                                                                                selectedTerm = selectedMajor.collection("terminfo").document(termValue.split("\\s*-\\s*")[0]);
                                                                                                selectedTerm.collection("careerlevel").get().
                                                                                                        addOnCompleteListener(task3 -> {
                                                                                                           if (task3.isSuccessful()){
                                                                                                               for(DocumentSnapshot document : task3.getResult()){
                                                                                                                   careerv.add(document.getId()+" - "+document.getData().get("name"));
                                                                                                               }
                                                                                                           }else{}

                                                                                                            CareerSpinner = (Spinner) findViewById(R.id.career);
                                                                                                            ArrayAdapter<String> careerAdapter = new ArrayAdapter<String>(MainActivity.this,
                                                                                                                    android.R.layout.simple_spinner_item, careerv);
                                                                                                            careerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                                            CareerSpinner.setAdapter(careerAdapter);

                                                                                                            CareerSpinner.setOnItemSelectedListener(
                                                                                                                    new AdapterView.OnItemSelectedListener() {
                                                                                                                        @Override
                                                                                                                        public void onItemSelected(AdapterView<?> parent3, View view3, int position3, long id3) {
                                                                                                                            Object car = parent3.getItemAtPosition(position3);
                                                                                                                            careerValue = car.toString();
                                                                                                                            nums.clear();
                                                                                                                            selectedCareer = selectedTerm.collection("careerlevel").document(careerValue.split("\\s*-\\s*")[0]);
                                                                                                                            selectedCareer.collection("coursenumber").get().
                                                                                                                                    addOnCompleteListener(task4 -> {
                                                                                                                                        System.out.println(task4.toString());

                                                                                                                                        if(task4.isSuccessful()){
                                                                                                                                            System.out.println(task4.getResult().getDocuments().toString());
                                                                                                                                            for(DocumentSnapshot document : task4.getResult().getDocuments()){
                                                                                                                                                nums.add(document.getId());
                                                                                                                                            }
                                                                                                                                        }else{}

                                                                                                                                        coursenumSpinner = (Spinner) findViewById(R.id.courseNumber);
                                                                                                                                        ArrayAdapter<String> courseNumAdapter =  new ArrayAdapter<String>(MainActivity.this,
                                                                                                                                                android.R.layout.simple_spinner_item, nums);
                                                                                                                                        courseNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                                                                        coursenumSpinner.setAdapter(courseNumAdapter);

                                                                                                                                        coursenumSpinner.setOnItemSelectedListener(
                                                                                                                                                new AdapterView.OnItemSelectedListener() {
                                                                                                                                                    @Override
                                                                                                                                                    public void onItemSelected(AdapterView<?> parent4, View view4, int position4, long id4) {
                                                                                                                                                        Object cn = parent4.getItemAtPosition(position4);
                                                                                                                                                        numValue = cn.toString();
                                                                                                                                                        sec.clear();
                                                                                                                                                        selectedNum = selectedCareer.collection("coursenumber").document(numValue);
                                                                                                                                                        selectedNum.collection("sections").get().addOnCompleteListener(task5 -> {
                                                                                                                                                           if(task5.isSuccessful()){
                                                                                                                                                               for (DocumentSnapshot document : task5.getResult()){
                                                                                                                                                                   sec.add(document.getId());
                                                                                                                                                               }
                                                                                                                                                           }else{}

                                                                                                                                                           sectionSpinner = (Spinner) findViewById(R.id.section);
                                                                                                                                                            ArrayAdapter<String> sectionAdapter =  new ArrayAdapter<String>(MainActivity.this,
                                                                                                                                                                    android.R.layout.simple_spinner_item, sec);
                                                                                                                                                            sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                                                                                                            sectionSpinner.setAdapter(sectionAdapter);
                                                                                                                                                            sectionSpinner.setOnItemSelectedListener(
                                                                                                                                                                    new AdapterView.OnItemSelectedListener() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onItemSelected(AdapterView<?> parent5, View view5, int position5, long id5) {
                                                                                                                                                                            Object se = parent5.getItemAtPosition(position5);
                                                                                                                                                                            secValue = se.toString();
                                                                                                                                                                        }

                                                                                                                                                                        @Override
                                                                                                                                                                        public void onNothingSelected(AdapterView<?> parent) {
                                                                                                                                                                            System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
                                                                                                                                                                        }
                                                                                                                                                                    }
                                                                                                                                                            );
                                                                                                                                                        });
                                                                                                                                                    }

                                                                                                                                                    @Override
                                                                                                                                                    public void onNothingSelected(AdapterView<?> parent) {
                                                                                                                                                        System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                        );


                                                                                                                                    });
                                                                                                                        }

                                                                                                                        @Override
                                                                                                                        public void onNothingSelected(AdapterView<?> parent) {
                                                                                                                            System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
                                                                                                                        }
                                                                                                                    }
                                                                                                            );
                                                                                                        });
                                                                                            }

                                                                                            @Override
                                                                                            public void onNothingSelected(AdapterView<?> parent) {
                                                                                                System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
                                                                                            }
                                                                                        }
                                                                                );
                                                                            });
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> parent) {
                                                                    System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
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
                        System.out.println("NOTHING SELECTED!!!!!!!!!!!!!!!!!!!!");
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
        HashMap<String, Object> userinfo = new HashMap<>();
        userinfo.put("college", schoolValue);
        userinfo.put("major", majorValue);
        userinfo.put("term", termValue);
        userinfo.put("course career", careerValue);
        userinfo.put("course number", numValue);
        userinfo.put("section",secValue);

        FirebaseUtil.mergeToFirestoreUser(userinfo);

    }
}
