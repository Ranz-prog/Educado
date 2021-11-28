package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AddStudent extends AppCompatActivity {

    String time;
    String sec;
    String year;
    String title;
    String searchUser;
    int code;
    String studentName;
    TextView subTitle,subCode,subTime,noOfStudents;
    EditText studentNo;

    Button addStudent,viewStudent;

    StorageReference storageReference;
    FirebaseDatabase UsersDatabase;
    DatabaseReference reference,list,databaseReference,mRef,kref,studentAcc;

    ListView listView;
    List<studentHelper> EntryList;

    HashMap<String, String> nameAddresses = new HashMap<>();
    List<HashMap<String, String>> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_student);

        code = getIntent().getIntExtra("Code",-1);
        title = getIntent().getStringExtra("Title");
        year = getIntent().getStringExtra("Year");
        sec = getIntent().getStringExtra("Section");
        time = getIntent().getStringExtra("Time");

        subTitle = findViewById(R.id.textView31);
        subTime = findViewById(R.id.textView32);
        noOfStudents = findViewById(R.id.textView35);

        subTitle.setText(title);
        subTime.setText(time);

        listView = findViewById(R.id.studentList);
        EntryList = new ArrayList<>();

        addStudent = findViewById(R.id.addStudentBtn);

        studentNo = findViewById(R.id.StudentNo);
        viewStudent = findViewById(R.id.addStudentBtn2);

        viewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrieveList();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {
                studentHelper studentHelper = EntryList.get(intention);

                String studentNo = studentHelper.getStudentNo();
                String name = studentHelper.getName();

                Intent intent = new Intent(AddStudent.this,StudentProfile.class);

                intent.putExtra("Code", code);
                intent.putExtra("Year", year);
                intent.putExtra("Section", sec);
                intent.putExtra("Name", name);
                intent.putExtra("StudentNo", studentNo);

                startActivity(intent);
            }
        });

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(TextUtils.isEmpty(studentNo.getText().toString()))
                {
                    Toast.makeText(AddStudent.this, "No empty keyword allowed", Toast.LENGTH_SHORT).show();
                }
                else{
                    mRef=  FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
                    kref = FirebaseDatabase.getInstance().getReference().child("users");
                    searchUser= studentNo.getText().toString();
                    kref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            studentName = snapshot.child(searchUser).child("name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                            if (dataSnapshot.child(searchUser).exists())
                            {
                                Toast.makeText(AddStudent.this, "Student is already added to the class", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                list = FirebaseDatabase.getInstance().getReference("JoinClasses").child(searchUser).child(String.valueOf(code));

                                addStudentsJoined addStudentsJoined = new addStudentsJoined(studentName,searchUser);
                                addStudentHelper addStudentHelper = new addStudentHelper(searchUser,title,year,sec,time,code);

                                list.setValue(addStudentHelper);
                                mRef.child(searchUser).setValue(addStudentsJoined);
                                Toast.makeText(AddStudent.this,"STUDENT ADDED",Toast.LENGTH_LONG).show();
                                studentNo.setText("");

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

    private void RetrieveList() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined").child(studentNo.getText().toString());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                    EntryList.add(studentHelper);
                }

                String[] uploadsName= new String[EntryList.size()];

                for (int i=0;i<uploadsName.length; i++){

                    uploadsName[i]=EntryList.get(i).getStudentNo();
                    nameAddresses.put(EntryList.get(i).getStudentNo(), EntryList.get(i).getName());
                }


                SimpleAdapter adapter = new SimpleAdapter(AddStudent.this, listItems, R.layout.list_student,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.taas, R.id.baba});

                if(listItems.size()>0) {
                    listItems.clear();
                }

                TreeMap<String, String> map = new TreeMap<String, String>(nameAddresses);
                Set set2 = map.entrySet();

                Iterator iterator2 = set2.iterator();
                while (iterator2.hasNext())
                {
                    HashMap<String, String> resultsMap = new HashMap<>();
                    Map.Entry pair = (Map.Entry)iterator2.next();
                    resultsMap.put("First Line", pair.getKey().toString());
                    resultsMap.put("Second Line", pair.getValue().toString());

                    listItems.add(resultsMap);
                }
                listView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}