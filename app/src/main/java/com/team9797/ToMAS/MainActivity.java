package com.team9797.ToMAS;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team9797.ToMAS.navigation.first_level_adapter;
import com.team9797.ToMAS.ui.home.market.marketFragment;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    ExpandableListView expandableListView;
    List<String> headerList = new ArrayList<>();
    String title;
    DrawerLayout drawer;

    //firebase
    public FirebaseFirestore db;

    // 사용자 정보 저장.
    public SharedPreferences preferences;
    public SharedPreferences.Editor sp_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //firebase
        db = FirebaseFirestore.getInstance();

        //shared preference init
        preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        sp_editor = preferences.edit();

        db.collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        sp_editor.putString("password",document.get("password").toString());
                        sp_editor.putString("이름", document.get("이름").toString());
                        sp_editor.putString("권한", document.get("권한").toString());
                        sp_editor.putString("계급", document.get("계급").toString());
                        sp_editor.putString("소속", document.get("소속").toString());
                        sp_editor.putString("군번", document.get("군번").toString());
                        sp_editor.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        sp_editor.putString("email", document.get("email").toString());
                        sp_editor.putString("birth", document.get("birth").toString());

                        sp_editor.putString("phonenumber", document.get("phonenumber").toString());
                        sp_editor.commit();

                    } else {

                    }
                }
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        // 맨위 tool bar 세팅
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_social, R.id.navigation_mypage)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        // 오른쪽 drawable total_navi 세팅
        drawer = findViewById(R.id.drawer_layout);

        // 임시 firebase에서 받아와서 넣어줘야함.

        String[] ItemHeaders = new String[]{"자기개발", "소통게시판", "플리마켓", "인원모집"};
        Collections.addAll(headerList, ItemHeaders);


        expandableListView = findViewById(R.id.total_right_menu);
        if (expandableListView != null)
        {
            first_level_adapter first_adapter = new first_level_adapter(this, headerList, this);
            expandableListView.setAdapter(first_adapter);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                return false;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView)findViewById(R.id.total_navi_view);
        navigationView.setNavigationItemSelectedListener(this);


        // 자신이 올린 market, 인원모집 기간 확인 후 삭제하고 최고입찰자한테 포인트 수령 보내기
        market_date_check();


   }

   // 서버에서 처리 할 수 있으면 좋음.
   public void market_date_check()
   {
       db.collection("user").document(getUid()).collection("market").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   for (QueryDocumentSnapshot document : task.getResult()) {
                       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                           LocalDate due_date = LocalDate.parse(document.get("due_date").toString(), DateTimeFormatter.ISO_DATE);
                           LocalDate today = LocalDate.now();
                           if (today.isEqual(due_date) || today.isAfter(due_date))
                           {
                               db.collection(document.get("path").toString()).document(document.getId()).collection("participants").orderBy("price", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                   @Override
                                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                       if (task.isSuccessful()) {
                                           for (QueryDocumentSnapshot tmp_document : task.getResult()) {
                                               Map<String, Object> post = new HashMap<>();
                                               post.put("price", tmp_document.get("price", Integer.class));
                                               post.put("path", document.get("path").toString());
                                               post.put("post_id", document.getId());
                                               post.put("title", document.get("title").toString());
                                               db.collection("user").document(tmp_document.getId()).collection("buy_list").document().set(post);
                                           }
                                       }
                                   }
                               });
                           }
                       }
                   }
               } else {
                   //Log.d(TAG, "Error getting documents: ", task.getException());
               }
           }
       });
   }

    public void set_title(){
        toolbar.setTitle(title);
    }

    public void push_title(String title_fragment)
    {
        title = title_fragment;
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void closeDrawer()
    {
        drawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }



}