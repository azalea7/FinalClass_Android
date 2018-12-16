package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
    ArrayAdapter<String> college;
    ArrayAdapter<String> major;
    ArrayAdapter<String> term;
    ArrayAdapter<String> careerv;
    ArrayAdapter<String> nums;
    ArrayAdapter<String> sec;

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
        Button SubmitBtn = findViewById(R.id.SubmitBtn);
        schoolSpinner = findViewById(R.id.school);
        college = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        college.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(college);

        schoolSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        Object item = parent.getItemAtPosition(pos);
                        schoolValue = item.toString();

                        selectedSchool = db.collection("colleges").document(schoolValue.split("\\s*-\\s*")[0]);
                        major.clear();
                        term.clear();
                        careerv.clear();
                        nums.clear();
                        sec.clear();
                        selectedSchool.collection("majors").get().
                                addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        for (DocumentSnapshot document : task1.getResult()) {
                                            major.add((String) document.getData().get(document.getId()));
                                        }
                                    } else {

                                    }
                                });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        subjectSpinner = findViewById(R.id.subject);
        major = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        major.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(major);

        subjectSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent1, View view1, int position1, long id1) {
                        Object maj = parent1.getItemAtPosition(position1);
                        majorValue = maj.toString();
                        term.clear();
                        careerv.clear();
                        nums.clear();
                        sec.clear();
                        selectedMajor = selectedSchool.collection("majors").document(majorValue.split("\\s*-\\s*")[0]);
                        selectedMajor.collection("terminfo").get().
                                addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        for (DocumentSnapshot document : task2.getResult()) {
                                            term.add(document.getId() + " - " + document.getData().get("name"));
                                        }
                                    } else {
                                    }
                                });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        TermSpinner = findViewById(R.id.term);
        term = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        term.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TermSpinner.setAdapter(term);

        TermSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent2, View view2, int position2, long id2) {
                        Object trm = parent2.getItemAtPosition(position2);
                        termValue = trm.toString();
                        careerv.clear();
                        nums.clear();
                        sec.clear();
                        selectedTerm = selectedMajor.collection("terminfo").document(termValue.split("\\s*-\\s*")[0]);
                        selectedTerm.collection("careerlevel").get().
                                addOnCompleteListener(task3 -> {
                                    if (task3.isSuccessful()) {
                                        for (DocumentSnapshot document : task3.getResult()) {
                                            careerv.add(document.getId() + " - " + document.getData().get("name"));
                                        }
                                    } else {
                                    }
                                });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        CareerSpinner = findViewById(R.id.career);
        careerv = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        careerv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CareerSpinner.setAdapter(careerv);

        CareerSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent3, View view3, int position3, long id3) {
                        Object car = parent3.getItemAtPosition(position3);
                        careerValue = car.toString();
                        nums.clear();
                        sec.clear();
                        selectedCareer = selectedTerm.collection("careerlevel")
                                .document(careerValue.split("\\s*-\\s*")[0]);
                        selectedCareer.collection("coursenumber").get().
                                addOnCompleteListener(task4 -> {

                                    if (task4.isSuccessful()) {
                                        for (DocumentSnapshot document : task4.getResult().getDocuments()) {
                                            nums.add(document.getId());
                                        }
                                    } else {
                                    }

                                });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        coursenumSpinner = findViewById(R.id.courseNumber);
        nums = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        nums.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursenumSpinner.setAdapter(nums);

        coursenumSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent4, View view4, int position4, long id4) {
                        Object cn = parent4.getItemAtPosition(position4);
                        numValue = cn.toString();
                        sec.clear();
                        selectedNum = selectedCareer.collection("coursenumber").document(numValue);
                        selectedNum.collection("sections").get().addOnCompleteListener(task5 -> {
                            if (task5.isSuccessful()) {
                                for (DocumentSnapshot document : task5.getResult()) {
                                    sec.add(document.getId() + "-" + document.getData().get("instructor") + " - " + document.getData().get("time"));
                                }
                            } else {
                            }

                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        sectionSpinner = findViewById(R.id.section);
        sec = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>());
        sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sectionSpinner.setAdapter(sec);

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
                });


        db.collection("colleges").get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    college.add(document.getId() + " - " + document.getData().get("name"));
                                }
                            }
                        }
                );


        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secValue == null) {
                    Toast.makeText(MainActivity.this, "Please Select all Options!", Toast.LENGTH_SHORT).show();
                    return;
                }
                addUserInfo();
                passToPriority();

                File storedLogin = new File(getApplicationContext().getFilesDir(), "user");
                if (!storedLogin.exists()) {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    finish();
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        });
    }

    private void addUserInfo() {
//        HashMap<String, Object> user = new HashMap<>();
        HashMap<String, HashMap<String, Object>> courses = new HashMap<>();
        HashMap<String, Object> userinfo = new HashMap<>();
        userinfo.put("college", schoolValue);
        userinfo.put("major", majorValue);
        userinfo.put("term", termValue);
        userinfo.put("course career", careerValue);
        userinfo.put("course number", numValue);
        userinfo.put("section", secValue.split("\\s*-\\s*")[0]);
        userinfo.put("instructor", secValue.split("\\s*-\\s*")[1]);
        userinfo.put("time", secValue.split("\\s*-\\s*")[2] + " - " + secValue.split("\\s*-\\s*")[3]);
        userinfo.put("status", "unregistered");
        courses.put(secValue.split("\\s*-\\s*")[0], userinfo);
//        user.put("selectedCourses", courses);

        FirebaseUtil.mergeToUser(courses);

    }

    private void passToPriority() {
        HashMap<String, Object> col = new HashMap<>();
        col.put("name", schoolValue.split("\\s*-\\s*")[1]);
        String tokenID = FirebaseUtil.tokenId();
        HashMap<String, Object> token = new HashMap<>();
        HashMap<String, HashMap<String, Object>> userToken = new HashMap();

        token.put("selectedtime", Timestamp.now());
        token.put("major", majorValue);
        token.put("course career", careerValue);
        token.put("course number", numValue);
        token.put("instructor", secValue.split("\\s*-\\s*")[1]);
        token.put("time", secValue.split("\\s*-\\s*")[2] + " - " + secValue.split("\\s*-\\s*")[3]);
        userToken.put(tokenID, token);


        HashMap<String, Object> c = new HashMap<>();
        c.put("career", careerValue.split("\\s*-\\s*")[1]);
        HashMap<String, Object> s = new HashMap<>();
        s.put("time", Timestamp.now());

        db.collection("priority1").document(schoolValue.split("\\s*-\\s*")[0])
                .set(col, SetOptions.merge());

        DocumentReference collegeDoc = db.collection("priority1").document(schoolValue.split("\\s*-\\s*")[0]);
        collegeDoc.collection(termValue.split("\\s*-\\s*")[0]).document(careerValue.split("\\s*-\\s*")[0])
                .set(c, SetOptions.merge());
        DocumentReference termDoc = collegeDoc.collection(termValue.split("\\s*-\\s*")[0]).document(careerValue.split("\\s*-\\s*")[0]);
        termDoc.collection(secValue.split("\\s*-\\s*")[0]).document(tokenID)
                .set(s, SetOptions.merge());

    }
}
