package com.example.android.contactsdemo.unused;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;
import com.example.android.contactsdemo.unused.CustomAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * THE CLASSES IN THIS PACKAGE WERE BUILT IN ORDER TO CREATE A RECYCLER VIEW BUT FEW FUNCTIONALITY WERE
 * MISSING SO I USED LISTVIEW INSTEAD.
 *
 * THIS CLASSES ARE KEPT SO THAT RECYCLERVIW CAN BE IMPLEMENTED IN FUTURE
 *
 */

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    DatabaseReference reference;
    List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        final String uid = getIntent().getStringExtra("uid");
        recyclerView = findViewById(R.id.recyclerView);
        contactList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(uid).child("contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactList.clear();
                for(DataSnapshot tempSnapShot: dataSnapshot.getChildren()) {
                    Contact contact = tempSnapShot.getValue(Contact.class);
                    Log.i("Data", "Name: " + contact.name + " Phone: " + contact.phone);
                    contactList.add(contact);
                }
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("ListActivity", "DatabaseError" + databaseError.getMessage());
            }
        });


        Log.i("If im here", "im here");

        if(contactList.size() != 0) {
            Toast.makeText(getBaseContext(), "data empty", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "data NOT empty", Toast.LENGTH_SHORT).show();
        }

        customAdapter = new CustomAdapter(contactList, new CustomAdapter.MyOnClickListener() {
            @Override
            public void myOnItemClick() {
                Toast.makeText(getBaseContext(), "tapped", Toast.LENGTH_SHORT).show();
                Log.i("onclick", "tapped");
            }
        });

        LinearLayoutManager l = new LinearLayoutManager(getBaseContext());
        l.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), l.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(l);

    }
}
