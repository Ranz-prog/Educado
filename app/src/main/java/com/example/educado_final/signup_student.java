package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signup_student extends AppCompatActivity {

    EditText email, Name, Password,studentNumber;
    Button Create;

    FirebaseDatabase UsersDatabase;
    DatabaseReference reference,students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup_student);

        email = findViewById(R.id.txtEmail2);
        Name = findViewById(R.id.txtName2);
        Password = findViewById(R.id.txtpassword2);
        studentNumber = findViewById(R.id.txtSNumber);
        Create =  findViewById(R.id.btnReg2);
        TextView AHA = (TextView)findViewById(R.id.txtAHA2);

        AHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup_student.this, MainActivity.class);
                startActivity(intent);

            }
        });


        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty((email.getText().toString()))){
                    Toast.makeText(signup_student.this, "Please input email address", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((Name.getText().toString()))){
                    Toast.makeText(signup_student.this, "Please input Full Name", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((Password.getText().toString()))){
                    Toast.makeText(signup_student.this, "Please input password", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((studentNumber.getText().toString()))){
                    Toast.makeText(signup_student.this, "Please input student number", Toast.LENGTH_SHORT).show();
                }


                else{
                    final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users");

                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String searchUser = studentNumber.getText().toString();
                            String searchEmail = email.getText().toString();
                            String searchPass = Password.getText().toString();

                            if(dataSnapshot.child(searchUser).exists()) {
                                Toast.makeText(signup_student.this, "Student number already used", Toast.LENGTH_SHORT).show();
                            }


                            else{

                                if (email.getText().toString().toLowerCase().indexOf("@up.phinma.edu.ph") != -1) {


                                    UsersDatabase = FirebaseDatabase.getInstance();
                                    reference = UsersDatabase.getReference("users");
                                    students = UsersDatabase.getReference("Students");

                                    String emailAdd = email.getText().toString();
                                    String fName = Name.getText().toString();
                                    String password = Password.getText().toString();
                                    String studentNo = studentNumber.getText().toString();
                                    String userType = "Student";

                                    studentHelper studentHelper = new studentHelper(emailAdd, fName, password, studentNo, userType);
                                    Toast.makeText(signup_student.this, "USER REGISTERED", Toast.LENGTH_LONG).show();
                                    reference.child(studentNo).setValue(studentHelper);
                                    students.child(studentNo).setValue(studentHelper);
                                    Intent intent = new Intent(signup_student.this, MainActivity.class);
                                    startActivity(intent);
                                }

                                else{

                                    Toast.makeText(signup_student.this, "USER NOT FROM UPANG", Toast.LENGTH_LONG).show();

                                }


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