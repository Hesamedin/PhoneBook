package com.kamalan.phonebook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;

import java.util.List;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
{
    private List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList)
    {
        this.contactList = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_contact, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i)
    {
        Contact contact = contactList.get(i);
        contactViewHolder.tvContactName.setText(contact.getCName());
        contactViewHolder.tvContactDetails.setText(contact.getCPhoneNumber() + ", " + contact.getCEmail());
    }

    @Override
    public int getItemCount()
    {
        return this.contactList.size();
    }

    public static final class ContactViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tvContactName;
        protected TextView tvContactDetails;

        public ContactViewHolder(View view)
        {
            super(view);

            this.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            this.tvContactDetails = (TextView) view.findViewById(R.id.tvContactDetails);
        }
    }
}
