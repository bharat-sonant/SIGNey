package com.sonant.dsin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.ui.PdfActivityIntentBuilder;
import com.unity3d.player.UnityPlayerActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter1 extends BaseExpandableListAdapter {
    CommonProgressBar commonProgressBar = new CommonProgressBar();

    DatabaseReference rootRef;
    String chapterpdfurlll;
    String chapternamelink;

    private Activity context;
    int lastparent = -1;
    int pastchild = -1;
    String expandedListText;
    private List<String> expandableListTitle;
    private HashMap<String, List<SubjectAdapter>> expandableListDetail;


    static class ViewHolder {
        ProgressBar progressBarr;
        RelativeLayout relativeLayout;
        ImageView book, hand;
        TextView pageNo, expandableTextView;
    }

    public CustomExpandableListAdapter1(Activity context, List<String> expandableListTitle,
                                        HashMap<String, List<SubjectAdapter>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        Log.d("TAG", "getChildView: zvbffdsdfbf: " + listPosition);
//        Log.d("hello", lastparent + "/" + patchily);
        final SubjectAdapter currentSubjectAdapter = (SubjectAdapter) getChild(listPosition, expandedListPosition);

        Log.d("TAG", "getChildView: bnmsdfsdfbnm" + currentSubjectAdapter);

        expandedListText = currentSubjectAdapter.getSubtopic();


        final String pageNo = currentSubjectAdapter.getPgno();
        chapterpdfurlll = currentSubjectAdapter.getChapterpdfurl();
        chapternamelink = currentSubjectAdapter.getChaptername();

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.subject_subheading, parent, false);


            holder.expandableTextView = (TextView) convertView
                    .findViewById(R.id.expandedListItem);
            holder.relativeLayout = convertView.findViewById(R.id.lInfo);
            holder.book = convertView.findViewById(R.id.book);
            holder.hand = convertView.findViewById(R.id.hand);
            holder.pageNo = convertView.findViewById(R.id.pageno1);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.expandableTextView.setText(expandedListText);

        holder.pageNo.setText(pageNo);

        if (currentSubjectAdapter.isSelected()) {
            holder.book.setVisibility(View.VISIBLE);
            holder.hand.setVisibility(View.VISIBLE);
            holder.pageNo.setVisibility(View.GONE);
        } else {
            holder.book.setVisibility(View.GONE);
            holder.hand.setVisibility(View.GONE);
            holder.pageNo.setVisibility(View.VISIBLE);
        }

        final View finalConvertView = convertView;


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                if (lastparent == -1 && pastchild == -1) {
                    lastparent = listPosition;
                    pastchild = expandedListPosition;
                }
                Log.d("subjectadapter0", lastparent + "/" + pastchild);

                for (SubjectAdapter subjectAdapter : expandableListDetail.get(expandableListTitle.get(lastparent))) {
                    if (count == pastchild) {
                        Log.d("subjectadapter0", "onClick:" + subjectAdapter.getSubtopic());
                        if (subjectAdapter.isSelected())
                            subjectAdapter.setSelected(false);
                        break;

                    }
                    count++;
//                    if (subjectAdapter.isSelected())
//                        subjectAdapter.setSelected(false);
                }
                lastparent = listPosition;
                pastchild = expandedListPosition;


                currentSubjectAdapter.setSelected(true);

                holder.book.setVisibility(View.VISIBLE);
                holder.hand.setVisibility(View.VISIBLE);
                holder.pageNo.setVisibility(View.GONE);

                notifyDataSetChanged();


            }
        });


        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.book.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.bookselected));
                holder.hand.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.hands));
                Log.d("TAG", "onClick:pagenoforpdf " + pageNo + expandedListText);

                // Download file from firbase to local database
                //  permissionOfWriteExternal();

                final Uri uri = Uri.parse("file:///android_asset/chapterfive.pdf");
                //   final Intent intent = PdfActivityIntentBuilder.fromUri(context, videoUri)
                final Intent intent = PdfActivityIntentBuilder.fromUri(context, uri)
                        .configuration(new PdfActivityConfiguration.Builder(context).textSelectionPopupToolbarEnabled(false).build())
                        .activityClass(EditablePdfActivity.class)
                        .build();
                context.startActivity(intent);


            }
        });

        holder.hand.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.hand.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.handselected));
                holder.book.setImageDrawable(finalConvertView.getResources().getDrawable(R.drawable.book));


                String helenKellerBookString = "It was the summer of 1880.@%A healthy baby girl was born in a small town in Alabama" +
                        ".@%Her parents loved her dearly and named her Helen Keller.@%But one day, the baby" +
                        " became ill and day after day, her fever stayed high.@%" +
                        "Everyone in the family tried to help her to get better, but" +
                        " all they could say was, “There is nothing more we can do.@%" +
                        "The baby may not live.@%" +
                        "Helen lived.@%But she was not the same after her illness.@%" +
                        "“Something is very wrong,” her mother said.@%At last they" +
                        " found out what was wrong.@%The child could not see or hear.@%" +
                        "The baby grew into a little girl.@%Her parents felt sorry for" +
                        " her.@%Helen often cried and held on to her mother.@%“Give the" +
                        " poor child what she wants,” her father would say.@%" +
                        "Though Helen could not hear or see, she was a bright" +
                        " little girl.@%Some people thought Helen could not learn" +
                        " anything.@%Her mother did not agree.@%“Helen is very smart,”" +
                        " she said and added, “the problem is, how can we reach" +
                        " her.@%She is locked up inside herself." +
                        "@%" +
                        "Helen began to" +
                        " grow wild.@%" + "She would not let anyone comb her hair.@%Her clothes were always dirty." +
                        "@%She was often angry.@%Sometimes she" +
                        " even lay on the floor and kicked her feet.@%" +
                        "Her parents thought that they should find a teacher" +
                        " for her.@%Miss Sullivan, a young teacher agreed to help" +
                        " Helen to learn to see the world.@%Miss Sullivan gave" +
                        " Helen a doll.@%“D_O_L_L” spells" +
                        " doll.@%She spelt the " +
                        "word with her" +
                        " fingers into" +
                        " Helen’s hand.@%" +
                        "She made the" +
                        " letters with" +
                        " special hand" +
                        " signs.@%" +
                        "Helen copied" +
                        " her teacher and" +
                        " spelt D_O_L_L" +
                        " too, but she did not" +
                        " understand what she was" +
                        " doing.@%Helen liked Miss Sullivan.@%She was strict but" +
                        " kind.@%She spelt a lot of words for Helen, on her hand," +
                        " day and night, to make Helen understand.@%" +
                        "One day, her teacher made Helen put her hand into" +
                        " running water.@%Then, she spelt W_A_T_E_R.@%Suddenly" +
                        " Helen understood that W_A_T_E_R meant something" +
                        " wet, running over her hand.@%She understood that words were the most important things in" +
                        " the world.@%Words would tell her" +
                        " everything she wanted to know.@%";

                Intent intent = new Intent(context, UnityPlayerActivity.class);
                intent.putExtra("selectedDataFromUnity", helenKellerBookString);
                context.startActivity(intent);

            }
        });


        return finalConvertView;
    }


    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
//        return super.getChildType(groupPosition, childPosition);
        return groupPosition;
    }

    @Override
    public int getChildTypeCount() {
//        return super.getChildTypeCount();
        return getGroupCount();
    }


    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String listTitle = (String) getGroup(listPosition);

//        ArrayList<String> all = new ArrayList<String>();
//
//        all.add(listTitle);

        // Log.d("TAG", "getGroupView: skiukhfdsf"+all);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.subject_heading, parent, false);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.chaptername);
        SubjectAdapter subjectAdapter = new SubjectAdapter();

        Log.d("TAG", "getGroupView:njibkjk " + subjectAdapter.getPosition());


        listTitleTextView.setText(listTitle);


        TextView chapternumbers = (TextView) convertView
                .findViewById(R.id.listTitle);


        chapternumbers.setText("Chapter " + (listPosition + 1));
        chapternumbers.setTypeface(null, Typeface.BOLD);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


    private void permissionOfWriteExternal() {

        Dexter.withContext(context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //   Toast.makeText(SplashScreen.this, "Access granted Write", Toast.LENGTH_SHORT).show();
                        downloadPdfFromFirebase();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        Toast.makeText(context, "You need to give Access to download books", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "You can give storage permission manually", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void downloadPdfFromFirebase() {

//Todo i was about to send data from firabse also remame the chapter name so that we can get the data as we wanted save in local with same refeabce

        try {

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(chapterpdfurlll).child(chapternamelink + ".pdf");

            File file = new File(Environment.getExternalStorageDirectory(), "Sonant Pdf");

            if (!file.exists()) {
                file.mkdirs();
            }

            final File localFile = new File(file, chapternamelink + ".pdf");

            if (!localFile.exists()) {

                commonProgressBar.setProgressDialog(context, context);

                Toast.makeText(context, "Pdf downloading...", Toast.LENGTH_SHORT).show();

                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        commonProgressBar.closeDialog(context);

                        openpdf();
                        //  Intent intent = new Intent(context, TestingActivity.class);
                        //   context.startActivity(intent); openPdf();
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        Toast.makeText(context, "Pdf download failed ", Toast.LENGTH_SHORT).show();

                        Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                    }
                });


            } else {
                openpdf();
            }


        } catch (Exception e) {
        }

    }

    private void openpdf() {
        commonProgressBar.setProgressDialog(context, context);

        try {

            Toast.makeText(context, "Pdf opening from Storage", Toast.LENGTH_SHORT).show();
            File root = new File(String.valueOf("file://" + Environment.getExternalStorageDirectory()), "Sonant Pdf");

            File wardFile = new File(root, chapternamelink + ".pdf");
            Uri videoUri = Uri.parse(String.valueOf(wardFile));
            commonProgressBar.closeDialog(context);


            // final Uri uri = Uri.parse("file:///android_asset/chapterfive.pdf");
            final Intent intent = PdfActivityIntentBuilder.fromUri(context, videoUri)
                    // final Intent intent = PdfActivityIntentBuilder.fromUri(context, uri)
                    .configuration(new PdfActivityConfiguration.Builder(context).textSelectionPopupToolbarEnabled(false).build())
                    .activityClass(EditablePdfActivity.class)
                    .build();
            context.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(context, "failed2" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}