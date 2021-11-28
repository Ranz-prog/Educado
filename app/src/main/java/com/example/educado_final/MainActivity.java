package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText studentNumber, studentPassword;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        studentNumber = findViewById(R.id.Username);
        studentPassword = findViewById(R.id.Password);
        forgot = findViewById(R.id.forgot);

        TextView create = (TextView)findViewById(R.id.txtSignUp);
        Button Signin = (Button) findViewById(R.id.btnSignIn);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgotPassword .class);
                startActivity(intent);

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Signup_Choices.class);
                startActivity(intent);

            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(TextUtils.isEmpty(studentNumber.getText().toString()))
                {
                    Toast.makeText(MainActivity.this, "No empty keyword allowed", Toast.LENGTH_SHORT).show();
                }
                else{
                    final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("users");

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            String searchUser= studentNumber.getText().toString();
                            String searchPass = studentPassword.getText().toString();


                            if (dataSnapshot.child(searchUser).exists())
                            {

                                String passwordmo = dataSnapshot.child(searchUser).child("password").getValue().toString();

                                if(searchPass.equals(passwordmo)){

                                    if((dataSnapshot.child(searchUser).child("userType").getValue().toString()).equals("Teacher")){
                                        Intent intent = new Intent(MainActivity.this, Dashboard_Teacher.class);
                                        intent.putExtra("User", searchUser);
                                        studentNumber.setText("");
                                        studentPassword.setText("");
                                        startActivity(intent);
                                    }

                                    else if((dataSnapshot.child(searchUser).child("userType").getValue().toString()).equals("Student")){
                                        Intent intent = new Intent(MainActivity.this, Dashboard_Student.class);
//                                        String year = dataSnapshot.child(searchUser).child("year").getValue().toString();
                                        String name = dataSnapshot.child(searchUser).child("name").getValue().toString();
                                        intent.putExtra("User", name);
                                        intent.putExtra("studentNumber", searchUser);
//                                        intent.putExtra("Year", year);
//                                        intent.putExtra("Section", section);
                                        studentNumber.setText("");
                                        studentPassword.setText("");

                                        startActivity(intent);
                                    }


                                }

                                else{
                                    Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Student not registered", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }


}