package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1;
    CardView signInButton;
    TextView textView;
    ProgressBar progressBar;
    SharedPreferences sharedpreferences;
    GoogleSignInAccount account;
    DatabaseReference rootRef;
    int autoSave;
    private int count = 0;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
//        Default is 0 so autologin is disabled
        rootRef = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("key", 0);
        if (j > 0) {
            Intent activity = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(activity);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = findViewById(R.id.signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager cm =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {

                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);

                    //Once you click login, it will add 1 to shredPreference which will allow autologin in onCreate
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, RC_SIGN_IN);

                } else {
                    Intent intent = new Intent(SignUpActivity.this, NoInternet.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.INVISIBLE);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
//            autoSave = 1;
//            SharedPreferences.Editor editor = sharedpreferences.edit();
            account = result.getSignInAccount();
            checkprofile(account.getId());
//            editor.putInt("key", autoSave);
//            editor.apply();
        } else {
            Toast.makeText(getApplicationContext(), "Sign Up cancel", Toast.LENGTH_LONG).show();
        }
    }
//check firebase for profile
    private void checkprofile(String id) {
        DatabaseReference hotelRef = rootRef.child("users");
        final String accountid = id;
        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String id = data.getKey();

                    if (id.equals(accountid)) {
                        //making auto login
                        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
                        autoSave = 1;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("key", autoSave);

                        editor.apply();
                        count = 1;
                        //getting details


                        String board = data.child("board").getValue(String.class);
                        String name = data.child("name").getValue(String.class);
                        String medium = data.child("medium").getValue(String.class);
                        String dob = data.child("dob").getValue(String.class);
                        String classs = data.child("classs").getValue(String.class);
                        String boardname = data.child("boardname").getValue(String.class);
                        String profileurl = "";

                        if (account.getPhotoUrl().equals(null)) {
                            profileurl = "https://firebasestorage.googleapis.com/v0/b/dsiapp-103c4.appspot.com/o/defaultuser.png?alt=media&token=a32da964-f3f6-4333-b105-b6ae44684099";
                        } else {
                            profileurl = account.getPhotoUrl().toString();
                        }
                        String email = account.getEmail();

                        SharedPreferences sharedpreferences1 = getSharedPreferences("details", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
                        editor1.putString("name", name);
                        editor1.putString("date", dob);
                        editor1.putString("class", classs);
                        editor1.putString("medium", medium);
                        editor1.putString("board", board);
                        editor1.putString("boardname", boardname);
                        editor1.putString("profileurl", profileurl);
                        editor1.putString("email", email);
                        editor1.apply();


                        Log.d("values", "name " + name + "dob " + dob + "classs " + classs + "medium " + medium + "board " + board + "boardname " + boardname + "profileurl " + profileurl + "+email " + email);

                        break;
                    } else {
                        continue;
                    }

                }
                gotoProfile();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //navigation if profile exists in firebase
    private void gotoProfile() {

        if (count == 1) {
            Toast.makeText(getApplicationContext(), "Account exist! Welcome", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SignUpActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Didnt find account! Signup", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, SignupSecond.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void abcd(View view) {
        TextView signin = (TextView) findViewById(R.id.signintext);
        signin.setTextColor(getResources().getColor(R.color.Red));
        Intent intent = new Intent(this, SigninActivity.class);

        startActivity(intent);
    }
}
