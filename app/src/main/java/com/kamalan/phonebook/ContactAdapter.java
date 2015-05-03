package com.kamalan.phonebook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;

import java.util.List;

/**
 * Created by Hesam Kamalan on 5/3/15
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
{
    public interface OnCardViewClickedListener
    {
        void onContactCallClicked(String phoneNumber);
        void onContactSMSClicked(String phoneNumber);
        void onContactEmailClicked(String email);
        void onContactEditClicked(Contact contact);
    }
    
    private List<Contact> mContactList;
    private OnCardViewClickedListener mListener;

    public ContactAdapter(List<Contact> mContactList, OnCardViewClickedListener listener)
    {
        this.mContactList = mContactList;
        this.mListener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_contact, viewGroup, false);

        return new ContactViewHolder(itemView, i);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i)
    {
        Contact contact = mContactList.get(i);
        contactViewHolder.tvContactName.setText(contact.getCName());
        contactViewHolder.tvContactDetails.setText(contact.getCPhoneNumber() + ", " + contact.getCEmail());
    }

    @Override
    public int getItemCount()
    {
        return this.mContactList.size();
    }

    public final class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        protected TextView tvContactName;
        protected TextView tvContactDetails;
        protected Button btnCall;
        protected Button btnSMS;
        protected Button btnEmail;
        protected Button btnEdit;

        public ContactViewHolder(View view, int position)
        {
            super(view);

            this.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            this.tvContactDetails = (TextView) view.findViewById(R.id.tvContactDetails);

            this.btnCall = (Button) view.findViewById(R.id.btnContactCall);
            this.btnCall.setOnClickListener(this);
            this.btnCall.setTag(position);

            this.btnSMS = (Button) view.findViewById(R.id.btnContactSMS);
            this.btnSMS.setOnClickListener(this);
            this.btnSMS.setTag(position);

            this.btnEmail = (Button) view.findViewById(R.id.btnContactEmail);
            this.btnEmail.setOnClickListener(this);
            this.btnEmail.setTag(position);

            this.btnEdit = (Button) view.findViewById(R.id.btnContactEdit);
            this.btnEdit.setOnClickListener(this);
            this.btnEdit.setTag(position);
        }

        @Override
        public void onClick(View v)
        {
            if (ContactAdapter.this.mListener == null)
                return;

            int position = (int) v.getTag();
            Contact contact = ContactAdapter.this.mContactList.get(position);

            switch (v.getId())
            {
                case R.id.btnContactCall:
                    ContactAdapter.this.mListener.onContactCallClicked(contact.getCPhoneNumber().getNumber());
                    break;
                case R.id.btnContactSMS:
                    ContactAdapter.this.mListener.onContactSMSClicked(contact.getCPhoneNumber().getNumber());
                    break;
                case R.id.btnContactEmail:
                    ContactAdapter.this.mListener.onContactEmailClicked(contact.getCEmail().getEmail());
                    break;
                case R.id.btnContactEdit:
                    ContactAdapter.this.mListener.onContactEditClicked(contact);
                    break;

            }
        }
    }
}
