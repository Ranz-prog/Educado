package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class Dashboard_Teacher extends AppCompatActivity {

    private long backButtonCount;

    TextView username,create;
    String user;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    ListView listView;
    List<classHelper> EntryList;
    HashMap<Integer, String> nameAddresses = new HashMap<Integer, String>();
    List<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();

    //String user,sec,year;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard__teacher);

        username = findViewById(R.id.textView10);
        user = getIntent().getStringExtra("User");
        username.setText(user);

        listView =  findViewById(R.id.sample);
        EntryList = new ArrayList<classHelper>();


        create = findViewById(R.id.txtcreateclass);

        RetrieveList();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard_Teacher.this, CreateClass.class);
                intent.putExtra("User", user);
                finish();
                overridePendingTransition( 0, 0);
                startActivity(getIntent());
                overridePendingTransition( 0, 0);
                startActivity(intent);

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {
                classHelper classHelper = EntryList.get(intention);
                Integer Code = classHelper.getSubjectCode();
                String Section = classHelper.getSubjectSection();
                String Title = classHelper.getSubjectTitle();
                String Year = classHelper.getSubjectYear();
                String Time = classHelper.getTime();

                Intent intent = new Intent(Dashboard_Teacher.this,Classes.class);

                intent.putExtra("Code", Code);
                intent.putExtra("Section", Section);
                intent.putExtra("Title", Title);
                intent.putExtra("Year", Year);
                intent.putExtra("Time", Time);

                startActivity(intent);
            }
        });


    }

    private void RetrieveList() {
        String searchUser = user;

        databaseReference = FirebaseDatabase.getInstance().getReference("CreatedRooms").child(user);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren()){

                   classHelper classHelper = ds.getValue(com.example.educado_final.classHelper.class);
                    EntryList.add(classHelper);
                }

                String[] uploadsName= new String[EntryList.size()];

                for (int i=0;i<uploadsName.length; i++){

                    nameAddresses.put(EntryList.get(i).getSubjectCode(), EntryList.get(i).getSubjectTitle());
                    uploadsName[i]= String.valueOf(EntryList.get(i).getSubjectTitle());
                }

//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, uploadsName){
//
//                    @NonNull
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        View view = super.getView(position, convertView, parent);
//                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
//
//                        textView.setTextColor(Color.BLACK);
//
//                        return view;
//                    }
//
//                };
//
//                listView.setAdapter(arrayAdapter);


                SimpleAdapter adapter = new SimpleAdapter(Dashboard_Teacher.this, listItems, R.layout.list_item,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.text1, R.id.text2});

                TreeMap<Integer, String> map = new TreeMap<Integer, String>(nameAddresses);
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
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 2)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

}