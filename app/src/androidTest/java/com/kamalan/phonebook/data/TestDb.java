package com.kamalan.phonebook.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.kamalan.phonebook.data.PhonebookContract.ContactEntry;

/**
 * Created by admin on 6/28/15
 */
public class TestDb extends AndroidTestCase
{
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase()
    {
        this.mContext.deleteDatabase(PhonebookDbHelper.DATABASE_NAME);
    }

    // This function gets called before each test is executed to delete the database.
    // This makes sure that we always have a clean test.
    public void setUp()
    {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable
    {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(ContactEntry.TABLE_NAME);

        this.mContext.deleteDatabase(PhonebookDbHelper.DATABASE_NAME);
        SQLiteDatabase db = PhonebookDbHelper.getInstance(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the table(s) we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the table(s) have been created
        do
        {
            tableNameHashSet.remove(c.getString(0));
        }
        while (c.moveToNext());
        // if this fails, it means that your database doesn't contain *_Entry table(s)
        assertTrue("Error: Your database was created without *_Entry table(s)", tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + ContactEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> contactColumnHashSet = new HashSet<>();
        contactColumnHashSet.add(ContactEntry._ID);
        contactColumnHashSet.add(ContactEntry.COLUMN_NAME);
        contactColumnHashSet.add(ContactEntry.COLUMN_EMAIL);
        contactColumnHashSet.add(ContactEntry.COLUMN_PHONE_NUMBER);

        int columnNameIndex = c.getColumnIndex("name");
        do
        {
            String columnName = c.getString(columnNameIndex);
            contactColumnHashSet.remove(columnName);
        }
        while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required *contact* entry columns", contactColumnHashSet.isEmpty());
        db.close();
    }

    public void testContactTable()
    {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        PhonebookDbHelper dbHelper = PhonebookDbHelper.getInstance(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        ContentValues testValues = new ContentValues();
        testValues.put(ContactEntry.COLUMN_NAME, "Hesam Kamalan");
        testValues.put(ContactEntry.COLUMN_EMAIL, "info@kamalan.com");
        testValues.put(ContactEntry.COLUMN_PHONE_NUMBER, "00123456789");

        // Third Step: Insert ContentValues into database and get a row ID back
        long rowId = db.insert(ContactEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue(rowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(ContactEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        String error = "Error: *Contact* Query Validation Failed.";
        Set<Map.Entry<String, Object>> valueSet = testValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet)
        {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, cursor.getString(idx));
        }

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
    }
}
