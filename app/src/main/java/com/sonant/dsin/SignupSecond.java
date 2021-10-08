package com.sonant.dsin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SignupSecond extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Spinner medium, cla, board;
    EditText name;
    AutoCompleteTextView state;
    Button signup;
    String userid, date, email;
    LinearLayout linlay;
    String profileurl = "";
    ProgressBar progressBar;
    private Calendar calendar;
    GoogleSignInAccount account;
    private TextView dateView;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    String[] states;
    SharedPreferences sharedpreferences;
    private int year, month, day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_second);
        progressBar = findViewById(R.id.progressBar2);
        linlay = findViewById(R.id.linlay);
        state = findViewById(R.id.board_name);
        medium = findViewById(R.id.medium);
        board = findViewById(R.id.board);
        // name = findViewById(R.id.name);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        cla = findViewById(R.id.classs);
        sharedpreferences = getSharedPreferences("details", Context.MODE_PRIVATE);
        states = getResources().getStringArray(R.array.states);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        dateView = (TextView) findViewById(R.id.date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month + 1, day);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, states);

        state.setThreshold(1);

        state.setAdapter(adapter);
        date = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date());

        board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (board.getItemAtPosition(position).equals("State Board")) {
                    linlay.setVisibility(View.VISIBLE);
                } else {
                    linlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void signup(View view) {
        progressBar.setVisibility(View.VISIBLE);
//        if (name.getText().toString() == null) {
//            name.setError("Please enter name ");
//            name.requestFocus();
//        } else
        if (dateView.getText().equals(date)) {
            dateView.setError("Invalid date");
            Toast.makeText(getApplicationContext(), "Please select Date of Birth", Toast.LENGTH_SHORT).show();
            dateView.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else if (cla.getSelectedItem().equals("Class")) {

            Toast.makeText(getApplicationContext(), "Please Select Your Class", Toast.LENGTH_SHORT).show();
            cla.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else if (medium.getSelectedItem().equals("Medium")) {
            Toast.makeText(getApplicationContext(), "Please Select Your Medium", Toast.LENGTH_SHORT).show();
            medium.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else if (board.getSelectedItem().equals("Board")) {
            Toast.makeText(getApplicationContext(), "Please Select Your Board", Toast.LENGTH_SHORT).show();
            board.requestFocus();
            progressBar.setVisibility(View.GONE);
        } else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            //    editor.putString("name", name.getText().toString());
            editor.putString("date", dateView.getText().toString());
            editor.putString("class", cla.getSelectedItem().toString());
            editor.putString("medium", medium.getSelectedItem().toString());
            editor.putString("board", board.getSelectedItem().toString());
            editor.putString("boardname", state.getText().toString());
            editor.putString("profileurl", profileurl);
            editor.putString("email", email);
            //     Log.d("values", name.getText().toString() + dateView.getText().toString() + cla.getSelectedItem().toString() + medium.getSelectedItem().toString() + board.getSelectedItem().toString());
            editor.apply();
            try {
                writeNewUser();
            } catch (Exception e) {
                e.printStackTrace();
            }
            signup = findViewById(R.id.signupp);
            signup.setTextColor(getResources().getColor(R.color.Red));
        }
    }

    private void writeNewUser() {
        try {
//            User user = new User();
//         //   user.setName(name.getText().toString());
//            user.setBoard(board.getSelectedItem().toString());
//            user.setBoardname(state.getText().toString());
//            user.setMedium(medium.getSelectedItem().toString());
//            user.setClasss(cla.getSelectedItem().toString());
//            user.setDob(dateView.getText().toString());


            HashMap<String, Object> userDetails = new HashMap<>();
            userDetails.put("dob", dateView.getText().toString());
            userDetails.put("classs", cla.getSelectedItem().toString());
            userDetails.put("medium", medium.getSelectedItem().toString());
            userDetails.put("board", board.getSelectedItem().toString());
            userDetails.put("boardname", state.getText().toString());

            String classsTextView = cla.getSelectedItem().toString();

            Log.d("TAG", "writeNewUser: qweqwe" + classsTextView);


            SharedPreferences sh = getSharedPreferences("userUniqueIdsahredPref", MODE_PRIVATE);
            String userUnique = sh.getString("userUniqueId", "");


            FirebaseDatabase.getInstance().getReference("users").child(userUnique).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(SignupSecond.this, "Registered Sucessfullly", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                        if (classsTextView.equals("4th"))
                        {

                            Intent intent = new Intent(SignupSecond.this, DashboardActivity.class);
                            //intent.putExtra("name", name.getText().toString());
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else
                        {
                            SharedPreferences sharedPreferencess = getSharedPreferences("getTheUserName", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferencess.edit();
                            editor.putInt("avaiablebooks", 1);
                            editor.commit();

                          //  SharedPreferences sh = getSharedPreferences("getTheUserName", MODE_PRIVATE);
                         //   int getTheUserName = sh.getInt("userUniqueId", 5);

                            Intent intent = new Intent(SignupSecond.this, AvailableBooksActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }


                    } else {

                        Toast.makeText(SignupSecond.this, "registration_failed", Toast.LENGTH_LONG).show();

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDate(View view) {
        showDialog(999);

    }

//    private void handleSignInResult(GoogleSignInResult result) {
//        if (result.isSuccess()) {
//            account = result.getSignInAccount();
//            name.setText(account.getDisplayName());
//            userid = account.getId();
//
//
//            profileurl = "";
//
//            if (account.getPhotoUrl() == null) {
//                profileurl = "https://firebasestorage.googleapis.com/v0/b/dsiapp-103c4.appspot.com/o/defaultuser.png?alt=media&token=a32da964-f3f6-4333-b105-b6ae44684099";
//            } else {
//                profileurl = account.getPhotoUrl().toString();
//            }
//        }
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if (opr.isDone()) {
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }

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
        dateView.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}