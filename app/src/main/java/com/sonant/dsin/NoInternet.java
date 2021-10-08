package com.sonant.dsin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoInternet extends AppCompatActivity {
    Context context;
    private long pressedTime;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        context = getApplicationContext();
    }

    public void onClick(View view) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            Toast.makeText(context, "Your connection is back", Toast.LENGTH_SHORT).show();
            finish();


        } else {
            Toast.makeText(context, "Could'nt find a connection! Check and try again", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
