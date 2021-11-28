package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Uploading extends AppCompatActivity {

    TextView title, time,description,pangalan,due;
    ImageView uploadedFile;

    String Title,Time,Name,Url,user,year,sec,des,date,state,periodic;
    int code;
    EditText addModule;
    String status;

    Button btn;

    Date d = new Date();
    CharSequence s  = DateFormat.format("dd/M/yyyy", d.getTime());

    StorageReference storageReference;
    DatabaseReference databaseReference,passedModulesWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_uploading);

        title = findViewById(R.id.textView15);
        time = findViewById(R.id.textView19);

        Name = getIntent().getStringExtra("Name");
        code = getIntent().getIntExtra("Code",-1);
        year = getIntent().getStringExtra("Year");
        user = getIntent().getStringExtra("User");
        Title = getIntent().getStringExtra("Title");
        Url = getIntent().getStringExtra("Url");
        sec = getIntent().getStringExtra("Section");
        Time = getIntent().getStringExtra("Time");
        des = getIntent().getStringExtra("Description");
        date = getIntent().getStringExtra("Date");
        periodic = getIntent().getStringExtra("Periodic");
        //state = getIntent().getStringExtra("State");

        pangalan = findViewById(R.id.textView49);
        description = findViewById(R.id.textView50);
        due = findViewById(R.id.textView52);

        pangalan.setText(Name);
        description.setText(des);
        due.setText("Due Date: "+date);

        addModule = findViewById(R.id.uploadFileStudent);

        btn = findViewById(R.id.btnUploadStudent);



        title.setText(s);
        time.setText(Time);

        uploadedFile = findViewById(R.id.imageView7);

        btn.setEnabled(false);
//        if (state.equals("close")){
//            addModule.setEnabled(false);
//            addModule.setHint("Module is currently closed\nContact your instructor for instructions");
//        }

        uploadedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);

            }
        });

        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPDF();
            }
        });
    }
    private void selectPDF() {

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(user).child(String.valueOf(code)).child(Name).child("pdf");
        passedModulesWeb = FirebaseDatabase.getInstance().getReference("passedModulesWeb").child(String.valueOf(code)).child(Name).child(user);

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
            addModule.setText(data.getDataString());

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
                String passed = s.toString();

                try{

                    SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");

                    String str1 = s.toString();
                    Date date1 = formatter.parse(str1);

                    String str2 = date;
                    Date date2 = formatter.parse(str2);

                    if (date1.compareTo(date2)<0)
                    {//date2 > than date1
//                        System.out.println("date2 is Greater than my date1");
                        status = "Passed on time";
                    }
                    else if (date1.compareTo(date2)==0)
                    {//date2 > than date1
//                        System.out.println("date2 is Greater than my date1");
                        status = "Passed on time";
                    }
                    else {
                        status = "Late";
                    }

                }catch (ParseException | java.text.ParseException e1){
                    e1.printStackTrace();
                }

                putPDFStudent putPDFStudent = new putPDFStudent(addModule.getText().toString(),uri.toString(),passed,status,periodic);

                databaseReference.child(Name).setValue(putPDFStudent);
                passedModulesWeb.setValue(putPDFStudent);
                Toast.makeText(Uploading.this,"File Upload", Toast.LENGTH_LONG).show();
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
}