package com.kittyapplication.core.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.kittyapplication.core.R;


public class DialogUtils {

    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.please_wait));
        return progressDialog;
    }

    public static Dialog createDialog(Context context,
                                      @StringRes int titleId, @StringRes int messageId,
                                      @StringRes int postiveTitle, @StringRes int negativeTitle,
                                      View view,
                                      DialogInterface.OnClickListener positiveClickListener,
                                      DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setView(view);
        builder.setPositiveButton(postiveTitle, positiveClickListener);
        builder.setNegativeButton(negativeTitle, negativeClickListener);

        return builder.create();
    }
}