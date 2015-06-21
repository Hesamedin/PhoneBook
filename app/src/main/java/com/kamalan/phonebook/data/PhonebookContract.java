package com.kamalan.phonebook.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the weather database.
 * <p/>
 * Created by Hesam Kamalan on 6/21/15.
 */
public class PhonebookContract
{
    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website. A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.kamalan.phonebook";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.kamalan.phonebook/contact/ is a valid path for
    // looking at contact data. content://com.kamalan.phonebook/somethingelse/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "somethingelse".
    public static final String PATH_CONTACT = "contact";

    /**
     * Inner class that defines the contents of the Contact table
     */
    public static final class ContactEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        // Table name
        public static final String TABLE_NAME = "contact";

        // Contact table's column names
        public static final String COLUMN_NAME = "contact_name";
        public static final String COLUMN_EMAIL = "contact_email";
        public static final String COLUMN_PHONE_NUMBER = "contact_phone_number";

    }
}
