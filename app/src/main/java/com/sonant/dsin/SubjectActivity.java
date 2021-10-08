package com.sonant.dsin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;


public class SubjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<String> items = new ArrayList<String>();
    RecyclerView recyclerView;
    Spinner spinner;
    ProgressBar progressBar;
    TextView title;
    AlertDialog.Builder builder;
    ImageView spinnerArrow;
    LocalDatabaseSubject localDatabaseSubject;
    private List<ItemAdapter> mList = new ArrayList<>();
    private ListAdapter1 mAdapter;
    int count = 0, val = 0;
    SharedPreferences sharedPreferencesss;
    CommonProgressBar commonProgressBar = new CommonProgressBar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_display);

        title = findViewById(R.id.title);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.VISIBLE);
        commonProgressBar.setProgressDialog(SubjectActivity.this, SubjectActivity.this);

        items.clear();
        items.add("Select subject");
//        items.add("English");
//        items.add("Social Science");
//        items.add("Science");
//        items.add("Maths");
        // items.add("Physics");
        DatabaseReference subref = FirebaseDatabase.getInstance().getReference();
        subref.child("Material/10/CBSE").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
                for (DataSnapshot snapp : dataSnapshot.getChildren()) {
                    items.add(snapp.getKey());
                }
                commonProgressBar.closeDialog(SubjectActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // todo mohit tesing to check that firebase is working or not
        //     FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Material/10/CBSE/English/Bookname");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String heading = String.valueOf(dataSnapshot.getValue());
//                //  String bookname = dataSnapshot.child("Bookname").getValue(String.class);
//                Log.d("TAG", "Value is:fdgdfsdffd " + heading);
//            }@Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("TAG", "Value is:error ");
//            }});


        spinner = findViewById(R.id.spinner);
        spinner.setClickable(false);
        spinnerArrow = findViewById(R.id.spinnerarrow);
        localDatabaseSubject = new LocalDatabaseSubject(this);

        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner, items);
            spinner.setAdapter(adapter);
            spinner.setClickable(true);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    //  progressBar.setVisibility(View.VISIBLE);
                    //    progressBar.setIndeterminate(true);
                    mList.clear();
                    //     progressBar.setVisibility(View.VISIBLE);
                    //     progressBar.setIndeterminate(true);
                    String selectedsub = parentView.getItemAtPosition(position).toString();
                    sharedPreferencesss = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferencesss.edit();
                    myEdit.putString("name", selectedsub);
                    myEdit.commit();

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
                    Log.d("TAG", "log434" + selectedsub);
                    //      Toast.makeText(SubjectActivity.this, selectedsub, Toast.LENGTH_SHORT).show();


                    addData(spinner.getSelectedItem().toString());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }
            });
        } catch (Exception e) {

        }


        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        toolbar1.setContentInsetStartWithNavigation(5);
        setSupportActionBar(toolbar1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setBackgroundColor(getResources().getColor(R.color.white));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

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

    }

    private void adapter() {
        mAdapter = new ListAdapter1(mList, this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //  progressBar.setVisibility(View.GONE);
        //   progressBar.setIndeterminate(false);
    }

    // todo mohit --- main method where data is getting to the activity

    private void addData(final String selected) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnectedd = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnectedd) {

            progressBar.setVisibility(View.GONE);


            if (spinner.getSelectedItem().toString().equals("Select subject")) {
                //     progressBar.setVisibility(View.GONE);

            } else {

                Log.d("subjectcount", "gxvvsdvdvunt" + selected);
                // Read from the database
                count = localDatabaseSubject.getSubjectCount();
                Log.d("subjectcount", "get subject count" + count);

                // todo Mohit comment the below line because i do no want to add local sqlite database
                // val = fetchdata(selected);

                if (val == 1) {
                    Log.d("DataRecieved", "added from local");

                    //  progressBar.setVisibility(View.GONE);
                    //   progressBar.setIndeterminate(false);
                    return;

                } else {
                    ConnectivityManager cmm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetworkk = cmm.getActiveNetworkInfo();
                    boolean isConnected = activeNetworkk != null &&
                            activeNetworkk.isConnectedOrConnecting();
                    if (isConnected) {


                        Log.d("DataRecieved", "added from firebase");

                        Log.d("Subject Activity", "I am here");
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference hotelRef = rootRef.child("Material/10/CBSE");

                        // TODO: 5/9/20 change the logic here
                        // TODO: 5/9/20 add the database count
                        if (spinner.getSelectedItem().toString().equals("Social Science")) {

                            DatabaseReference hotelRef = rootRef.child("Material/10/CBSE/Social Science");
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // progress dialog dismiss

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                        ItemAdapter itemAdapter = new ItemAdapter();
                                        String heading = ds.getKey();
                                        //  Log.d("TAG", "onDataChange:test6 "+heading);
                                        String bookname = ds.child("Bookname").getValue(String.class);
                                        Log.d("TAG", "onDataChange:test6 " + bookname);

                                        String bookurl = ds.child("Bookimgurl").getValue(String.class);

                                        itemAdapter.setHeading(heading);
                                        itemAdapter.setName(bookname);
                                        itemAdapter.setKey(selected);
                                        itemAdapter.setBookurl(bookurl);
                                        itemAdapter.setId("" + count);
                                        Log.w("Subject Activity", "Added from " + itemAdapter.getKey());
                                        mList.add(itemAdapter);
                                        count++;
                                        localDatabaseSubject.addBook(itemAdapter);

                                    }
                                    adapter();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("Subject Activity", "Failed to read value.", databaseError.toException());
                                }
                            };
                            hotelRef.addListenerForSingleValueEvent(eventListener);

                        } else {
                            DatabaseReference hotelRef = rootRef.child("Material/10/CBSE/" + spinner.getSelectedItem().toString() + "/");
                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ItemAdapter itemAdapter = new ItemAdapter();
                                    String heading = dataSnapshot.getKey();
                                    String bookname = dataSnapshot.child("Bookname").getValue(String.class);
                                    String bookurl = dataSnapshot.child("Bookimgurl").getValue(String.class);

                                    Log.d("TAG", "Value is:ghdhygfjhg " + heading);

                                    itemAdapter.setHeading(heading);
                                    itemAdapter.setName(bookname);
                                    itemAdapter.setBookurl(bookurl);
                                    itemAdapter.setKey(selected);

                                    itemAdapter.setId("" + count);
                                    Log.w("Subject Activity", "Added from " + itemAdapter.getKey());
                                    mList.add(itemAdapter);
                                    count++;
                                    localDatabaseSubject.addBook(itemAdapter);
                                    adapter();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("Subject Activity", "Failed to read value.", databaseError.toException());
                                }

                            };
                            hotelRef.addListenerForSingleValueEvent(eventListener);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(SubjectActivity.this, NoInternet.class);
                        startActivity(intent);
                    }
                }

            }
        } else {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(SubjectActivity.this, NoInternet.class);
            startActivity(intent);
        }
    }

    //todo fetch data
    private int fetchdata(String s) {
        mList = localDatabaseSubject.getSpecific(s);

        if (mList.size() <= 0) {

            Log.d("abcdabcdmohit", "a1mohit" + mList.size());
            return 0;
        } else {
            Log.d("abcdabcd", "name=mohit " + mList.get(0).getName() + "/id = " + mList.get(0).getKey() + "/url = " + mList.get(0).getBookurl() + "/heading = " + mList.get(0).getHeading());

            mAdapter = new ListAdapter1(mList, this);

            recyclerView.setAdapter(mAdapter);
            return 1;
        }
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
        }
        return true;
    }

    private void logout() {

        builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure? You want to logout")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        sharedPreferences = getSharedPreferences("details", Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(SubjectActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        removerUserDeatils();
                        startActivity(intent);
                    }

                    private void removerUserDeatils() {
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                        googleSignInClient.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Logout");
        alert.show();


    }

}
