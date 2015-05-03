package com.kamalan.phonebook.endpoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appspot.my_phonebook_123.phonebookAPI.PhonebookAPI;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.kamalan.phonebook.MainActivity;
import com.kamalan.phonebook.R;
import com.kamalan.phonebook.utility.Endpoint;
import com.kamalan.phonebook.utility.Storage;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class DeleteContactEndpoint extends AsyncTask<Void, Void, Void>
{
    private static final String TAG = DeleteContactEndpoint.class.getSimpleName();

    private PhonebookAPI mPhonebookAPI;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private long mContactId;

    WeakReference<MainActivity> mWeakReference;

    public DeleteContactEndpoint(Context context, long contactId)
    {
        this.mContext = context;
        this.mWeakReference = new WeakReference<>((MainActivity) context);
        this.mContactId = contactId;
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.dialog_loading_title), mContext.getString(R.string.dialog_loading_wait), true);
    }

    @Override
    protected Void doInBackground(Void... params)
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
            return mPhonebookAPI.deleteContact(mContactId).execute();
        }
        catch (IOException | IllegalArgumentException e)
        {
            Log.e(TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void v)
    {
        if (mProgressDialog != null)
        {
            mProgressDialog.dismiss();
        }

        MainActivity activity = mWeakReference.get();
        if (activity != null)
        {
            activity.getContacts();
        }
    }
}
