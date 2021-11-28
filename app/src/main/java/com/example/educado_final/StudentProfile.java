package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class StudentProfile extends AppCompatActivity {
    String Pangalan, studentNo,year,sec;
    TextView Name, Number;


    Integer code;

    FirebaseDatabase UsersDatabase;

    String[] ModuleNames;

    ListView listView;
    List<putPDFStudent> EntryList;
    List<putPDFTeacher> ModuleNameList;

    HashMap<String, String> nameAddresses = new HashMap<>();
    List<HashMap<String, String>> listItems = new ArrayList<>();

    DatabaseReference databaseReference,studentNameReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_student_profile);

        Pangalan = getIntent().getStringExtra("Name");
        studentNo = getIntent().getStringExtra("StudentNo");
        year = getIntent().getStringExtra("Year");
        sec = getIntent().getStringExtra("Section");
        code = getIntent().getIntExtra("Code",-1);


        EntryList = new ArrayList<>();
        ModuleNameList = new ArrayList<>();

        Name = findViewById(R.id.txtStudentName);
        Number = findViewById(R.id.textView54);
        listView= findViewById(R.id.ModPass);


        Name.setText(Pangalan);
        Number.setText(studentNo);

        RetrieveList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {

                putPDFStudent putPDFStudent = EntryList.get(intention);
                if (putPDFStudent.getUrl().equals("N/A")){
                    Toast.makeText(StudentProfile.this,"Student hasn't uploaded Module", Toast.LENGTH_LONG).show();
                }

                else {
                    String Url = putPDFStudent.getUrl();

                    Intent intent = new Intent(StudentProfile.this, viewPdfUsingApp.class);
                    intent.putExtra("Url", Url);

//                Intent intent = new Intent();
//                intent.setType(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(putPDFStudent.getUrl()));
                    startActivity(intent);
                }
            }
        });



    }
    private void RetrieveList() {

        studentNameReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");
        studentNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                    ModuleNameList.add(putPDFTeacher);
                }

                ModuleNames = new String[ModuleNameList.size()];
                for (int k = 0; k < ModuleNames.length; k++) {
                    ModuleNames[k] = ModuleNameList.get(k).getName();

                    databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNo).child(String.valueOf(code)).child(ModuleNames[k]).child("pdf");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDFStudent putPDFStudent = ds.getValue(com.example.educado_final.putPDFStudent.class);
                                EntryList.add(putPDFStudent);
                            }

                            String[] uploadsName = new String[EntryList.size()];

                            for (int i = 0; i < uploadsName.length; i++) {
                                nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getStatus());

                            }

                            SimpleAdapter adapter = new SimpleAdapter(StudentProfile.this, listItems, R.layout.list_all_passed_modules_of_student,
                                    new String[]{"First Line", "Second Line"},
                                    new int[]{R.id.ModuleName, R.id.ModuleStatus});

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