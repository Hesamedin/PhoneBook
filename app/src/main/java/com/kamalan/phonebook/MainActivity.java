package com.kamalan.phonebook;

import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;
import com.appspot.my_phonebook_123.phonebookAPI.model.ContactForm;
import com.appspot.my_phonebook_123.phonebookAPI.model.Email;
import com.appspot.my_phonebook_123.phonebookAPI.model.PhoneNumber;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.kamalan.phonebook.endpoint.ContactListEndpoint;
import com.kamalan.phonebook.endpoint.CreateContactEndpoint;
import com.kamalan.phonebook.utility.Storage;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements
        ContactAdapter.OnCardViewClickedListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ACCOUNT_PICKER = 101;

    private ContactAdapter mContactAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView requires a layout manager. This component positions item views inside
        // the row and determines when it is time to recycle the views.
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvCardList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llm);

        this.mContactAdapter = new ContactAdapter(new ArrayList<Contact>(), this);
        this.mRecyclerView.setAdapter(this.mContactAdapter);

        // Get Google account credentials
        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(this,
                "server:my-phonebook-123:1-web-app.apps.googleusercontent.com");
        String accountName = Storage.getAccountName(this);
        credential.setSelectedAccountName(accountName);
        if (credential.getSelectedAccountName() != null)
        {
            Log.d(TAG, "Logged in with: " + credential.getSelectedAccountName());

            // Already signed in,
            // get list of contacts
            getContacts();
        }
        else
        {
            // Not signed in, show login window or request an account.
            startActivityForResult(credential.newChooseAccountIntent(), MainActivity.REQUEST_ACCOUNT_PICKER);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // add contact dialog
        if (id == R.id.action_add_contact)
        {
            ContactForm form = new ContactForm();
            form.setUserName("Name 1");
            form.setUserEmailAddress(new Email().setEmail("name1@example.com"));
            form.setUserPhoneNumber(new PhoneNumber().setNumber("123456"));

            new CreateContactEndpoint(this, form).execute();
            return true;
        }

        // Get contact list
        if (id == R.id.action_get_contacts)
        {
            getContacts();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactCallClicked(String phoneNumber)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onContactSMSClicked(String phoneNumber)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("smsto:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onContactEmailClicked(String email)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        startActivity(intent);
    }

    @Override
    public void onContactEditClicked(Contact contact)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case MainActivity.REQUEST_ACCOUNT_PICKER:
                if (data != null && data.getExtras() != null)
                {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null)
                    {
                        // User is authorized.
                        Storage.setAccountName(MainActivity.this, accountName);
                        Log.d(TAG, "Acc name: " + accountName);

                        // get list of contacts
                        getContacts();
                    }
                }
                break;
        }
    }

    /**
     *  Load list of Contacts from App Engine
     */
    private void getContacts()
    {
        new ContactListEndpoint(this).execute();
    }

    /**
     * Calls by ContactListEndpoint in order to display contact list result.
     *
     * @param contactList
     */
    public void onContactListReceived(final List<Contact> contactList)
    {
        TextView tvResult = (TextView) findViewById(R.id.tvNoData);
        tvResult.setVisibility(View.GONE);

        if (contactList == null || contactList.size() == 0)
        {
            tvResult.setVisibility(View.VISIBLE);
            return;
        }

        this.mContactAdapter.setContactList(contactList);
    }

    /**
     * Calls by CreateContactEndpoint in order to display contact result.
     *
     * @param contact
     */
    public void onContactCreated(Contact contact)
    {
        if (contact != null)
        {
            Log.d(TAG, contact.toString());
            getContacts();
        }
    }
}
