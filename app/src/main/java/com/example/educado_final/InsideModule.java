package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class InsideModule extends AppCompatActivity {

    TextView title, time,modulename, description,dueDate;
    ImageView uploadedFile;
    Spinner Sort;

    String Title,Time,Name,Url,sec,year,des,date,state;
    int code;
    int [] positionValues;


    String[] studentNames;

    ListView listView;
    List<putPDFStudent> EntryList;
    List<studentHelper> StudentList;

    HashMap<String, String> nameAddresses = new HashMap<>();
    List<HashMap<String, String>> listItems = new ArrayList<>();

    Date d = new Date();
    CharSequence currentDate  = DateFormat.format("dd/M/yyyy", d.getTime());

    StorageReference storageReference;
    DatabaseReference databaseReference,studentNameReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_inside_module);

        listView =  findViewById(R.id.passedStudents);
        EntryList = new ArrayList<>();
        StudentList = new ArrayList<>();


        title = findViewById(R.id.textView37);
        time = findViewById(R.id.textView38);
        modulename = findViewById(R.id.textView47);

        dueDate = findViewById(R.id.calendarOutput);
        description = findViewById(R.id.textView48);


        code = getIntent().getIntExtra("Code", -1);
        year = getIntent().getStringExtra("Year");
        sec = getIntent().getStringExtra("Section");




        Name = getIntent().getStringExtra("Name");
        Title = getIntent().getStringExtra("Title");
        Url = getIntent().getStringExtra("Url");
        Time = getIntent().getStringExtra("Time");

        des = getIntent().getStringExtra("Description");
        date = getIntent().getStringExtra("Date");


        dueDate.setText("Due Date: "+date);
        description.setText(des);

        title.setText(Name);
        time.setText(Time);
        modulename.setText(Name);

        uploadedFile = findViewById(R.id.imageView10);
        Sort = (Spinner) findViewById(R.id.sorting);



        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(InsideModule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sort));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sort.setAdapter(myAdapter);

        Sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        listView.setAdapter(null);
                        nameAddresses.clear();

                        studentNames=null;
                        StudentList.clear();
                        EntryList.clear();
                        listItems.clear();
                        databaseReference = null;
                        studentNameReference=null;

                        RetrieveList();
                        break;
                    case 1:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        studentNames=null;
                        StudentList.clear();
                        EntryList.clear();
                        listItems.clear();
                        OnTime();
                        break;

                    case 2:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        studentNames=null;
                        StudentList.clear();
                        EntryList.clear();
                        listItems.clear();
                        Late();
                        break;

                    case 3:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        studentNames=null;
                        StudentList.clear();
                        EntryList.clear();
                        listItems.clear();
                        Missing();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        uploadedFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {
                putPDFStudent putPDFStudent = EntryList.get(positionValues[intention]);

                if (putPDFStudent.getUrl().equals("N/A")){Toast.makeText(InsideModule.this,"Student hasn't uploaded Module", Toast.LENGTH_LONG).show();
                }
                else {

                    String Url = putPDFStudent.getUrl();
//                    Intent intent = new Intent();
//                    intent.setType(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(putPDFStudent.getUrl()));

                    Intent intent = new Intent(InsideModule .this,viewPdfUsingApp.class);
                    intent.putExtra("Url", Url);
                    startActivity(intent);
                }
            }
        });

    }

    private void RetrieveList() {

    studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
    studentNameReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                StudentList.add(studentHelper);
            }

            studentNames = new String[StudentList.size()];
            for (int k = 0; k < studentNames.length; k++) {
                studentNames[k] = StudentList.get(k).getStudentNo();

                databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNames[k]).child(String.valueOf(code)).child(Name).child("pdf");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            putPDFStudent putPDFStudent = ds.getValue(com.example.educado_final.putPDFStudent.class);
                            EntryList.add(putPDFStudent);
                        }

                        String[] uploadsName = new String[EntryList.size()];
                        positionValues = new int[EntryList.size()];
                        int pos = 0;

                        for (int i = 0; i < uploadsName.length; i++) {


//                            nameAddresses.put(EntryList.get(i).getStatus(), EntryList.get(i).getName());
                            //uploadsName[i] = EntryList.get(i).getName();

                            if (EntryList.get(i).getStatus().equals("Passed on time")){
                                nameAddresses.put(studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());
                                positionValues[pos] = i;
                                pos++;
                            }
                            else if (EntryList.get(i).getStatus().equals("Late")){
                                nameAddresses.put(studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());
                                positionValues[pos] = i;
                                pos++;
                            }
                            else if (EntryList.get(i).getStatus().equals(Name + " Missing")){
                                nameAddresses.put( studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());
                                positionValues[pos] = i;
                                pos++;
                            }
                            else {
                                uploadsName[i] = EntryList.get(i).getName();
                            }


                        }



                        uploadsName = null;
                        SimpleAdapter adapter = new SimpleAdapter(InsideModule.this, listItems, R.layout.list_passed_modules,
                                new String[]{"First Line", "Second Line"},
                                new int[]{R.id.nameOfModule, R.id.statusOfModule});

                        if(listItems.size()>0)
                            listItems.clear();

                        TreeMap<String, String> map = new TreeMap<String, String>(nameAddresses);
                        Set set2 = map.entrySet();



                        Iterator iterator2 = set2.iterator();
                        while (iterator2.hasNext())
                        {

                            HashMap<String, String> resultsMap = new HashMap<>();
                            Map.Entry pair = (Map.Entry)iterator2.next();

//                            Map.Entry pair2 = (Map.Entry)iterator2.next();
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
//                title.setText(studentNames[1]);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });


}

    private void OnTime() {

        studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
        studentNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                    StudentList.add(studentHelper);
                }

                studentNames = new String[StudentList.size()];
                for (int k = 0; k < studentNames.length; k++) {
                    studentNames[k] = StudentList.get(k).getStudentNo();

                    databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNames[k]).child(String.valueOf(code)).child(Name).child("pdf");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDFStudent putPDFStudent = ds.getValue(com.example.educado_final.putPDFStudent.class);
                                EntryList.add(putPDFStudent);
                            }

                            String[] uploadsName = new String[EntryList.size()];
                            positionValues = new int[EntryList.size()];
                            int pos = 0;
                            for (int i = 0; i < uploadsName.length; i++) {
                                if (EntryList.get(i).getStatus().equals("Passed on time")){
                                    nameAddresses.put(studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());

                                    positionValues[pos] = i;
                                    pos++;
                                }
                                else {
                                    uploadsName[i] = EntryList.get(i).getName();
                                }
                            }
                            uploadsName = null;


                            SimpleAdapter adapter = new SimpleAdapter(InsideModule.this, listItems, R.layout.list_passed_modules,
                                    new String[]{"First Line", "Second Line"},
                                    new int[]{R.id.nameOfModule, R.id.statusOfModule});

                            if(listItems.size()>0)
                                listItems.clear();
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
//                title.setText(studentNames[1]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void Late() {

        studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
        studentNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                    StudentList.add(studentHelper);
                }

                studentNames = new String[StudentList.size()];
                for (int k = 0; k < studentNames.length; k++) {
                    studentNames[k] = StudentList.get(k).getStudentNo();

                    databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNames[k]).child(String.valueOf(code)).child(Name).child("pdf");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDFStudent putPDFStudent = ds.getValue(com.example.educado_final.putPDFStudent.class);
                                EntryList.add(putPDFStudent);
                            }

                            String[] uploadsName = new String[EntryList.size()];
                            positionValues = new int[EntryList.size()];
                            int pos = 0;
                            for (int i = 0; i < uploadsName.length; i++) {
                                if (EntryList.get(i).getStatus().equals("Late")){
                                    nameAddresses.put(studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());
                                    positionValues[pos] = i;
                                    pos++;
                                }
                                else {
                                    uploadsName[i] = EntryList.get(i).getName();
                                }
                            }
                            uploadsName = null;
                            SimpleAdapter adapter = new SimpleAdapter(InsideModule.this, listItems, R.layout.list_passed_modules,
                                    new String[]{"First Line", "Second Line"},
                                    new int[]{R.id.nameOfModule, R.id.statusOfModule});

                            if(listItems.size()>0)
                                listItems.clear();
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
//                title.setText(studentNames[1]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void Missing() {

        studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("StudentsJoined");
        studentNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    studentHelper studentHelper = ds.getValue(com.example.educado_final.studentHelper.class);
                    StudentList.add(studentHelper);
                }

                studentNames = new String[StudentList.size()];
                for (int k = 0; k < studentNames.length; k++) {
                    studentNames[k] = StudentList.get(k).getStudentNo();

                    databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNames[k]).child(String.valueOf(code)).child(Name).child("pdf");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDFStudent putPDFStudent = ds.getValue(com.example.educado_final.putPDFStudent.class);
                                EntryList.add(putPDFStudent);
                            }

                            String[] uploadsName = new String[EntryList.size()];
                            positionValues = new int[EntryList.size()];
                            int pos = 0;
                            for (int i = 0; i < uploadsName.length; i++) {
                                if (EntryList.get(i).getStatus().equals(Name + " Missing")){
                                    nameAddresses.put(studentNames[i] +"\n"+ EntryList.get(i).getName(),EntryList.get(i).getStatus());
                                    positionValues[pos] = i;
                                    pos++;
                                }
                                else {
                                    uploadsName[i] = EntryList.get(i).getName();
                                }
                            }
                            uploadsName = null;
                            SimpleAdapter adapter = new SimpleAdapter(InsideModule.this, listItems, R.layout.list_passed_modules,
                                    new String[]{"First Line", "Second Line"},
                                    new int[]{R.id.nameOfModule, R.id.statusOfModule});

                            if(listItems.size()>0)
                                listItems.clear();
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
//                title.setText(studentNames[1]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}