package com.kamalan.phonebook;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appspot.my_phonebook_123.phonebookAPI.model.Contact;
import com.appspot.my_phonebook_123.phonebookAPI.model.Email;
import com.appspot.my_phonebook_123.phonebookAPI.model.PhoneNumber;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView requires a layout manager. This component positions item views inside
        // the row and determines when it is time to recycle the views.
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvCardList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        Button btnContactCall = (Button) findViewById(R.id.btnContactCall);
        Button btnContactSMS = (Button) findViewById(R.id.btnContactSMS);
        Button btnContactEmail = (Button) findViewById(R.id.btnContactEmail);
        Button btnContactEdit = (Button) findViewById(R.id.btnContactEdit);

        List<Contact> contactList = new ArrayList<>(20);
        for (int i=0; i<20; i++)
        {
            contactList.add(getDemoContact());
        }

        ContactAdapter adapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(adapter);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {

    }

    private Contact getDemoContact()
    {
        Contact contact = new Contact();
        contact.setCName("Hesam");

        Email email = new Email();
        email.setEmail("Hesam.Kamalan@gmail.com");
        contact.setCEmail(email);

        PhoneNumber number = new PhoneNumber();
        number.setNumber("123456");
        contact.setCPhoneNumber(number);

        return contact;
    }
}
