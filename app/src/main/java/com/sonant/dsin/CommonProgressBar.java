package com.sonant.dsin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;


public class CommonProgressBar {
    ProgressDialog dialog;
    public void setProgressDialog(Context context, Activity activity) {
        closeDialog(activity);
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait....");
        dialog.setMessage("Loading....");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        if (!dialog.isShowing() && !activity.isFinishing()) {
            dialog.show();
        }
    }

    public void closeDialog(Activity activity) {
        if (dialog != null) {
            if (dialog.isShowing() && !activity.isFinishing()) {
                dialog.dismiss();
            }
        }
    }




}
