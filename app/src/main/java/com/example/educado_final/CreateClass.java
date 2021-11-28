package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class CreateClass extends AppCompatActivity {

    Spinner yearSpinner;
    EditText title,code,Araw,Oras,Sec;

    String user;

    Button cancel, create;

    FirebaseDatabase UsersDatabase;
    DatabaseReference reference,list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_class);

        title = findViewById(R.id.subjectTitle);
        code = findViewById(R.id.subjectCode);
        Araw = findViewById(R.id.day);
        Oras = findViewById(R.id.time);
        Sec = findViewById(R.id.Section);

        create = findViewById(R.id.btnCreateClass);

        user = getIntent().getStringExtra("User");

        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateClass.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.year));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(myAdapter);



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty((code.getText().toString()))){
                    Toast.makeText(CreateClass.this, "Please input Class Code", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((title.getText().toString()))){
                    Toast.makeText(CreateClass.this, "Please input Subject Title", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((Araw.getText().toString()))){
                    Toast.makeText(CreateClass.this, "Please input Day", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty((Oras.getText().toString()))){
                    Toast.makeText(CreateClass.this, "Please input Time", Toast.LENGTH_SHORT).show();
                }

                else{
                    final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("Classrooms").child(yearSpinner.getSelectedItem().toString()).child(Sec.getText().toString()).child(code.getText().toString());

                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String subCode = code.getText().toString();
                            String subTitle = title.getText().toString();



                            if(dataSnapshot.child(subCode).exists() || dataSnapshot.child(subTitle).exists()){
                                Toast.makeText(CreateClass.this,"Subject Already Created",Toast.LENGTH_SHORT).show();
                            }

                            else{

                                UsersDatabase = FirebaseDatabase.getInstance();
                                reference = UsersDatabase.getReference("Classrooms").child(yearSpinner.getSelectedItem().toString()).child(Sec.getText().toString()).child(code.getText().toString());
                                list = UsersDatabase.getReference("CreatedRooms");
                                String CUDE = code.getText().toString();
                                Integer Code = Integer.valueOf(CUDE);

                                String title =subTitle;
                                String year = yearSpinner.getSelectedItem().toString();
                                String sec = Sec.getText().toString();
                                String teacher = user;
                                String date = Araw.getText().toString();
                                String time = Oras.getText().toString();

                                classHelper classHelper = new classHelper(Code, title, year, sec, teacher, date,time);

                                reference.setValue(classHelper);
                                list.child(user).child(Code.toString()).setValue(classHelper);


                                Toast.makeText(CreateClass.this,"CLASS CREATED",Toast.LENGTH_LONG).show();

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });






    }
}