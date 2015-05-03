package com.kamalan.phonebook.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class Storage
{
    private static final String PREFS_NAME = "myPrefs";
    private static final String ACCOUNT_NAME = "accountName";

    public static void setAccountName(Context context, String accountName)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(ACCOUNT_NAME, accountName);
        editor.apply();
    }

    public static String getAccountName(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(ACCOUNT_NAME, null);
    }
}
