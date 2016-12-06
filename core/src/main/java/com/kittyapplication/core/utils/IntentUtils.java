package com.kittyapplication.core.utils;

import android.os.Bundle;

import java.util.Set;

/**
 * Created by MIT on 8/19/2016.
 */
public class IntentUtils {
    public static void printBundle(Bundle bundle) {
        if (bundle != null) {
            Set<String> set = bundle.keySet();
            System.out.print("Bundle  key => value ");
            for (String s : set) {
                System.out.print(s + " => " + bundle.get(s));
            }
        }
    }
}
