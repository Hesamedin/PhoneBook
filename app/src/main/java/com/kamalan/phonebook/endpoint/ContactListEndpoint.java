package com.kamalan.phonebook.endpoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.my_phonebook_123.phonebookAPI.PhonebookAPI;
import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.kamalan.phonebook.MainActivity;
import com.kamalan.phonebook.R;
import com.kamalan.phonebook.utility.Endpoint;
import com.kamalan.phonebook.utility.Storage;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class ContactListEndpoint extends AsyncTask<Void, Void, List<Contact>>
{
    private static final String TAG = ContactListEndpoint.class.getSimpleName();

    private PhonebookAPI mPhonebookAPI;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    WeakReference<MainActivity> mWeakReference;

    public ContactListEndpoint(Context context)
    {
        this.mContext = context;
        this.mWeakReference = new WeakReference<>((MainActivity) context);
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.dialog_loading_title), mContext.getString(R.string.dialog_loading_wait), true);
    }

    @Override
    protected List<Contact> doInBackground(Void... params)
    {
        // To just do it once
        if (mPhonebookAPI == null)
        {
            GoogleAccountCredential credential = GoogleAccountCredential.usingAudience(mContext, Endpoint.AUDIENCE);
            credential.setSelectedAccountName(Storage.getAccountName(mContext));

            PhonebookAPI.Builder builder = new PhonebookAPI.Builder(Endpoint.HTTP_TRANSPORT, Endpoint.JSON_FACTORY, credential)
                    .setApplicationName(Endpoint.CLIENT_ID);
            mPhonebookAPI = builder.build();
        }

        try
        {
            return mPhonebookAPI.getContactsCreated().execute().getItems();
        }
        catch (IOException | IllegalArgumentException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Contact> contactList)
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }

        MainActivity activity = mWeakReference.get();
        if (activity != null)
        {
            activity.onContactListReceived(contactList);
        }
    }
}
