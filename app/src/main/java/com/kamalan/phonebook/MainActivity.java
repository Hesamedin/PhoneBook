package com.kamalan.phonebook;

import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;
import com.appspot.my_phonebook_123.phonebookAPI.model.ContactForm;
import com.appspot.my_phonebook_123.phonebookAPI.model.Email;
import com.appspot.my_phonebook_123.phonebookAPI.model.PhoneNumber;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.kamalan.phonebook.endpoint.ContactListEndpoint;
import com.kamalan.phonebook.endpoint.CreateContactEndpoint;
import com.kamalan.phonebook.endpoint.DeleteContactEndpoint;
import com.kamalan.phonebook.endpoint.UpdateContactEndpoint;
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
        GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(this, "server:my-phonebook-123:1-web-app.apps.googleusercontent.com");
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
            displayCreateContactDialog();
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
        displayEditContactDialog(contact);
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
     * Load list of Contacts from App Engine
     */
    public void getContacts()
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
    public void onContactCreated(final Contact contact)
    {
        if (contact != null)
        {
            Log.d(TAG, contact.toString());
            getContacts();
        }
    }

    /**
     * Opens a dialog, let the user to add contact detail and finally tries to create contact
     * by sending request to CreateContactEndpoint.
     */
    private void displayCreateContactDialog()
    {
        new MaterialDialog.Builder(this).title(R.string.dialog_title_create).customView(R.layout.dialog_contact, true).positiveText(R.string.dialog_btn_create).neutralText(R.string.dialog_btn_cancel).callback(new MaterialDialog.ButtonCallback()
        {
            @Override
            public void onPositive(MaterialDialog dialog)
            {
                super.onPositive(dialog);

                final ContactForm contactForm = getContactFromDialog(dialog);

                // Create contact
                if (contactForm != null)
                {
                    new CreateContactEndpoint(MainActivity.this, contactForm).execute();
                }
            }
        }).show();
    }

    private void displayEditContactDialog(final Contact contact)
    {
        MaterialDialog dialog = new MaterialDialog.Builder(this).title(R.string.dialog_title_edit).customView(R.layout.dialog_contact, true).positiveText(R.string.dialog_btn_update).neutralText(R.string.dialog_btn_cancel).negativeText(R.string.dialog_btn_delete).callback(new MaterialDialog.ButtonCallback()
        {
            @Override
            public void onPositive(MaterialDialog dialog)
            {
                super.onPositive(dialog);

                final ContactForm contactForm = getContactFromDialog(dialog);

                // Create contact
                if (contactForm != null)
                {
                    new UpdateContactEndpoint(MainActivity.this, contact.getId(), contactForm).execute();

                    // close dialog
                    dialog.dismiss();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog)
            {
                super.onNegative(dialog);

                // Delete contact
                new DeleteContactEndpoint(MainActivity.this, contact.getId()).execute();
            }
        }).build();

        View view = dialog.getCustomView();
        TextView tvContactName = (TextView) view.findViewById(R.id.etContactName);
        tvContactName.setText(contact.getCName());

        TextView tvContactEmail = (TextView) view.findViewById(R.id.etContactEmail);
        tvContactEmail.setText(contact.getCEmail().getEmail());

        TextView tvContactPhone = (TextView) view.findViewById(R.id.etContactPhone);
        tvContactPhone.setText(contact.getCPhoneNumber().getNumber());

        // display dialog
        dialog.show();
    }

    private ContactForm getContactFromDialog(MaterialDialog dialog)
    {
        if (dialog == null)
        {
            return null;
        }

        final ContactForm contactForm = new ContactForm();

        TextView tvContactName = (TextView) dialog.getCustomView().findViewById(R.id.etContactName);
        contactForm.setUserName(tvContactName.getText().toString().trim());

        TextView tvContactEmail = (TextView) dialog.getCustomView().findViewById(R.id.etContactEmail);
        Email email = new Email();
        email.setEmail(tvContactEmail.getText().toString().trim());
        contactForm.setUserEmailAddress(email);

        TextView tvContactPhone = (TextView) dialog.getCustomView().findViewById(R.id.etContactPhone);
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setNumber(tvContactPhone.getText().toString().trim());
        contactForm.setUserPhoneNumber(phoneNumber);

        // check fields, return null if contact info is not complete
        if (TextUtils.isEmpty(contactForm.getUserName()))
        {
            displayToastMessage(getResources().getString(R.string.toast_blank_contact));
            return null;
        }

        if (TextUtils.isEmpty(contactForm.getUserEmailAddress().getEmail()))
        {
            displayToastMessage(getResources().getString(R.string.toast_blank_email));
            return null;
        }

        if (TextUtils.isEmpty(contactForm.getUserPhoneNumber().getNumber()))
        {
            displayToastMessage(getResources().getString(R.string.toast_blank_phone));
            return null;
        }

        return contactForm;
    }

    private void displayToastMessage(final String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
