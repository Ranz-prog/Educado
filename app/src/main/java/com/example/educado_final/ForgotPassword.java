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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ForgotPassword extends AppCompatActivity {
    EditText user, emailAdd, code ,newPass;
    Button email,reset;
    String generatedCode,Trapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.emailbtn);
        user = findViewById(R.id.username);
        emailAdd = findViewById(R.id.emailadd);
        code = findViewById(R.id.code);
        newPass = findViewById(R.id.newpass);
        reset = findViewById(R.id.resetpassbtn);
        int randomPIN = (int)(Math.random()*9000)+1000;
        generatedCode = String.valueOf(randomPIN);


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(user.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your Account Name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(emailAdd.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
                }
                else{
                    final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("users");

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            String searchUser= user.getText().toString();
                            String searchEmail= emailAdd.getText().toString();


                            if (dataSnapshot.child(searchUser).exists())
                            {



                                String emailMo = dataSnapshot.child(searchUser).child("emailAdd").getValue().toString();
                                String accName = dataSnapshot.child(searchUser).child("name").getValue().toString();

                                if(searchEmail.equals(emailMo)){
                                    Log.i("SendMailActivity", "Send Button Clicked.");

                                    String fromEmail = "educadopak@gmail.com";
                                    String fromPassword = "educadotayo";
                                    String toEmails = searchEmail;
                                    List<String> toEmailList = Arrays.asList(toEmails
                                            .split("\\s*,\\s*"));
                                    Log.i("SendMailActivity", "To List: " + toEmailList);
                                    String emailSubject = generatedCode+ "  is your Educado account recovery code";
                                    String emailBody = "Hi "+accName +"" +
                                            "" +
                                            "We received a request to reset your Educado password." +
                                            "" +
                                            "Enter the following password reset code:" +
                                            ""+generatedCode;
                                    new SendMailTask(ForgotPassword.this).execute(fromEmail,
                                            fromPassword, toEmailList, emailSubject, emailBody);

                                    String otp = generatedCode;
                                    Trapped = generatedCode;

                                    codeGeneratorHelper codeGeneratorHelper = new codeGeneratorHelper(otp);

                                    mRef.child(searchUser).child("otp").setValue(codeGeneratorHelper);

                                    email.setEnabled(false);



                                }

                                else{
                                    Toast.makeText(ForgotPassword.this, "Email does not match inputted user account", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(ForgotPassword.this, "User not registered", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(user.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your Account Name", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(emailAdd.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
                }

                else if(TextUtils.isEmpty(code.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please Enter your OTP code from your email", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(newPass.getText().toString()))
                {
                    Toast.makeText(ForgotPassword.this, "Please enter your new Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    final DatabaseReference mRef= FirebaseDatabase.getInstance().getReference().child("users");

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            String searchUser= user.getText().toString();
                            String searchEmail= emailAdd.getText().toString();


                            if (dataSnapshot.child(searchUser).exists())
                            {
                                //Teacher

                                if((dataSnapshot.child(searchUser).child("userType").getValue().toString()).equals("Teacher")){
                                    if((dataSnapshot.child(searchUser).child("otp").child("otp").getValue().toString()).equals(code.getText().toString())) {

                                        String emailMo = dataSnapshot.child(searchUser).child("emailAdd").getValue().toString();
                                        String accName = dataSnapshot.child(searchUser).child("name").getValue().toString();
                                        String password = newPass.getText().toString();
                                        String userType = dataSnapshot.child(searchUser).child("userType").getValue().toString();

                                        teacherHelper teacherHelper = new teacherHelper(emailMo, password, accName, userType);
                                        mRef.child(accName).setValue(teacherHelper);
                                        Toast.makeText(ForgotPassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(ForgotPassword.this, "Code does not match.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                //student
                                else if((dataSnapshot.child(searchUser).child("userType").getValue().toString()).equals("Student")){
                                    if((dataSnapshot.child(searchUser).child("otp").child("otp").getValue().toString()).equals(code.getText().toString())){
                                        //final DatabaseReference deleteOtp= FirebaseDatabase.getInstance().getReference().child("users");

                                        //deleteOtp.child(searchUser).child("otp").child("otp").removeValue();

                                        String emailMo = dataSnapshot.child(searchUser).child("emailAdd").getValue().toString();
                                        String accName = dataSnapshot.child(searchUser).child("name").getValue().toString();
                                        String password = newPass.getText().toString();
                                        String studentNo = dataSnapshot.child(searchUser).child("studentNo").getValue().toString();
                                        String userType = dataSnapshot.child(searchUser).child("userType").getValue().toString();

                                        studentHelper studentHelper = new studentHelper(emailMo,accName,password,studentNo,userType);

                                        mRef.child(studentNo).setValue(studentHelper);
                                        Toast.makeText(ForgotPassword.this, "Password Changed", Toast.LENGTH_SHORT).show();

                                    }

                                    else{
                                        Toast.makeText(ForgotPassword.this, "Code does not match.", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            }
                            else{
                                Toast.makeText(ForgotPassword.this, "User not registered", Toast.LENGTH_SHORT).show();

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