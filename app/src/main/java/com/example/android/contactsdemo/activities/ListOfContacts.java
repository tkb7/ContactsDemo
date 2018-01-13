package com.example.android.contactsdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.contactsdemo.utils.ListViewAdapter;
import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
*       The main task of this activity is to download content from firebase database and display it on a listview.
*       Listview has an onItemClickListener that invokes ContactDetails activity, where you can see location of user,
*       or delete or edit the contact
*
* */

public class ListOfContacts extends AppCompatActivity {

    ListView listView;

    DatabaseReference reference;    //reference to firebase database
    List<Contact> contactList;      //data that is retrieved will be saved in this list
    ListViewAdapter adapter;        //adapter for listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_contacts);
/*
* uid is the important entity in the database. No user can access contacts without uid.
* All the contacts of any one users are stored witiin uid in database
* Uid is needed to make change in the database.
* */
        final String uid = getIntent().getStringExtra("uid");
        listView = findViewById(R.id.list_view);
        contactList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        /*
        * THE FOLLOWING METHOD RETRIEVES ALL THE CONTACTS OF USER. IT IS RETRIEVED IN HASHMAP FORM.
        * BUT TO STORE KEY AND DATA IN ONE CLASS ONLY, I HAVE CREATED ANOTHER CLASS THAT HAS ADDITIONAL
        * STRING ENTITY CALLED uniqueContactId.
        * I STORE ALL THE DATA IN THIS ANOTHER CLASS
        * */
        reference.child("users").child(uid).child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();

                GenericTypeIndicator<Map<String, Contact>> t = new GenericTypeIndicator<Map<String, Contact>>() {};
                Map<String, Contact> map = dataSnapshot.getValue(t);

//                Technique to get all the values of hashmap into a list:
//                Collection<Contact> collection = map.values();
//                contactList = new ArrayList<>(collection);

                if(map != null) {
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        Contact contact = (Contact) pair.getValue();
                        String key = (String) pair.getKey();
                        contact.uniqueContactId = key;
                        contactList.add(contact);
                        it.remove(); // avoids a ConcurrentModificationException
                    }

//                    PASSING LIST TO THE ADAPTER TO POPULATE LIST ON THE SCREEN
                    adapter = new ListViewAdapter(getBaseContext(), contactList);
                    listView.setAdapter(adapter);
                } else {
//                    INFORM USER IF THE LIST IS EMPTY
                    Toast.makeText(getBaseContext(), "Your Contact List is empty!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("ListActivity", "DatabaseError" + databaseError.getMessage());
            }
        });

//        if(contactList.size() != 0) {
//            Toast.makeText(getBaseContext(), "data empty", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getBaseContext(), "data NOT empty", Toast.LENGTH_SHORT).show();
//        }

        /*
        *       IN THIS CODE SNIPPET, I HAVE RETRIEVED THREE ADDITIONAL STRING THAT ARE NOT BEING SHOWN
        *       BUT I HAVE KEPT THEM SO THAT I CAN SEND THEM TO THE NEXT ACTIVITY WHEN CONTACT IS TAPPED.
        *       THIS STRINGS ARE latitude, longitude and uniqueId of the user
        * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView name = view.findViewById(R.id.nameTextView);
                TextView phone = view.findViewById(R.id.phoneNumberTextView);
                TextView uniqueId = view.findViewById(R.id.uniqueIdTextView);
                TextView latitudeView = view.findViewById(R.id.latitudeTextView);
                TextView longitudeView = view.findViewById(R.id.longitudeTextView);
                Intent intent = new Intent(getBaseContext(), ContactDetailsActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("phone", phone.getText().toString());
                intent.putExtra("uniqueId", uniqueId.getText().toString());
                intent.putExtra("latitude", latitudeView.getText().toString());
                intent.putExtra("longitude", longitudeView.getText().toString());
                intent.putExtra("uid", uid);
                startActivity(intent);
//                Toast.makeText(getBaseContext(), "tapped " + "name: " + name.getText().toString() + " phone: " + phone.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//  Function for sign out
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getBaseContext(), SignInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
