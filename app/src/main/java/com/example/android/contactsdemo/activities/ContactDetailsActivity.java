package com.example.android.contactsdemo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;
import com.example.android.contactsdemo.datamodels.UploadContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
*      IN THIS ACTIVITY USER CAN EDIT OR DELETE A SINGLE CONTACT. ALSO, USER CAN SEE SAVED
*      LOCATION IN THE MAPVIEW.
*      FOR TIME BEING THE FUNCTIONALITY OF EDITING LOCATION IS NOT IMPLEMENTED. BUT IT CAN BE
*      IMPLEMENTED WITH FEW LINES OF CODE.
*
*      WE RETRIEVE ALL THE NECESSARY INFORMATION OF THE CONTACTS FROM INTENT COMING FROM PREVIOUS CLASS
* */

public class ContactDetailsActivity extends AppCompatActivity {

    private String uid;
    private Contact currentContact;

    private Button editButton;
    private Button deleteButton;
    private Button saveButton;
    private Button showLocationButton;

    private EditText nameEditText;
    private EditText numberEditText;

    private TextView nameTextView;
    private TextView numberTextView;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentContact = new Contact();
//      Retrieving all the necessary information
        setContentView(R.layout.activity_contact_details);
        currentContact.name = getIntent().getStringExtra("name");
        currentContact.phone = getIntent().getStringExtra("phone");
        currentContact.uniqueContactId = getIntent().getStringExtra("uniqueId");
        currentContact.latitude = getIntent().getStringExtra("latitude");
        currentContact.longitude = getIntent().getStringExtra("longitude");
        uid = getIntent().getStringExtra("uid");

//      Initializing ui elements
        editButton = findViewById(R.id.editButtonContactDetails);
        deleteButton = findViewById(R.id.deleteButtonContactDetails);
        saveButton = findViewById(R.id.saveButtonContactDetails);
        showLocationButton = findViewById(R.id.showLocationButton);

        nameEditText = findViewById(R.id.nameEditTextContactDetails);
        numberEditText = findViewById(R.id.phoneNumberEditTextContactDetails);

        nameTextView = findViewById(R.id.nameTextViewContactDetails);
        numberTextView = findViewById(R.id.numberTextViewContactDetails);

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        Toast.makeText(getBaseContext(), "lat: " + currentContact.latitude + " long :" + currentContact.longitude, Toast.LENGTH_SHORT).show();

        nameTextView.setText(currentContact.name);
        numberTextView.setText(currentContact.phone);

/*
*       WHEN EDIT BUTTON IS TAPPED, THE TEXTVIEW IS CONVERTED TO EDITTEXT AND USER CAN EDIT THE NAME
*       AND PHONE NUMBER. THE EDIT BUTTON IS MADE HIDDEN AND SAVE BUTTON APPEARS WHERE USER CAN SAVE THE
*       CHANGES THAT ARE MADE
* */
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameTextView.getText().toString();
                String number = numberTextView.getText().toString();
                nameTextView.setVisibility(View.GONE);
                numberTextView.setVisibility(View.GONE);

                nameEditText.setVisibility(View.VISIBLE);
                numberEditText.setVisibility(View.VISIBLE);

                nameEditText.setText(name);
                numberEditText.setText(number);

                editButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.VISIBLE);

            }
        });
/*
*       THIS BUTTON COMES INTO VISION ONLY AFTER USER TAPS EDIT BUTTON
*       WHEN SAVE BUTTON IS TAPPED, THE TEXTS WRITTEN IN THE EDITTETEXTS ARE RETRIEVED AND THEY ARE
*       SAVED TO DATABASE
*
* */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String number = numberEditText.getText().toString();
                UploadContact contact = new UploadContact(name, number,currentContact.latitude,currentContact.longitude);
                databaseReference.child("users").child(uid).child("contacts").child(currentContact.uniqueContactId).setValue(contact);


                nameEditText.setVisibility(View.INVISIBLE);
                numberEditText.setVisibility(View.INVISIBLE);

                saveButton.setVisibility(View.INVISIBLE);
                editButton.setVisibility(View.VISIBLE);

                nameTextView.setVisibility(View.VISIBLE);
                numberTextView.setVisibility(View.VISIBLE);

                nameTextView.setText(name);
                numberTextView.setText(number);

                Toast.makeText(getBaseContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), ListOfContacts.class);
                intent.putExtra("uid", uid);
                startActivity(intent);

            }
        });

        /*
        * WHEN THIS BUTTON IS TAPPED, THE CONTACT THAT IS IN USE IS SIMPLY DELETED FROM THE DATABASE
        * */

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("users").child(uid).child("contacts").child(currentContact.uniqueContactId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(getBaseContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), ListOfContacts.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                });
            }
        });

        /*
        *   THIS METHOD STARTS THE MAP ACTIVITY PASSING THE LATITUDE AND LONGITUDE RECEIVED FOR ANY CONTACT
        * */

        showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((currentContact.latitude != "" && currentContact.latitude != null) && (currentContact.longitude != "" && currentContact.latitude != null)) {
                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    intent.putExtra("latitude", currentContact.latitude);
                    intent.putExtra("longitude", currentContact.longitude);

                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Contact has not any location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//  Method to let user sign out
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
