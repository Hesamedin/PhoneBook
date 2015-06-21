package com.kamalan.phonebook.data;

import android.test.AndroidTestCase;

import java.lang.System;

/**
 * Created by Hesam Kamalan on 6/21/15
 */
public class TestPhonebookContract extends AndroidTestCase
{
    public void testPath()
    {
        String path = "content://com.kamalan.phonebook/contact";
        assertEquals("Error in validation of content provider path", path,
                PhonebookContract.BASE_CONTENT_URI + "/" + PhonebookContract.PATH_CONTACT);

        System.out.println(PhonebookContract.BASE_CONTENT_URI + "/" + PhonebookContract.PATH_CONTACT);
    }
}
