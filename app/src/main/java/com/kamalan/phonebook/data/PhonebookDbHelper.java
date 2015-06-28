package com.kamalan.phonebook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kamalan.phonebook.data.PhonebookContract.ContactEntry;

/**
 * Created by Hesam Kamalan on 6/21/15
 */
public class PhonebookDbHelper extends SQLiteOpenHelper
{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "phonebook.db";

    private static PhonebookDbHelper INSTANCE;

    public static synchronized PhonebookDbHelper getInstance(final Context context)
    {
        if (PhonebookDbHelper.INSTANCE == null)
        {
            PhonebookDbHelper.INSTANCE = new PhonebookDbHelper(context.getApplicationContext());
        }

        return PhonebookDbHelper.INSTANCE;
    }

    // Enforce singleton
    private PhonebookDbHelper(final Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        ContactEntry.onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next line
        // should be your top priority before modifying this method.
        ContactEntry.onUpgrade(db, oldVersion, newVersion);
    }
}
