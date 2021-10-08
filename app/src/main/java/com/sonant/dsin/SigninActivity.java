package com.sonant.dsin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1;
    EditText usernameEt, passEt;
    Button signB;
    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;
    boolean allowClick = true;

    StorageReference storageReference;
    StorageReference sReference;

    //    private static final int RC_SIGN_IN = 1;
    CardView signInButton;
    DatabaseReference rootRef;
    SharedPreferences sharedpreferences;
    //    int autoSave;
    CommonAlertBox commonAlertBox = new CommonAlertBox();
    CommonProgressBar commonProgressBar = new CommonProgressBar();
    ProgressBar progressBar;
//    GoogleSignInAccount account;
//    private GoogleApiClient googleApiClient;
//    private int count = 0;


    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_activityy);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        inIt();
        //  setAction();
        createRequest();
        ref = FirebaseDatabase.getInstance().getReference();

        sharedpreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
        final int j = sharedpreferences.getInt("key", 0);
        if (j > 0) {
            Intent activity = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(activity);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //       rootRef = FirebaseDatabase.getInstance().getReference();
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

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
                    commonProgressBar.setProgressDialog(SigninActivity.this, SigninActivity.this);
                    startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminate(true);
//
//                    //Once you click login, it will add 1 to shredPreference which will allow autologin in onCreate
//                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//                    startActivityForResult(intent, RC_SIGN_IN);
                } else {
                    //commenting some code also changing Nointernet activity to Dashboard activity
                    Intent intent = new Intent(SigninActivity.this, NoInternet.class);
                    startActivity(intent);
                }
            }
        });

    }


    private void inIt() {
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
    }

    private void setAction() {
        findViewById(R.id.signin).setOnClickListener(view -> signIn());

    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("427232668165-ntp53djmlq8nfci1cg7b1ki457rvqt16.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        if (allowClick) {
            allowClick = true;
            commonProgressBar.setProgressDialog(SigninActivity.this, SigninActivity.this);
            startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

                Toast.makeText(SigninActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                commonProgressBar.closeDialog(SigninActivity.this);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            commonProgressBar.closeDialog(SigninActivity.this);
                            Intent intent = new Intent(SigninActivity.this, SignupSecond.class);
                            startActivity(intent);
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                ref = FirebaseDatabase.getInstance().getReference("users");
                                String uid = user.getUid();
                                SharedPreferences sharedPreferences = getSharedPreferences("userUniqueIdsahredPref",MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("userUniqueId", uid);
                                myEdit.commit();

                                Log.d("TAG", "onComplete:uid "+ uid);
                                ref.child(uid).child("uid").setValue(uid);
                                ref.child(uid).child("name").setValue(user.getDisplayName());
                                ref.child(uid).child("email").setValue(user.getEmail());
                                ref.child(uid).child("photo").setValue(String.valueOf(user.getPhotoUrl()));
                            }

                        } else {
                            Toast.makeText(SigninActivity.this, "SignIn Failed", Toast.LENGTH_SHORT).show();
                            commonProgressBar.closeDialog(SigninActivity.this);
                        }
                    }
                });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()) {
//            account = result.getSignInAccount();
//
//            checkprofile(account.getId());
//            Log.d("accountid", account.getId());
//        } else {
//            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
//        }
//    }
    //check if profile exist in firebase
//    private void checkprofile(String id) {
//        DatabaseReference hotelRef = rootRef.child("users");
//        final String accountid = id;
//        hotelRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    String id = data.getKey();
//
//                    if (id.equals(accountid)) {
//                        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", Context.MODE_PRIVATE);
//                        autoSave = 1;
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putInt("key", autoSave);
//                        editor.apply();
//                        count = 1;
//                        //getting details
//                        String board = data.child("board").getValue(String.class);
//                        String name = data.child("name").getValue(String.class);
//                        String medium = data.child("medium").getValue(String.class);
//                        String dob = data.child("dob").getValue(String.class);
//                        String classs = data.child("classs").getValue(String.class);
//                        String boardname = data.child("boardname").getValue(String.class);
//                        String profileurl = "";
//
//                        if (account.getPhotoUrl() == null) {
//                            profileurl = "https://firebasestorage.googleapis.com/v0/b/dsiapp-103c4.appspot.com/o/defaultuser.png?alt=media&token=a32da964-f3f6-4333-b105-b6ae44684099";
//                        } else {
//                            profileurl = account.getPhotoUrl().toString();
//                        }
//                        String email = account.getEmail();
//
//                        SharedPreferences sharedpreferences1 = getSharedPreferences("details", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor1 = sharedpreferences1.edit();
//                        editor1.putString("name", name);
//                        editor1.putString("date", dob);
//                        editor1.putString("class", classs);
//                        editor1.putString("medium", medium);
//                        editor1.putString("board", board);
//                        editor1.putString("boardname", boardname);
//                        editor1.putString("profileurl", profileurl);
//                        editor1.putString("email", email);
//                        editor1.apply();
//                        Log.d("values", "name " + name + "dob " + dob + "classs " + classs + "medium " + medium + "board " + board + "boardname " + boardname + "profileurl " + profileurl + "+email " + email);
//                        break;
//                    } else {
//                        continue;
//                    }
//                }
//                gotoProfile();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }

    //navigation to activity if account already exist
//    private void gotoProfile()
//    {
//        if (count == 1) {
//            Toast.makeText(getApplicationContext(), "Account exist! Welcome", Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(SigninActivity.this, DashboardActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(getApplicationContext(), "Didnt find account! Signup", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(SigninActivity.this, SignupSecond.class);
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    public void abcd(View view) {
//        TextView signup = (TextView) findViewById(R.id.signuptext);
//
//        signup.setTextColor(getResources().getColor(R.color.Red));
//
//        Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
//        startActivity(intent);
//    }
//
    public void nextactivity(View view) {
      //  Intent intent = new Intent(SigninActivity.this, DashboardActivity.class);
    //    startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
