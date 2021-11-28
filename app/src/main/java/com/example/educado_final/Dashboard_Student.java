package com.example.educado_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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

public class Dashboard_Student extends AppCompatActivity {

    private long backButtonCount;

    TextView username;
    String user,studentNo;


    StorageReference storageReference;
    DatabaseReference databaseReference;
    ListView listView;
    List<classHelper> EntryList;



    HashMap<Integer, String> nameAddresses = new HashMap<>();
    List<HashMap<String, String>> listItems = new ArrayList<>();

    DatabaseReference studentNameReference;

    List<classHelper> SubCodeList;

    List<putPDFTeacher> NotifyList;

    Integer[] subcode;
    String[] subyear;
    String[] subsec;

    Date d = new Date();
    CharSequence s  = DateFormat.format("dd/M/yyyy", d.getTime());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dashboard__student);

        username = findViewById(R.id.textView21);
        user = getIntent().getStringExtra("User");
        username.setText(user);
        studentNo = getIntent().getStringExtra("studentNumber");

        listView =  findViewById(R.id.studentClasses);
        EntryList = new ArrayList<classHelper>();

        SubCodeList = new ArrayList<>();
        NotifyList = new ArrayList<>();

        

        RetrieveList();
        GetNotified();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My notification","My notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

//        notify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int intention, long id) {

                classHelper classHelper = EntryList.get(intention);
                Integer Code = classHelper.getSubjectCode();
                String Section = classHelper.getSubjectSection();
                String Title = classHelper.getSubjectTitle();
                String Year = classHelper.getSubjectYear();
                String Time = classHelper.getTime();

                Intent intent = new Intent(Dashboard_Student.this,ClassesStudent.class);

                intent.putExtra("User",studentNo);
                intent.putExtra("studentNo",user);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNo);
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
                    uploadsName[i]= String.valueOf(EntryList.get(i).getSubjectCode());

                }


                SimpleAdapter adapter = new SimpleAdapter(Dashboard_Student.this, listItems, R.layout.list_student_classes,
                        new String[]{"First Line", "Second Line"},
                        new int[]{R.id.top, R.id.bot});

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
        if(backButtonCount >= 1)
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

    private void GetNotified() {

        studentNameReference = FirebaseDatabase.getInstance().getReference("JoinClasses").child(studentNo);
        studentNameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    classHelper classHelper = ds.getValue(com.example.educado_final.classHelper.class);
                    SubCodeList.add(classHelper);
                }

                subcode = new Integer[SubCodeList.size()];
                subyear = new String[SubCodeList.size()];
                subsec = new String[SubCodeList.size()];
                for (int k = 0; k < subcode.length; k++) {
                    subcode[k] = SubCodeList.get(k).getSubjectCode();
                    subyear[k] = SubCodeList.get(k).getSubjectYear();
                    subsec[k] = SubCodeList.get(k).getSubjectSection();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Classrooms").child(subyear[k]).child(subsec[k]).child(subcode[k].toString()).child("pdf");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot ds : snapshot.getChildren()) {
                                putPDFTeacher putPDFTeacher = ds.getValue(com.example.educado_final.putPDFTeacher.class);
                                NotifyList.add(putPDFTeacher);
                            }

                            String[] uploadsSubtitle = new String[NotifyList.size()];
                            String[] uploadsName = new String[NotifyList.size()];
                            String[] uploadsDate = new String[NotifyList.size()];

                            for (int i = 0; i < uploadsName.length; i++) {
                              //  nameAddresses.put(EntryList.get(i).getName(), EntryList.get(i).getStatus());
                                uploadsSubtitle[i] =NotifyList.get(i).getSubTitle();
                                uploadsName[i] = NotifyList.get(i).getName();
                                uploadsDate[i] =NotifyList.get(i).getDate();

                                try{

                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy");

                                    String str1 = s.toString();
                                    Date date1 = formatter.parse(str1);

                                    String str2 = uploadsDate[i];
                                    Date date2 = formatter.parse(str2);

                                     if (date1.compareTo(date2)==0)
                                    {//date2 > than date1
//                        System.out.println("date2 is Greater than my date1");
                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard_Student.this,"My notification");
                                        builder.setContentTitle(uploadsSubtitle[i]);
                                        builder.setContentText(uploadsName[i]);
                                        builder.setSmallIcon(R.drawable.educadologo);
                                        builder.setAutoCancel(true);
                                        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(uploadsName[i]+" is due today. Please finish your module before it ends"));

                                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Dashboard_Student.this);
                                        managerCompat.notify(i, builder.build());
                                    }
                                    else {

                                    }

                                }catch (ParseException | java.text.ParseException e1){
                                    e1.printStackTrace();
                                }


                            }


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