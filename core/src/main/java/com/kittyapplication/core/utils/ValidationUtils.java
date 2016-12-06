package com.kittyapplication.core.utils;

import android.content.Context;
import android.widget.EditText;

import com.kittyapplication.core.R;

/**
 * Created by Dhaval Soneji Riontech on 21/11/16.
 */
public class ValidationUtils {
    public static boolean isValidUsername(EditText edtUserName, Context context) {
        if (StringUtils.isValidString(edtUserName.getText().toString())) {
            return true;
        } else {
            edtUserName.setError(context.getResources().getString(R.string.invalid_username));
            return false;
        }
    }

    public static boolean isValidPassword(EditText edtPassword, Context context) {
        if (StringUtils.isValidString(edtPassword.getText().toString())) {
            return true;
        } else {
            edtPassword.setError(context.getResources().getString(R.string.invalid_password));
            return false;
        }
    }
}
