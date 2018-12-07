package edu.cuny.qc.cs.finalclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner schoolSpinner = (Spinner) findViewById(R.id.school);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.SchoolList, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        schoolSpinner.setAdapter(adapter1);

        Spinner CareerSpinner = (Spinner) findViewById(R.id.career);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.CareerStr, android.R.layout.simple_spinner_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CareerSpinner.setAdapter(adapter2);

        Spinner TermSpinner = (Spinner) findViewById(R.id.term);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.TermInfo, android.R.layout.simple_spinner_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        TermSpinner.setAdapter(adapter3);

        Button SubmitBtn = (Button) findViewById(R.id.SubmitBtn);
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

    public void launchLoginActivity(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
