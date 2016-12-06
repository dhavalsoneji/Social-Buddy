package com.kittyapplication.core.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.kittyapplication.core.R;

import java.util.List;

/**
 * Created by Dhaval Soneji Riontech on 17/11/16.
 */

public class StringUtils {
    private static final String TAG = StringUtils.class.getSimpleName();

    /**
     * Check Server data is Valid or not
     *
     * @param str
     * @return
     */
    public static boolean isValidString(String str) {
        try {
            if (str != null && str.length() > 0 && !TextUtils.isEmpty(str)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Applog.e(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static void makeLinkClickable(final Context context, SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                context.startActivity(intent);
                // Do something with span.getURL() to handle the link click...
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public static void setTextViewHTML(Context context, TextView text, String html) {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for (URLSpan span : urls) {
            makeLinkClickable(context, strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static <T> Boolean isValidList(List<T> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String removeStringAfterGivenString(String string, String searchString) {
        int indexOfDisclaimerText = string.indexOf(searchString);
        String description = string;
        if (indexOfDisclaimerText > 0) {
            description = description.substring(0, indexOfDisclaimerText);
        }
        return description;
    }

    public static SpannableString spanningAStringColor(Context context, String sourceString, String spanString) {
        int startIndex = 0;
        int lastIndex = spanString.length();

        SpannableString spannableString = new SpannableString(sourceString);
        spannableString.setSpan(new ForegroundColorSpan
                        (ContextCompat.getColor(context, R.color.colorSubjectMarksText)),
                startIndex, lastIndex, 0);

        return spannableString;
    }
}
