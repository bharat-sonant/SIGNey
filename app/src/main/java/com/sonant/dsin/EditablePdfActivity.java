package com.sonant.dsin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatDrawableManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pspdfkit.datastructures.Range;
import com.pspdfkit.datastructures.TextSelection;
import com.pspdfkit.ui.PdfActivity;
import com.pspdfkit.ui.special_mode.controller.TextSelectionController;
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager;
import com.pspdfkit.ui.toolbar.ContextualToolbar;
import com.pspdfkit.ui.toolbar.ContextualToolbarMenuItem;
import com.pspdfkit.ui.toolbar.TextSelectionToolbar;
import com.pspdfkit.ui.toolbar.ToolbarCoordinatorLayout;
import com.sonant.dsin.testing.TestingActivitythree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EditablePdfActivity extends PdfActivity implements ToolbarCoordinatorLayout.OnContextualToolbarLifecycleListener,
        TextSelectionManager.OnTextSelectionChangeListener, TextSelectionManager.OnTextSelectionModeChangeListener {

    private ContextualToolbarMenuItem customTextSelectionAction;
    String selectiontext, seletionbefore, test1, test2;
    int c = 1;
    TextSelection m, mtwo;
    int wordindexpoint = 0;
    int pageIndex = 0, count = 0;
    int mfinalnumber = 0;
    int lengthOfCharacters = 0;
    String selectedTextReacts = null;
    String selectedtexttoremoveautoselect = null;


    @SuppressLint({"RestrictedApi", "ResourceType"})
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the activity as a callback for contextual toolbar changes.
        // It will be called once the `TextSelectionToolbar` is going to be presented.
        setOnContextualToolbarLifecycleListener(this);
        Objects.requireNonNull(getPdfFragment()).addOnTextSelectionModeChangeListener(this);
        getPdfFragment().addOnTextSelectionChangeListener(this);
        // getPdfFragment().addOnTextSelectionChangeListener(this);
        // Create a custom menu item that will be shown inside the text selection toolbar.
        customTextSelectionAction = ContextualToolbarMenuItem.createSingleItem(
                this,
                R.menu.custom_menu_toolbar_items,
                AppCompatDrawableManager.get().getDrawable(this, R.drawable.circlehandsfinaskfsdf),
                "Sign language interpreter",
                Color.rgb(66, 229, 199),
                Color.WHITE,
                ContextualToolbarMenuItem.Position.END,
                true
        );
        //  customTextSelectionAction.setIcon(getDrawable(R.drawable.name));

        downloadDataFromFirebase();

    }


    @SuppressLint("ResourceType")
    @Override
    public void onPrepareContextualToolbar(@NonNull ContextualToolbar toolbar) {
        // Add the custom action once the selection toolbar is being prepared.
        // At this point, you could also remove undesired actions from the toolbar.
        if (toolbar instanceof TextSelectionToolbar) {
            final List<ContextualToolbarMenuItem> menuItems = toolbar.getMenuItems();
            menuItems.clear();
            if (!menuItems.contains(customTextSelectionAction)) {
                menuItems.add(customTextSelectionAction);
                toolbar.setMenuItems(menuItems);

//                toolbar.setMenuItemEnabled(2131362634,false);
//               // toolbar.setMenuItemVisibility(2131362634, View.INVISIBLE);
//                toolbar.setMenuItemVisibility(2131362635, View.INVISIBLE);
//                toolbar.setMenuItemVisibility(2131362642, View.INVISIBLE);
//                toolbar.setMenuItemVisibility(2131362640, View.INVISIBLE);
//                toolbar.setMenuItemVisibility(2131362641, View.INVISIBLE);
//                toolbar.setMenuItemVisibility(2131362637, View.INVISIBLE);

            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onDisplayContextualToolbar(@NonNull ContextualToolbar toolbar) {
        // Register a click listener to handle taps on the custom text selection action.

        toolbar.setOnMenuItemClickListener((contextualToolbar, menuItem) -> {
            boolean handled = false;
            Log.d("TAG", "onDisplayContextualToolbar: " + menuItem.getId());

            if (menuItem.getId() == R.menu.custom_menu_toolbar_items) {

                if (c == 1) {
                    Intent intent = new Intent(EditablePdfActivity.this, TestingActivitythree.class);
                    intent.putExtra("p", seletionbefore);
                    startActivity(intent);
                    //  Toast.makeText(EditablePdfActivity.this, "" +seletionbefore, Toast.LENGTH_SHORT).show();
                    c = 2;
                } else {
                    Intent intent = new Intent(EditablePdfActivity.this, TestingActivitythree.class);
                    intent.putExtra("p", seletionbefore);
                    startActivity(intent);
                    //   Toast.makeText(EditablePdfActivity.this, "" + selectiontext, Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(EditablePdfActivity.this, "" + selectiontext, Toast.LENGTH_SHORT).show();
//                selectiontext = null;
                handled = true;
            }

            return handled;
        });
    }

    @Override
    public void onRemoveContextualToolbar(@NonNull ContextualToolbar toolbar) {
        toolbar.setOnMenuItemClickListener(null);
    }

    public boolean onBeforeTextSelectionChange(@Nullable TextSelection textSelectdfvdfion, @Nullable TextSelection textsdsdSelection1) {
        try {

            Log.d("TAG", "onBeforeTextSelectionChange: check on enter text selection mode");
            test1 = textSelectdfvdfion.text;
            test2 = textsdsdSelection1.text;

            Log.d("TAG", "zxcvdsvsdvsav:1 " + test1);
            Log.d("TAG", "zxcvdsvsdvsav:2 " + test2);


        } catch (Exception e) {

        }
        return true;
    }

    public void onAfterTextSelectionChange(@Nullable TextSelection textSelectionnnn, @Nullable TextSelection textSelection1123) {

        m = null;
        try {
            Log.d("TAG", "onEnterTextSelectionMode: werwerwerwerwerwerwerwerwer");
            seletionbefore = textSelectionnnn.text;
            selectiontext = textSelection1123.text;
            c = 2;

            Log.d("TAG", "zxcvdsvsdvsav:3 " + seletionbefore);
            Log.d("TAG", "zxcvdsvsdvsav:4 " + selectiontext);


            try {

                m = textSelectionnnn;
                Log.d("TAG", "zxcvdsvsdvsav:3 " + seletionbefore);

                Log.d("TAG", "topicSelection:dfgahgadfhagfkviheg " + selectedtexttoremoveautoselect);

                if (seletionbefore.equals(selectedtexttoremoveautoselect)) {

                    Log.d("TAG", "Testing 1: ");

                } else {

                    autoTextSelectionOnSelect();
                    //  selectedtexttoremoveautoselect = null;

                    Log.d("TAG", "Testing 2: ");

                }


            } catch (Exception e) {

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onEnterTextSelectionMode(@Nullable TextSelectionController cjhfhontroller) {

        Log.d("TAG", "onEnterTextSelectionMode: werwerwerwerwerwerwerwerwer");

        m = cjhfhontroller.getTextSelection();
        try {
            autoTextSelectionOnSelect();
        } catch (Exception e) {

        }
    }

    public void onExitTextSelectionMode(@NonNull TextSelectionController onexitTextselected) {
        c = 1;
    }

    @Override
    public List<Integer> onGenerateMenuItemIds(@NonNull List<Integer> menuItems) {
        menuItems.clear();
        return menuItems;
    }

    private void autoTextSelectionOnSelect() {
        count = 2;

        //todo------------------pageIndex--------------------------------

        pageIndex = Objects.requireNonNull(getPdfFragment()).getPageIndex();
        Log.d("TAG", "selectedTextReacts:pageIndex " + pageIndex);

        //todo------------------getRects--------------------------------

        selectedTextReacts = String.valueOf(m.textBlocks);
        Log.d("TAG", "selectedTextReacts " + selectedTextReacts);

        //todo-----------------convertStringRectsIntofloat----------------
        String seven = null;
        seven = String.valueOf(selectedTextReacts.charAt(7));
        String eight = null;
        eight = String.valueOf(selectedTextReacts.charAt(8));
        String nine = null;
        nine = String.valueOf(selectedTextReacts.charAt(9));
        String ten = null;
        ten = String.valueOf(selectedTextReacts.charAt(10));
        String eleven = null;
        eleven = String.valueOf(selectedTextReacts.charAt(11));
        String tweleve = null;
        tweleve = String.valueOf(selectedTextReacts.charAt(12));
        String rectX = seven + eight + nine + ten + eleven + tweleve;
        float v = 0.0f;
        v = Float.parseFloat(rectX);
        Log.d("TAG", "fdbvbb: " + v);
        String add = null;
        StringBuilder sb = new StringBuilder();
        int comaindex = 0;
        comaindex = selectedTextReacts.indexOf(",");
        comaindex++;
        int comaidexsecond = 0;
        comaidexsecond = comaindex;
        for (int i = 0; i < 7; i++) {
            int finalncomaidexsecond = 0;
            finalncomaidexsecond = comaidexsecond++;
            String chatat = null;
            chatat = String.valueOf(selectedTextReacts.charAt(finalncomaidexsecond));
            add = String.valueOf(sb.append(chatat));
        }
        Log.d("TAG", "add: " + add);
        float v1 = 0.0f;
        v1 = Float.parseFloat(add);

        //todo------------------getWordIndex--------------------------------

        wordindexpoint = getDocument().getCharIndexAt(pageIndex, v, v1);

        Log.d("TAG", "wordindexpoint " + wordindexpoint);
        int pagelength = 0;
        pagelength = getDocument().getPageTextLength(pageIndex);
        Log.d("TAG", "pagelength: " + pagelength);

        //todo------------------Beforefullstop--------------------------------

        String pageTextFromStartingToSeletedWord = null;
        pageTextFromStartingToSeletedWord = getDocument().getPageText(pageIndex, 0, wordindexpoint);
        Log.d("TAG", "pageTextFromStartingToSeletedWord: " + pageTextFromStartingToSeletedWord);
        StringBuilder input1 = new StringBuilder();
        input1.append(pageTextFromStartingToSeletedWord);
        String ReversedPageTextFromStartingToSeletedWord = null;
        ReversedPageTextFromStartingToSeletedWord = String.valueOf(input1.reverse());
        Log.d("TAG", "ReversedPageTextFromStartingToSeletedWord: " + ReversedPageTextFromStartingToSeletedWord);

        int selectedAfterQuestionMarktwo;
        selectedAfterQuestionMarktwo = ReversedPageTextFromStartingToSeletedWord.indexOf(".");

        int selectedAfterQuestionMarkthree;
        selectedAfterQuestionMarkthree = selectedAfterQuestionMarktwo;
        Log.d("TAG", "selectedAfterQuestionMarktwo: " + selectedAfterQuestionMarkthree);

//        //todo------------------BeforeQuestionMark--------------------------------
//        String textSelectedBeforeQuestionMark = getDocument().getPageText(pageIndex, 0, wordindexpoint);
//        Log.d("TAG", "textSelectedBeforeQuestionMark: " + textSelectedBeforeQuestionMark);
        StringBuilder input2 = new StringBuilder();
        input2.append(pageTextFromStartingToSeletedWord);
        String ReversedtextSelectedBeforeQuestionMark;
        ReversedtextSelectedBeforeQuestionMark = String.valueOf(input2.reverse());
        Log.d("TAG", "ReversedtextSelectedBeforeQuestionMark: " + ReversedtextSelectedBeforeQuestionMark);

        int selectedBeforeQuestionMarktwo = 0;
        selectedBeforeQuestionMarktwo = ReversedtextSelectedBeforeQuestionMark.indexOf("?");
        int selectedBeforeQuestionMarktwothree;
        selectedBeforeQuestionMarktwothree = selectedBeforeQuestionMarktwo;
        Log.d("TAG", "selectedBeforeQuestionMarktwothree: " + selectedBeforeQuestionMarktwothree);

        int closestIndexBeforeSentance = 0;

        Log.d("TAG", "autoTextSelectionOnSelect:selectedAfterQuestionMarkthree " + selectedAfterQuestionMarkthree);
        Log.d("TAG", "autoTextSelectionOnSelect:selectedBeforeQuestionMarktwothree " + selectedBeforeQuestionMarktwothree);

        //todo----Find Before closest Index B/w Full Stop And QuestionMark-----
        if (selectedAfterQuestionMarkthree == -1 && selectedBeforeQuestionMarktwothree == -1) {

            closestIndexBeforeSentance = pageTextFromStartingToSeletedWord.length();
        }
        {
            if (selectedAfterQuestionMarkthree == -1) {

                selectedAfterQuestionMarkthree = selectedBeforeQuestionMarktwothree + 1;
            } else if (selectedBeforeQuestionMarktwothree == -1) {

                selectedBeforeQuestionMarktwothree = selectedAfterQuestionMarkthree + 1;
            }
        }
        closestIndexBeforeSentance = Math.min(selectedAfterQuestionMarkthree, selectedBeforeQuestionMarktwothree);
        Log.d("TAG", "ClosestIndexBeforeSentance: " + closestIndexBeforeSentance);
        if (closestIndexBeforeSentance == -1) {
            closestIndexBeforeSentance = pageTextFromStartingToSeletedWord.length();

        }

        int ClosestIndexAfterSentance;
        //todo------------------Afterfullstop--------------------------------
        int customizelength = 0;
        customizelength = pagelength - wordindexpoint;
        Log.d("TAG", "customizelength: " + customizelength);
        int indexAfterFullStop;
        indexAfterFullStop = getDocument().getPageText(pageIndex, wordindexpoint, customizelength).indexOf(".");
        Log.d("TAG", "indexAfterFullStop: " + indexAfterFullStop);

        //todo------------------AfterQuestionMark--------------------------------
        String indexAfterQuestionMark = null;
        indexAfterQuestionMark = getDocument().getPageText(pageIndex, wordindexpoint, customizelength);
        Log.d("TAG", "indexAfterQuestionMark: " + indexAfterQuestionMark);
        int indexAfterQuestion = 0;
        indexAfterQuestion = indexAfterQuestionMark.indexOf("?");
        Log.d("TAG", "indexAfterQuestion: " + indexAfterQuestion);

        //todo-----Find closest Index B/w Full Stop And QuestionMark--------------
        if (indexAfterFullStop == -1) {
            indexAfterFullStop = indexAfterQuestion + 1;
        } else if (indexAfterQuestion == -1) {
            indexAfterQuestion = indexAfterFullStop + 1;
        }

        ClosestIndexAfterSentance = Math.min(indexAfterFullStop, indexAfterQuestion);
        Log.d("TAG", "ClosestIndexAfterSentance: " + ClosestIndexAfterSentance);

        //todo----Index length should be from first Special symbol to last Special symbol----

        if (closestIndexBeforeSentance == 0) {

            mfinalnumber = closestIndexBeforeSentance;

        } else {
            mfinalnumber = wordindexpoint - closestIndexBeforeSentance;

        }

        lengthOfCharacters = closestIndexBeforeSentance + ClosestIndexAfterSentance;


        //todo------------------condition for the topic selection---------------------------

        topicSelection();


    }

    private void topicSelection() {


        //here we already took the lenght of first sentance
        // meanwhile we are just taking lengthOfFirstSentanceInFirebase lenght 8 for example
        // meanwhile we are just taking lengthOfSecondSentanceInFirebase lenght 45 for example

        int lengthOfFirstSentanceInFirebase = 8;
        int lengthOfSecondSentanceInFirebase = 45;

        Log.d("TAG", "wordindexpoint " + wordindexpoint);

        if (wordindexpoint < lengthOfFirstSentanceInFirebase) {
            Log.d("TAG", "autoTextSelectionOnSelect:123");
            getPdfFragment().enterTextSelectionMode(pageIndex, new Range(mfinalnumber, lengthOfFirstSentanceInFirebase));

        } else {

            int secondsentancelength = lengthOfSecondSentanceInFirebase - lengthOfFirstSentanceInFirebase;

            if (wordindexpoint >= lengthOfFirstSentanceInFirebase && wordindexpoint <= lengthOfSecondSentanceInFirebase) {

                Log.d("TAG", "autoTextSelectionOnSelect:321 ");
                getPdfFragment().enterTextSelectionMode(pageIndex, new Range(lengthOfFirstSentanceInFirebase + 1, secondsentancelength + 1));

            } else {

                //todo------------------SelectWordAutomatic----------------------

                getPdfFragment().enterTextSelectionMode(pageIndex, new Range(mfinalnumber, lengthOfCharacters + 1));
                selectedtexttoremoveautoselect = getDocument().getPageText(pageIndex, mfinalnumber, lengthOfCharacters + 1);
                Log.d("TAG", "topicSelection:selectedtexttoremoveautoselect " + selectedtexttoremoveautoselect);


            }

        }


    }

    private void downloadDataFromFirebase() {

        FirebaseDatabase.getInstance().getReference().child("Sampledatabasefortestingmohit").child("1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String datasssnap = snapshot.toString();
                ArrayList<String> sampleList = new ArrayList<String>();

                Log.d("TAG", "onDataChangedatasssnap " + datasssnap);

                for (DataSnapshot snap : snapshot.getChildren()) {

                    String smaple = String.valueOf(snap.child("actual").getValue());
                    Log.d("TAG", "onDataChangesample " + smaple);

                    int samplelength = smaple.length();
                    Log.d("TAG", "onDataChangesample " + samplelength);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

