package com.sonant.dsin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.folioreader.Config;
import com.folioreader.FolioReader;
import com.folioreader.model.HighLight;
import com.folioreader.model.locators.ReadLocator;
import com.folioreader.ui.base.OnSaveHighlight;
import com.folioreader.util.AppUtil;
import com.folioreader.util.OnHighlightListener;
import com.folioreader.util.ReadLocatorListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Pdfdisplay extends AppCompatActivity
 implements OnHighlightListener, ReadLocatorListener, FolioReader.OnClosedListener
{
    //
    private static final String LOG_TAG = Pdfdisplay.class.getSimpleName();
    private FolioReader folioReader;
    String pagenostring, topicname, s1;
    SharedPreferences shhh;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_pdfdisplay);
        pagenostring = getIntent().getStringExtra("pagenoforpdf");
        topicname = getIntent().getStringExtra("topicnoforpdf");

        Log.d("TAG", "onCreate: asdassddasd " + pagenostring);


// We can then use the data


        folioReader = FolioReader.get()
                .setOnHighlightListener(this)
                .setReadLocatorListener(this)
                .setOnClosedListener(this);
//        getHighlightsAndSave();

        findViewById(R.id.btn_raw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Config config = AppUtil.getSavedConfig(getApplicationContext());
                if (config == null)
                    config = new Config();
                config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);

                folioReader.setConfig(config, true)
                        .openBook(R.raw.diversity);
    }
}
        );

        findViewById(R.id.btn_assest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReadLocator readLocator = getLastReadLocator();

                Config config = AppUtil.getSavedConfig(getApplicationContext());
                if (config == null)
                    config = new Config();
                config.setAllowedDirection(Config.AllowedDirection.VERTICAL_AND_HORIZONTAL);

                folioReader.setReadLocator(readLocator);
                folioReader.setConfig(config, true)
                        .openBook("file:///android_asset/TheSilverChair.epub");
            }
        });
    }

    private ReadLocator getLastReadLocator() {

        String jsonString = loadAssetTextAsString("Locators/LastReadLocators/last_read_locator_1.json");
        return ReadLocator.fromJson(jsonString);
    }

    @Override
    public void saveReadLocator(ReadLocator readLocator) {
        Log.i(LOG_TAG, "-> saveReadLocator -> " + readLocator.toJson());
    }

    /*
     * For testing purpose, we are getting dummy highlights from asset. But you can get highlights from your server
     * On success, you can save highlights to FolioReader DB.
     */
    private void getHighlightsAndSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HighLight> highlightList = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    highlightList = objectMapper.readValue(
                            loadAssetTextAsString("highlights/highlights_data.json"),
                            new TypeReference<List<HighlightData>>() {
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (highlightList == null) {
                    folioReader.saveReceivedHighLights(highlightList, new OnSaveHighlight() {
                        @Override
                        public void onFinished() {
                            //You can do anything on successful saving highlight list
                        }
                    });
                }
            }
        }).start();
    }

    private String loadAssetTextAsString(String name) {
        BufferedReader in = null;
        try {
            StringBuilder buf = new StringBuilder();
            InputStream is = getAssets().open(name);
            in = new BufferedReader(new InputStreamReader(is));

            String str;
            boolean isFirst = true;
            while ((str = in.readLine()) != null) {
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(str);
            }
            return buf.toString();
        } catch (IOException e) {
            Log.e("HomeActivity", "Error opening asset " + name);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("HomeActivity", "Error closing asset " + name);
                }
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FolioReader.clear();
    }

    @Override
    public void onHighlight(HighLight highlight, HighLight.HighLightAction type) {
        Toast.makeText(this,
                "highlight id = " + highlight.getUUID() + " type = " + type,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFolioReaderClosed() {
        Log.v(LOG_TAG, "-> onFolioReaderClosed");
    }

    public void pdfdisplayqwe(View view) {

        // Retrieving the value using its keys the file name
// must be same in both saving and retrieving the data
        shhh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        s1 = shhh.getString("name", "");
        Log.d("TAG", "pdfdisplayqwe: sdfhsdfhsdfh" + s1);
        // Toast.makeText(Pdfdisplay.this, "" + s1, Toast.LENGTH_SHORT).show();
// We can then use the data
        DatabaseReference rootRefff = FirebaseDatabase.getInstance().getReference();
        rootRefff.child("Material/10/CBSE/" + s1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    return;
                }
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                String bookurlll = dataSnapshot.child("Bookurl").getValue(String.class);
                //  Log.d("TAG", "onDataChange:ngtnngf " + pagenostring + bookurlll);

                Intent intent = new Intent(Pdfdisplay.this, PdfDisplayNewM.class);
                intent.putExtra("bookurlpdffff", bookurlll);
                intent.putExtra("bookpagepdffff", pagenostring);
                startActivity(intent);


            }


//            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


    }
}