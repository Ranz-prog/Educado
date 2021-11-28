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

public class signup_teacher extends AppCompatActivity {

    EditText UserName,Pass, EmailIn;
    Button Register;

    FirebaseDatabase UsersDatabase;
    DatabaseReference reference,list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup_teacher);

        EmailIn = findViewById(R.id.txtEmail);
        UserName = findViewById(R.id.txtName);
        Pass = findViewById(R.id.txtpassword);
        Register = (Button) findViewById(R.id.btnReg);


        TextView AHA = (TextView)findViewById(R.id.txtAHA);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty((UserName.getText().toString()))){
                    Toast.makeText(signup_teacher.this, "Please input your fullname", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((EmailIn.getText().toString()))){
                    Toast.makeText(signup_teacher.this, "Please input your Email Address", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty((Pass.getText().toString()))){
                    Toast.makeText(signup_teacher.this, "Please input your Password", Toast.LENGTH_SHORT).show();
                }

                else{
                    final DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users");

                    mref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String searchUser = UserName.getText().toString();
                            String searchPass = Pass.getText().toString();
                            String searchemail = EmailIn.getText().toString();


                            if(dataSnapshot.child(searchUser).exists()){
                                Toast.makeText(signup_teacher.this,"User registered",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(signup_teacher.this, MainActivity.class);
                                startActivity(intent);
                            }


                            else if(dataSnapshot.child(searchPass).exists()){
                                Toast.makeText(signup_teacher.this,"Password already in used",Toast.LENGTH_SHORT).show();
                            }

                            else{

                                if (EmailIn.getText().toString().toLowerCase().indexOf("@up.phinma.edu.ph") != -1) {

                                    UsersDatabase = FirebaseDatabase.getInstance();
                                    reference = UsersDatabase.getReference("users");
                                    list = UsersDatabase.getReference("teachers");

                                    String userName = UserName.getText().toString();
                                    String email = EmailIn.getText().toString();
                                    String password = Pass.getText().toString();
                                    String userType = "Teacher";

                                    teacherHelper teacherHelper = new teacherHelper(email, password, userName, userType);

                                    reference.child(userName).setValue(teacherHelper);
                                    list.child(userName).setValue(teacherHelper);

                                    Toast.makeText(signup_teacher.this, "USER REGISTERED", Toast.LENGTH_LONG).show();
                                }

                                else{
                                    Toast.makeText(signup_teacher.this, "USER NOT FROM UPANG", Toast.LENGTH_LONG).show();


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





        AHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup_teacher.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
}