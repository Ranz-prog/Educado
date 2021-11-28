package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;

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

public class Classes extends AppCompatActivity {

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String time;
    String sec;
    String year;
    String title;
    int code;
    int [] positionValues;
    Spinner Sortclassteach;

    TextView subTitle,subTime;

    ListView listView;
    List<putPDFTeacher> EntryList;
    HashMap<String, String> nameAddresses = new HashMap<>();
    List<HashMap<String, String>> listItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_classes);

        listView =  findViewById(R.id.uploadedModules);
        EntryList = new ArrayList<>();

        subTitle = findViewById(R.id.textView9);
        subTime = findViewById(R.id.textView13);

        TextView Addmod = (TextView) findViewById(R.id.txtAddmodule);
        Button AddStu =(Button) findViewById(R.id.btnAddStudent);

        code = getIntent().getIntExtra("Code", -1);
        title = getIntent().getStringExtra("Title");
        year = getIntent().getStringExtra("Year");
        sec = getIntent().getStringExtra("Section");
        time = getIntent().getStringExtra("Time");

        subTitle.setText(title);
        subTime.setText(time);
        Sortclassteach = (Spinner) findViewById(R.id.sortingclassteach);

        RetrieveList();



        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Classes.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortclassteach));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sortclassteach.setAdapter(myAdapter);


        Sortclassteach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        EntryList.clear();
                        listItems.clear();
                        databaseReference = null;
                        RetrieveList();
                        break;
                    case 1:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        EntryList.clear();
                        listItems.clear();
                        first();
                        break;

                    case 2:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        EntryList.clear();
                        listItems.clear();
                        second();
                        break;

                    case 3:
                        listView.setAdapter(null);
                        nameAddresses.clear();
                        EntryList.clear();
                        listItems.clear();
                        third();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Addmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Classes.this, AddModule.class);
                intent.putExtra("Code", code);
                intent.putExtra("Section", sec);
                intent.putExtra("Title", title);
                intent.putExtra("Year", year);
                intent.putExtra("Time", time);
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
                startActivity(intent);

            }
        });

        AddStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Classes.this, AddStudent.class);
                intent.putExtra("Code", code);
                intent.putExtra("Section", sec);
                intent.putExtra("Title", title);
                intent.putExtra("Year", year);
                intent.putExtra("Time", time);
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
                startActivity(intent);


                }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {

                putPDFTeacher putPDFTeacher = EntryList.get(positionValues[intention]);
                String Name = putPDFTeacher.getName();
                String Url = putPDFTeacher.getUrl();
                String Description = putPDFTeacher.getDescription();
                String Date = putPDFTeacher.getDate();



                Intent intent = new Intent(Classes.this,InsideModule.class);

                intent.putExtra("Name", Name);
                intent.putExtra("Url", Url);
                intent.putExtra("Description", Description);
                intent.putExtra("Date", Date);

                intent.putExtra("Code", code);

                intent.putExtra("Section", sec);
                intent.putExtra("Year", year);
                intent.putExtra("Title", title);
                intent.putExtra("Time", time);
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);

                startActivity(intent);
            }
        });


    }

    private void RetrieveList(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                    EntryList.add(putPDFTeacher);
                }

                String[] uploadsName= new String[EntryList.size()];

                positionValues = new int[EntryList.size()];
                int pos = 0;

                for (int i=0;i<uploadsName.length; i++){
                    nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getDate());
                    uploadsName[i]=EntryList.get(i).getName();
                    positionValues[pos] = i;
                    pos++;
                }

                SimpleAdapter adapter = new SimpleAdapter(Classes.this, listItems, R.layout.list_module,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.itaas, R.id.ikababa});

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

    private void first(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                    EntryList.add(putPDFTeacher);
                }

                String[] uploadsName= new String[EntryList.size()];

                positionValues = new int[EntryList.size()];
                int pos = 0;

                for (int i=0;i<uploadsName.length; i++){

                    if(EntryList.get(i).getPeriodic().equals("First Period")) {
                        nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getDate());
                        uploadsName[i] = EntryList.get(i).getName();
                        positionValues[pos] = i;
                        pos++;
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(Classes.this, listItems, R.layout.list_module,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.itaas, R.id.ikababa});

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
    private void second(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                    EntryList.add(putPDFTeacher);
                }

                String[] uploadsName= new String[EntryList.size()];

                positionValues = new int[EntryList.size()];
                int pos = 0;

                for (int i=0;i<uploadsName.length; i++){

                    if(EntryList.get(i).getPeriodic().equals("Second Period")) {
                        nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getDate());
                        uploadsName[i] = EntryList.get(i).getName();
                        positionValues[pos] = i;
                        pos++;
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(Classes.this, listItems, R.layout.list_module,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.itaas, R.id.ikababa});

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
    private void third(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(year).child(sec).child(String.valueOf(code)).child("pdf");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                    putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                    EntryList.add(putPDFTeacher);
                }

                String[] uploadsName= new String[EntryList.size()];

                positionValues = new int[EntryList.size()];
                int pos = 0;

                for (int i=0;i<uploadsName.length; i++){

                    if(EntryList.get(i).getPeriodic().equals("Third Period")) {
                        nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getDate());
                        uploadsName[i] = EntryList.get(i).getName();
                        positionValues[pos] = i;
                        pos++;
                    }
                }

                SimpleAdapter adapter = new SimpleAdapter(Classes.this, listItems, R.layout.list_module,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.itaas, R.id.ikababa});

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
}