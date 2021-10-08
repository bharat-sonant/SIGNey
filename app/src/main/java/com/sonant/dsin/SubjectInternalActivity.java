package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SubjectInternalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<SubjectAdapter> expandableListTitle1;

    List<String> chapter = new ArrayList<>();
    List<String> topic = new ArrayList<>();
    List<String> anthem = new ArrayList<>();

    ArrayList<String> chapterPos = new ArrayList<>();


    List<SubjectAdapter> chapter1 = new ArrayList<>();
    List<SubjectAdapter> topic1 = new ArrayList<>();
    DatabaseReference rootRef;
    Context context;
    EditText searchquery;
    String positionofchaptername;

    //TODO comment local database details by mohit

    //  LocalDatabaseChapterData databaseChapterData;
    // LocalDatabaseTopicData databaseTopicData;
    ProgressBar progressBar;
    HashMap<String, List<String>> expandableListDetail;
    HashMap<String, List<SubjectAdapter>> expandableListDetail1;


    TextView title;
    String name = "";
    private int pos = -1;
    private int pos2 = 0;
    private int pos3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subject_internal);
        context = getApplicationContext();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        rootRef = FirebaseDatabase.getInstance().getReference();

        //TODO comment local database details by mohit
        //   databaseChapterData = new LocalDatabaseChapterData(context);
        //  databaseTopicData = new LocalDatabaseTopicData(context);
        //////////////////////////

        name = getIntent().getStringExtra("name");
        Log.d("TAG", "onCreate: 234234" + name);
        Log.d("DATA", name);

        title = findViewById(R.id.title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        topic1 = new ArrayList<>();
        chapter1 = new ArrayList<>();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progressBar = findViewById(R.id.pb);

        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View view = navigationView.getHeaderView(0);
        navigationView.removeHeaderView(view);
        SharedPreferences sharedpreferences = getSharedPreferences("details", Context.MODE_PRIVATE);
        ImageView imgvw = (ImageView) hView.findViewById(R.id.side_imageView);
        TextView tv = (TextView) hView.findViewById(R.id.side_name);
        TextView tv1 = (TextView) hView.findViewById(R.id.side_email);

        String profileurl = sharedpreferences.getString("profileurl", "");
        tv.setText(sharedpreferences.getString("name", ""));
        tv1.setText(sharedpreferences.getString("email", ""));
        Glide.with(this)
                .load(profileurl)
                .into(imgvw);


        expandableListDetail = ExpandableListDataPump.getData();
        expandableListDetail1 = ExpandableListDataPump.getData1();
        addData(name);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                if (pos != -1 && groupPosition != pos) {
                    expandableListView.collapseGroup(pos);

                }
                pos = groupPosition;

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return true;
            }
        });
    }

    private void addData(String name) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        //TODO comment local database details by mohit

        // pos2 = databaseChapterData.getChapterCount();
        //pos3 = databaseTopicData.getTopicCount();
        //////////////////////////
        Log.d("SubjectInternal", "Positions" + pos2 + "/" + pos3);
        int a = fetchdata(name);
        if (a == 1) {
            Log.d("SubjectInternal", "Added from local");
        } else {
            // Read from the database
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                getsubdata();
//                Log.d("Subject internal", "I am here");
//                DatabaseReference hotelRef = rootRef.child("Material/10/CBSE/" + this.name + "/BookData" + "/");
//                ValueEventListener eventListener = new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                            String data = ds.getKey();
//                            SubjectAdapter subjectAdapter = new SubjectAdapter();
//                            subjectAdapter.setKeyname(SubjectInternalActivity.this.name);
//                            subjectAdapter.setId("" + pos2);
//
//
//                            subjectAdapter.setHeading(data);
//
//                            chapter.add(ds.getKey());
//
//                            //TODO comment local database details by mohit
//
//                            //    databaseChapterData.addChapter(subjectAdapter);
//                            ////////////////////////////////
//                            pos2++;
//                            Log.w("Subject data", "Added from " + dataSnapshot.getKey() + ds.getKey());
//
//                        }
//
//                        getsubdata(0);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//                };
//                hotelRef.addListenerForSingleValueEvent(eventListener);
            } else {
                Intent intent = new Intent(SubjectInternalActivity.this, NoInternet.class);
                startActivity(intent);
            }
        }
    }

//    private void getsubdata(int currentPos) {
//
//        final int[] pos = {currentPos};
//        if (pos[0] >= chapter.size()) {
//            expandableListTitle = new ArrayList<String>(chapter);
//
//            expandableListAdapter = new CustomExpandableListAdapter1(SubjectInternalActivity.this, expandableListTitle, expandableListDetail1);
//            expandableListView.setAdapter(expandableListAdapter);
//            progressBar.setVisibility(View.GONE);
//            progressBar.setIndeterminate(false);
//            return;
//        }
//        final String chap = chapter.get(pos[0]);
//        topic = new ArrayList<>();
//        topic1 = new ArrayList<>();
//
//
//        DatabaseReference hotelRef = rootRef.child("Material/10/CBSE/" + name + "/BookData/" + chap + "/");
//        ValueEventListener eventListener = new ValueEventListener() {
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String pagenosdsd = dataSnapshot.child("position").getValue(String.class);
//                Log.d("TAG", "onDataChange: sfbdfhgdgafgv  "+pagenosdsd);
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    SubjectAdapter subjectAdapter = new SubjectAdapter();
//                    String top = ds.getKey();
//                    String pageno = ds.child("pgno").getValue(String.class);
//                    String url = ds.child("Bookdata").getValue(String.class);
//
//                    subjectAdapter.setPosition(pagenosdsd);
//                    subjectAdapter.setSubtopic(top);
//                    subjectAdapter.setId("" + pos3);
//                    subjectAdapter.setPgno(pageno);
//                    subjectAdapter.setUrl(url);
//                    subjectAdapter.setKeyname(name + chap);
//
//
//
//                    //TODO comment local database details by mohit
//                    // databaseTopicData.addTopic(subjectAdapter);
//
//                    /////////////////////////////////////
//
//                    topic.add(top);
//                    topic1.add(subjectAdapter);
//                    pos3++;
//
//                    Log.w("Subjectdata1", "Added from " + topic1.size());
//                }
//                // todo comment
//
//                try {
//                    Collections.sort(topic1, new Comparator<SubjectAdapter>() {
//                        @Override
//                        public int compare(SubjectAdapter o1, SubjectAdapter o2) {
//                            return Integer.parseInt(o1.getPgno()) - (Integer.parseInt(o2.getPgno()));
//                        }
//                    });
//
//                } catch (Exception e) {
//                }
//
//
//                expandableListDetail1.put(chap, topic1);
//                pos[0]++;
//                getsubdata(pos[0]);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//        };
//        hotelRef.addListenerForSingleValueEvent(eventListener);
//
//        Log.d("topicsize", "" + topic.size());
//    }

    private void getsubdata() {
        Log.d("TAG", "getsubdata: cheack 1");

//        final int[] pos = {currentPos};
//        if (pos[0] >= chapter.size()) {
//            expandableListTitle = new ArrayList<String>(chapter);
//
//            expandableListAdapter = new CustomExpandableListAdapter1(SubjectInternalActivity.this, expandableListTitle, expandableListDetail1);
//            expandableListView.setAdapter(expandableListAdapter);
//            progressBar.setVisibility(View.GONE);
//            progressBar.setIndeterminate(false);
//            return;
//        }
//        final String chap = chapter.get(pos[0]);
//        topic = new ArrayList<>();
//        topic1 = new ArrayList<>();

        rootRef.child("Material/10/CBSE/" + name + "/BookData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot snapshot5 : snapshot.getChildren()) {

                        chapterPos.add(snapshot5.getKey());


                        Log.d("TAG", "onDataChange:chapterPos " + chapterPos);

                    }
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.hasChild("position")) {

                            chapterPos.set(Integer.parseInt(snapshot1.child("position").getValue().toString()) - 1, snapshot1.getKey());

                        }
                    }
                    Log.d("TAG", "getsubdata: cheack 2");
                    for (int i = 0; i < chapterPos.size(); i++) {
                        topic = new ArrayList<>();
                        topic1 = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.child(chapterPos.get(i)).getChildren()) {
                            if (!snapshot1.hasChild("position")) {
                                SubjectAdapter subjectAdapter = new SubjectAdapter();
                                String top = snapshot1.getKey();
                                String chapterpdfurl = null;
                                chapterpdfurl = snapshot1.child("chapterpdfurl").getValue(String.class);
                                Log.d("TAG", "onDataChange:chapterpdfurl " + chapterpdfurl);

                                String chaptername = null;
                                chaptername = snapshot1.child("chaptername").getValue(String.class);
                                Log.d("TAG", "onDataChange:chaptername " + chaptername);
                                if (top.equals("position")) {

                                } else {
                                    Log.d("TAG", "onDataChangesdfsdvsdv: ");
                                    String pageno = snapshot1.child("pgno").getValue(String.class);
                                    String url = snapshot1.child("Bookdata").getValue(String.class);
                                    subjectAdapter.setPosition("pagenosdsd");
                                    subjectAdapter.setSubtopic(top);
                                    subjectAdapter.setId("" + pos3);
                                    subjectAdapter.setPgno(pageno);
                                    subjectAdapter.setChapterpdfurl(chapterpdfurl);
                                    subjectAdapter.setChaptername(chaptername);
                                    subjectAdapter.setUrl(url);
                                    subjectAdapter.setKeyname(name + chapterPos.get(i));
                                    topic.add(top);
                                    topic1.add(subjectAdapter);
                                }


                            }
                        }
                        try {
                            Collections.sort(topic1, (o1, o2) -> Integer.parseInt(o1.getPgno()) - (Integer.parseInt(o2.getPgno())));
                        } catch (Exception e) {
                        }
                        Log.d("TAG", "getsubdata:topicwefwer " + topic);
                        Log.d("TAG", "getsubdata:topic1 " + topic1);

                        expandableListDetail1.put(chapterPos.get(i), topic1);

                    }


                    expandableListTitle = new ArrayList<String>(chapterPos);

                    Log.d("TAG", "getsubdata:expandableListTitle " + expandableListTitle);

                    Log.d("TAG", "getsubdata:expandableListDetail1 " + expandableListDetail1);


                    expandableListAdapter = new
                            CustomExpandableListAdapter1(SubjectInternalActivity.this, expandableListTitle, expandableListDetail1);


                    expandableListView.setAdapter(expandableListAdapter);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setIndeterminate(false);
                    return;


//                    String pagenosdsd = snapshot.child("position").getValue(String.class);
//                    Log.d("TAG", "onDataChange: sfbdfhgdgafgv  "+pagenosdsd);
//
//                    for (DataSnapshot ds : snapshot.getChildren()) {
//
//                        SubjectAdapter subjectAdapter = new SubjectAdapter();
//                        String top = ds.getKey();
//                        String pageno = ds.child("pgno").getValue(String.class);
//                        String url = ds.child("Bookdata").getValue(String.class);
//
//                        subjectAdapter.setPosition(pagenosdsd);
//                        subjectAdapter.setSubtopic(top);
//                        subjectAdapter.setId("" + pos3);
//                        subjectAdapter.setPgno(pageno);
//                        subjectAdapter.setUrl(url);
//                        subjectAdapter.setKeyname(name + chap);
//
//
//
//                        //TODO comment local database details by mohit
//                        // databaseTopicData.addTopic(subjectAdapter);
//
//                        /////////////////////////////////////
//
//                        topic.add(top);
//                        topic1.add(subjectAdapter);
//                        pos3++;
//
//                        Log.w("Subjectdata1", "Added from " + topic1.size());
//                    }
//                    // todo comment
//
//                    try {
//                        Collections.sort(topic1, new Comparator<SubjectAdapter>() {
//                            @Override
//                            public int compare(SubjectAdapter o1, SubjectAdapter o2) {
//                                return Integer.parseInt(o1.getPgno()) - (Integer.parseInt(o2.getPgno()));
//                            }
//                        });
//
//                    } catch (Exception e) {
//                    }
//
//
//                    expandableListDetail1.put(chap, topic1);
//                    pos[0]++;
//                    getsubdata(pos[0]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Log.d("check_click", "onNavigationItemSelected: id " + id);

        if (id == R.id.nav_profile) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, DashboardActivity.class));
        } else if (id == R.id.nav_logout) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            logout();
        } else if (id == R.id.nav_setting) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(getApplicationContext(), "Settings Coming Soon", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_library) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, SubjectActivity.class));
        }
        return true;
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        removeUserDeatils();

    }

    private void removeUserDeatils() {
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        googleSignInClient.signOut();
    }

    private int fetchdata(String name) {
        //TODO comment local database details by mohit

        // chapter1 = databaseChapterData.getSpecific(name);
        if (chapter1.size() == 0) {
            return 0;
        } else {
            int i, j;
//        topic1 = databaseTopicData.getAll();
            for (i = 0; i < chapter1.size(); i++) {
                topic = new ArrayList<>();

                topic1 = new ArrayList<>();
                chapter.add(chapter1.get(i).getHeading());
                Log.d("chapter1", chapter1.get(i).getKeyname() + "/" + chapter1.get(i).getHeading());
                String data = chapter1.get(i).getKeyname() + chapter1.get(i).getHeading();
                String chap = chapter1.get(i).getHeading();
                //TODO comment local database details by mohit
                //   topic1 = databaseTopicData.getSpecific(data);
                for (j = 0; j < topic1.size(); j++) {

                    Log.d("topic", topic1.get(j).getUrl() + "/" + topic1.get(j).getHeading());
                    topic.add(topic1.get(j).getHeading());
                }
                Log.d("topi1", "" + topic1.size());

                Log.d("TAG", "fetchdata:topic " + topic);

                //expandableListDetail.put(chap, topic);
                // expandableListDetail1.put(chap, topic1);
            }
            expandableListTitle = new ArrayList<String>(chapter);
            expandableListAdapter = new CustomExpandableListAdapter1(SubjectInternalActivity.this, expandableListTitle, expandableListDetail1);
            expandableListView.setAdapter(expandableListAdapter);
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);

        }
        return 1;
    }
}
