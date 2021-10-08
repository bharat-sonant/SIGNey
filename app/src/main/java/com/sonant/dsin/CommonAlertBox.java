package com.sonant.dsin;

import android.app.AlertDialog;
import android.content.Context;

public class CommonAlertBox {
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    public void showAlertBox(String message, String pBtn, Context ctx) {
        hideAlertBox();
        builder = new AlertDialog.Builder(ctx);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(pBtn, (dialog, id) -> dialog.cancel());
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void hideAlertBox() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }
}
