package com.kamalan.backend.phonebook.model;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.PhoneNumber;

/**
 * Created by Hesam Kamalan on 5/2/15
 */
public class ContactForm
{
    private String userName;
    private Email userEmailAddress;
    private PhoneNumber userPhoneNumber;

    // making default constructor private
    private ContactForm()
    {

    }

    public ContactForm(final String contactName, final Email emailAddress, final PhoneNumber phoneNumber)
    {
        this.userName = contactName;
        this.userEmailAddress = emailAddress;
        this.userPhoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public PhoneNumber getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public Email getUserEmailAddress() {
        return userEmailAddress;
    }
}
