package com.sonant.dsin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    TextView name, namee, email, dob, classs, medium, board, bio, et_dob, boardname;
    ImageView img, pen_name, pen_dob, pen_cls, pen_medium, pen_board, pen_bio, pen_boardname;
    EditText et_name, et_bio;
    Spinner et_class, et_medium, et_board;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    SharedPreferences sharedpreferences;
    AutoCompleteTextView state;
    LinearLayout linlay;
    boolean ed_name, ed_dob, ed_class, ed_medium, ed_board, ed_boardname, ed_bio;
    String[] states;
    String userid, date;
    Button save, cancel;
    NavigationView navigationView;
    private Calendar calendar;
    private int year, month, day;
    String profileurl,  profileurllllll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetStartWithNavigation(5);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        sharedpreferences = getSharedPreferences("details", Context.MODE_PRIVATE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);



        View hView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        View view = navigationView.getHeaderView(0);
        navigationView.removeHeaderView(view);

        ImageView imgvw = (ImageView) hView.findViewById(R.id.side_imageView);
        TextView tv = (TextView) hView.findViewById(R.id.side_name);
        TextView tv1 = (TextView) hView.findViewById(R.id.side_email);

        et_bio.setText(bio.getText().toString());
        et_name.setText(name.getText().toString());
        et_dob.setText(dob.getText().toString());


         fillNameOfUser();


        profileurl = sharedpreferences.getString("profileurl", "");
        tv.setText(sharedpreferences.getString("name", ""));
        tv1.setText(sharedpreferences.getString("email", ""));
        Glide.with(this)
                .load(profileurllllll)
                .into(img);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, states);

        state.setThreshold(1);

        state.setAdapter(adapter);

        et_board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (et_board.getItemAtPosition(position).equals("State Board")) {
                    linlay.setVisibility(View.VISIBLE);
                } else {
                    linlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pen_medium.setOnClickListener(this);
        pen_name.setOnClickListener(this);
        pen_dob.setOnClickListener(this);
        pen_cls.setOnClickListener(this);
        pen_board.setOnClickListener(this);
        pen_bio.setOnClickListener(this);
        pen_boardname.setOnClickListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, ProfileActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);
 
        // TODO: 25/9/20  fix issue here // data not loaded proper

        date = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date());

        name.setText(sharedpreferences.getString("name", ""));
        namee.setText(sharedpreferences.getString("name", ""));

        dob.setText(sharedpreferences.getString("date", ""));
        classs.setText(sharedpreferences.getString("class", ""));
        board.setText(sharedpreferences.getString("board", ""));
        medium.setText(sharedpreferences.getString("medium", ""));

        Log.d("TAG0123", sharedpreferences.getString("class", "") + sharedpreferences.getString("board", "") + sharedpreferences.getString("medium", ""));

    }

    private void fillNameOfUser() {
        SharedPreferences sh = getSharedPreferences("userUniqueIdsahredPref", MODE_PRIVATE);
        String userUniqueee = sh.getString("userUniqueId", "");
        FirebaseDatabase.getInstance().getReference().child("users").child(userUniqueee).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String namename = snapshot.child("name").getValue(String.class);
                profileurllllll = snapshot.child("photo").getValue(String.class);
                Log.d("TAG", "onDataChange:namename "+profileurllllll);
                namee.setText(namename);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void init() {
        name = findViewById(R.id.name);
        namee = findViewById(R.id.name1);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.date);
        classs = findViewById(R.id.cls);
        medium = findViewById(R.id.medium);
        linlay = findViewById(R.id.linlaya);
        board = findViewById(R.id.board);
        state = findViewById(R.id.temp6);
        cancel = findViewById(R.id.cancel);
        bio = findViewById(R.id.rectangle_7);
        img = findViewById(R.id.profile_image);
        et_name = findViewById(R.id.temp1);
        boardname = findViewById(R.id.board_name);
        et_dob = findViewById(R.id.temp2);
        et_class = findViewById(R.id.temp3);
        et_medium = findViewById(R.id.temp4);
        et_board = findViewById(R.id.temp5);
        et_bio = findViewById(R.id.et_bio);
        pen_name = findViewById(R.id.penname);
        pen_dob = findViewById(R.id.pendob);
        pen_medium = findViewById(R.id.penmedium);
        states = getResources().getStringArray(R.array.states);
        pen_cls = findViewById(R.id.penclass);
        pen_board = findViewById(R.id.penboard);
        pen_boardname = findViewById(R.id.penboard_name);
        save = findViewById(R.id.save);
        pen_bio = findViewById(R.id.penbio);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
//            name.setText(account.getDisplayName());
//            namee.setText(account.getDisplayName());
            email.setText(account.getEmail());
            userid = account.getId();
            Glide.with(this)
                    .load(account.getPhotoUrl())
                    .into(img);
        } else {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.penname) {
            pen_name.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            ed_name = true;
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            et_name.setVisibility(View.VISIBLE);
            namee.setVisibility(View.GONE);
            et_name.setText(namee.getText().toString());
            et_name.requestFocus();

        } else if (v.getId() == R.id.pendob) {
            ed_dob = true;
            pen_dob.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            Toast.makeText(getApplicationContext(), "Click on the Date to Change", Toast.LENGTH_SHORT).show();
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            et_dob.setVisibility(View.VISIBLE);
            dob.setVisibility(View.GONE);
            et_dob.setText(dob.getText().toString());
            et_dob.requestFocus();

        } else if (v.getId() == R.id.penboard) {
            ed_board = true;
            pen_board.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            et_board.setVisibility(View.VISIBLE);
            board.setVisibility(View.GONE);

//            et_board.setText(board.getText().toString());
            et_board.requestFocus();

        } else if (v.getId() == R.id.penclass) {
            ed_class = true;

            pen_cls.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            et_class.setVisibility(View.VISIBLE);
            classs.setVisibility(View.GONE);
//            et_class.setText(classs.getText().toString());
            et_class.requestFocus();

        } else if (v.getId() == R.id.penmedium) {

            ed_medium = true;
            pen_medium.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            et_medium.setVisibility(View.VISIBLE);
            medium.setVisibility(View.GONE);
//            et_medium.setText(medium.getText().toString());

            et_medium.requestFocus();
        } else if (v.getId() == R.id.penbio) {
            ed_bio = true;
            pen_bio.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

            et_bio.setVisibility(View.VISIBLE);
            bio.setVisibility(View.INVISIBLE);

            et_bio.setText(bio.getText().toString());
            et_bio.requestFocus();

        } else {
            pen_boardname.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

            state.setVisibility(View.VISIBLE);
            boardname.setVisibility(View.INVISIBLE);

            et_bio.setText(bio.getText().toString());
            et_bio.requestFocus();

        }

    }

    public void save(View view) {
        et_name.setVisibility(View.GONE);
        et_dob.setVisibility(View.GONE);
        et_class.setVisibility(View.GONE);
        et_board.setVisibility(View.GONE);
        et_medium.setVisibility(View.GONE);
        et_bio.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        linlay.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);
        namee.setVisibility(View.VISIBLE);
        dob.setVisibility(View.VISIBLE);
        classs.setVisibility(View.VISIBLE);
        medium.setVisibility(View.VISIBLE);
        board.setVisibility(View.VISIBLE);
        bio.setVisibility(View.VISIBLE);

        pen_medium.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_medium.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_name.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_cls.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_board.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_bio.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_dob.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));
        pen_boardname.setImageDrawable(getResources().getDrawable(R.drawable.pen_selected));

        if (ed_name) {
            namee.setText(et_name.getText().toString());
            name.setText(et_name.getText().toString());
        }

        if (ed_dob) {
            dob.setText(et_dob.getText().toString());
        }

        if (ed_medium) {
            medium.setText(et_medium.getSelectedItem().toString());
        }

        if (ed_class) {
            classs.setText(et_class.getSelectedItem().toString());
        }

        if (ed_board) {
            board.setText(et_board.getSelectedItem().toString());
        }
        if (ed_bio) {
            bio.setText(et_bio.getText().toString());
        }

        signup();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void signup() {
        sharedpreferences = getSharedPreferences("details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("date", dob.getText().toString());
        editor.putString("class", classs.getText().toString());
        editor.putString("medium", medium.getText().toString());
        editor.putString("board", board.getText().toString());
        editor.putString("bio", bio.getText().toString());
//        Log.d("TAG0123", et_class.getSelectedItem().toString() + et_medium.getSelectedItem().toString() + et_board.getSelectedItem().toString());
        editor.apply();
        try {
            updateUserDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void updateUserDetails() {
        try {
            User user = new User();
            user.setName(name.getText().toString());
            user.setBoard(board.getText().toString());
//            user.setBoardname(state.getText().toString());
            user.setMedium(medium.getText().toString());
            user.setClasss(classs.getText().toString());
            user.setDob(dob.getText().toString());

            FirebaseDatabase.getInstance().getReference("users").child(userid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Update_Success", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Update to Database Failed! Try Again", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel(View view) {

        et_name.setVisibility(View.GONE);
        et_dob.setVisibility(View.GONE);
        et_class.setVisibility(View.GONE);
        et_board.setVisibility(View.GONE);
        et_medium.setVisibility(View.GONE);
        et_bio.setVisibility(View.GONE);
        linlay.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);
        namee.setVisibility(View.VISIBLE);
        dob.setVisibility(View.VISIBLE);
        classs.setVisibility(View.VISIBLE);
        medium.setVisibility(View.VISIBLE);
        board.setVisibility(View.VISIBLE);
        bio.setVisibility(View.VISIBLE);

        ed_bio = false;
        ed_board = false;
        ed_class = false;
        ed_name = false;
        ed_dob = false;
        ed_boardname = false;
        ed_medium = false;

        pen_medium.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_name.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_cls.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_board.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_bio.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_dob.setImageDrawable(getResources().getDrawable(R.drawable.editpen));
        pen_boardname.setImageDrawable(getResources().getDrawable(R.drawable.editpen));

    }

    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        et_dob.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));

    }

    public void changepicture(View view) {
        Toast.makeText(getApplicationContext(), "Option Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.d("check_click", "onNavigationItemSelected: id " + id);

        if (id == R.id.nav_profile) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(ProfileActivity.this, DashboardActivity.class));
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
            startActivity(new Intent(ProfileActivity.this, SubjectActivity.class));
        }
        return true;
    }

    private void logout() {

       AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        Intent intent = new Intent(ProfileActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        removeUserDetails();
                        startActivity(intent);
                    }

                    private void removeUserDetails() {
                        GoogleSignInOptions gso = new GoogleSignInOptions.
                                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                build();

                        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                        googleSignInClient.signOut();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Logout");
        alert.show();
        navigationView.getMenu().getItem(2).setChecked(true);

    }
}