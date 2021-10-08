package com.sonant.dsin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private boolean exit = false;
    TextView userName, userEmail, userId, sidebar_name, sidebar_email;
    ImageView profileImage, arrow, profileImage1;
    TextView txt;
    Button btn;

    String profileurl;
    AlertDialog.Builder builder;
    LinearLayout points1, points2, points3;
    RelativeLayout relativeLayout_recycler, challenges;
    RecyclerView recyclerView;
    SharedPreferences sharedpreferences;
    int count = 0;
    private List<ItemAdapter> mList = new ArrayList<>();
    private ListAdapterDashboard mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(5);
        setSupportActionBar(toolbar);

        userName = (TextView) findViewById(R.id.username);
        points1 = findViewById(R.id.pointslayout1);
        points2 = findViewById(R.id.pointslayout2);
        challenges = findViewById(R.id.challenges);
        relativeLayout_recycler = findViewById(R.id.lay_recycler);
        points3 = findViewById(R.id.pointslayout3);
        arrow = findViewById(R.id.arrow);
        sidebar_email = findViewById(R.id.side_email);
        sidebar_name = findViewById(R.id.side_name);
        profileImage1 = findViewById(R.id.side_imageView);
        builder = new AlertDialog.Builder(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("key", 1);
        myEdit.commit();


        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("firstlogin", 0);


        if (j > 0) {
            //  relativeLayout_recycler.setVisibility(View.VISIBLE);
            recyclerView = findViewById(R.id.recycler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            addList();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter();
                }
            }, 500);

        } else {
            // relativeLayout_recycler.setVisibility(View.GONE);
            showpoints();
        }

        forceUpdate();
        //   advertisment();
        pushNotification();

        challenges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpoints();
            }
        });


        SharedPreferences sharedPreferencess = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencess.edit();
        editor.putInt("firstlogin", 1);
        editor.apply();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        userEmail = (TextView) findViewById(R.id.email);

        sharedpreferences = getSharedPreferences("details", Context.MODE_PRIVATE);


        //  userName.setText(sharedpreferences.getString("name", ""));

        SharedPreferences sh = getSharedPreferences("userUniqueIdsahredPref", MODE_PRIVATE);
        String userUniqueee = sh.getString("userUniqueId", "");
        FirebaseDatabase.getInstance().getReference().child("users").child(userUniqueee).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String namename = snapshot.child("name").getValue(String.class);
                Log.d("TAG", "onDataChange:namename " + namename);
                userName.setText(namename);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userName.setText("");

        profileurl = sharedpreferences.getString("profileurl", "");

        profileImage = findViewById(R.id.profile_image);

        Glide.with(this)
                .load(profileurl)
                .into(profileImage);


        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View view = navigationView.getHeaderView(0);
        navigationView.removeHeaderView(view);

        ImageView imgvw = (ImageView) hView.findViewById(R.id.side_imageView);
        TextView tv = (TextView) hView.findViewById(R.id.side_name);
        TextView tv1 = (TextView) hView.findViewById(R.id.side_email);
        tv.setText(sharedpreferences.getString("name", ""));
        tv1.setText(sharedpreferences.getString("email", ""));
        Glide.with(this)
                .load(profileurl)
                .into(imgvw);
    }


    private void advertisment() {


        Dialog dialog = new Dialog(DashboardActivity.this, android.R.style.Theme_Holo_Wallpaper_NoTitleBar);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.customview);
        dialog.show();
        txt = dialog.findViewById(R.id.textView4);
        btn = dialog.findViewById(R.id.buttonclick);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void pushNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic("class4th")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "msg_subscribed";
                        if (!task.isSuccessful()) {
                            msg = "msg_subscribe_failed";
                        }

                    }
                });

    }

    @Override
    protected void onResume() {
        forceUpdate();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exit) {
                this.finishAffinity();
            } else {
                Toast.makeText(this, "Press back again to exit",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        exit = false;
                    }
                }.start();
            }

        }
    }

    private void addList() {
        ItemAdapter itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.one);
        itemAdapter.setHeading("History");
        itemAdapter.setName("Topic: Conserving resources");
        itemAdapter.setPgno("Page No: 6");

        mList.add(itemAdapter);
        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.two);
        itemAdapter.setHeading("Social and Political Life");
        itemAdapter.setName("Topic: Conserving resources");
        itemAdapter.setPgno("Page No: 8");


        mList.add(itemAdapter);
        itemAdapter = new ItemAdapter();
        itemAdapter.setImage(R.drawable.three);
        itemAdapter.setHeading("The Earth Our Habitat");
        itemAdapter.setName("Topic: Conserving resources");
        itemAdapter.setPgno("Page No: 10");
        mList.add(itemAdapter);

    }

    private void adapter() {
        mAdapter = new ListAdapterDashboard(mList, this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    public void sselection(View view) {
        Intent intent = new Intent(this, SubjectActivity.class);

        startActivity(intent);
    }

    public void showpoints() {
        if (count % 2 == 0) {
            arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
            //   points2.setVisibility(View.VISIBLE);
            //  points3.setVisibility(View.VISIBLE);
            //   points1.setVisibility(View.GONE);
            count++;

        } else {
            arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
            //  points2.setVisibility(View.GONE);
            //  points3.setVisibility(View.GONE);
            //   points1.setVisibility(View.VISIBLE);
            count++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
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
//            startActivity(new Intent(this, DashboardActivity.class));
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
                        Intent intent = new Intent(DashboardActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        removeUserDetails();
                        startActivity(intent);
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

    private void removeUserDetails() {

        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        googleSignInClient.signOut();
    }

    private void forceUpdate() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnectedd = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnectedd) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("update").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("TAG", "onDataChange: snapshot" + snapshot);
                    String versionMatch = snapshot.child("version").getValue(String.class);
                    String updateStringUrl = snapshot.child("updateStringUrl").getValue(String.class);
                    String dialogstring = snapshot.child("dialogstring").getValue(String.class);

                    //   Uri uriii = Uri(updateStringUrl)
                    Log.d("TAG", "onDataChange: versionMatch " + versionMatch);

                    if (versionMatch.equals("4.0.1")) {

                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(DashboardActivity.this)
                                .setTitle("Alert!")
                                .setCancelable(false)
                                .setMessage(dialogstring)
                                .setPositiveButton("Go TO Play Store",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateStringUrl));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).create();
                        dialog.show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {

            Intent intent = new Intent(DashboardActivity.this, NoInternet.class);
            startActivity(intent);
        }


    }

    public void pointinformation(View view) {
        Intent intent = new Intent(DashboardActivity.this, PointsInformation.class);
        startActivity(intent);
    }

}
