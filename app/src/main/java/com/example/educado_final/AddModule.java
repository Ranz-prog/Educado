package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AddModule extends AppCompatActivity {

    String time,sec,year,title;
    EditText moduleDay,moduleDes,addModule,Date;
    Button btn, back;
    int code;
    String[] studentNames;
    List<studentHelper> StudentList;
    List<putPDFStudent> EntryList;
    Spinner per;

    DatePickerDialog.OnDateSetListener setListener;

    StorageReference storageReference;
    DatabaseReference databaseReference,moduleStudent,studentNameReference,passedModulesWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_module);

        addModule = findViewById(R.id.txtFile);
        moduleDay = findViewById(R.id.txtActDay);
        moduleDes = findViewById(R.id.txtActDes);
        back = findViewById(R.id.btnCancel);
        btn = findViewById(R.id.btnCreate);
        per = (Spinner) findViewById(R.id.period);


        StudentList = new ArrayList<>();
        EntryList = new ArrayList<>();

        code = getIntent().getIntExtra("Code",-1);
        title = getIntent().getStringExtra("Title");
        year = getIntent().getStringExtra("Year");
        sec = getIntent().getStringExtra("Section");
        time = getIntent().getStringExtra("Time");

        btn.setEnabled(false);

        Date = findViewById(R.id.dueDate);

        Date.setFocusable(false);

        Date.setKeyListener(null);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddModule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.period));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        per.setAdapter(myAdapter);

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddModule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        Date.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });


        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPDF();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddModule.this, Classes.class);
                startActivity(intent);

            }
        });

    }

    private void selectPDF() {

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF File Select"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode==RESULT_OK && data != null && data.getData() != null){

            btn.setEnabled(true);
            addModule.setText(moduleDay.getText().toString());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadPDFFileFirebase(data.getData());

                }
            });
        }
    }

    private void uploadPDFFileFirebase (Uri data){

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("File is uploading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("uploadPDF" +System.currentTimeMillis()+ ".pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri uri = uriTask.getResult();
                String moduleName = addModule.getText().toString();
                String descrip = moduleDes.getText().toString();
                String url = uri.toString();
                String passing = Date.getText().toString();
                String periodic = per.getSelectedItem().toString();


                String subTitle = title;

                putPDFTeacher putPDFTeacher = new putPDFTeacher(moduleName,url,descrip,passing,subTitle,periodic);

                databaseReference.child(moduleName).setValue(putPDFTeacher);
                studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
                studentNameReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                            StudentList.add(studentHelper);
                        }
// student number to
                        studentNames = new String[StudentList.size()];
                        String[] pangalanNgStudent = new String[StudentList.size()];
                        for (int k = 0; k < studentNames.length; k++) {
                            studentNames[k] = StudentList.get(k).getStudentNo();
                            pangalanNgStudent[k] = StudentList.get(k).getName();

                            moduleStudent = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNames[k]).child(String.valueOf(code)).child(addModule.getText().toString()).child("pdf");
                            passedModulesWeb = FirebaseDatabase.getInstance().getReference("passedModulesWeb").child(String.valueOf(code)).child(addModule.getText().toString()).child(studentNames[k]);

                            String name = pangalanNgStudent[k];
                                    String url = "N/A";
                                    String passedDate = "N/A";
                                    String status = addModule.getText().toString()+" Missing";
                                    String periodic = per.getSelectedItem().toString();

                                    String subTitle = title;

                                    putPDFStudent putPDFStudent = new putPDFStudent(name,url,passedDate,status,periodic);

                                    moduleStudent.child(addModule.getText().toString()).setValue(putPDFStudent);
                                    passedModulesWeb.setValue(putPDFStudent);
                                }

//                title.setText(studentNames[1]);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Toast.makeText(AddModule.this,"File Upload", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress =(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File uploaded..." +(int)progress+"%");

            }
        });

    }

    private void RetrieveStudents() {



    }

}