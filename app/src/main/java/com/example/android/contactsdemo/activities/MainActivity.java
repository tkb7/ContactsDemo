package com.example.android.contactsdemo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.UploadContact;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
*           THIS ACTIVITY DOES THE JOB OF TAKING NAME, PHONE NUMBER AND LOCATION FROM THE USER AND
*           STORING IT TO THE DATABASE
* */

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etNumber;
    Button saveButton;
    Button goToListButton;
    Button placePickerButton;

    Double latitude;
    Double longitude;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int PLACE_PICKER_REQUEST = 1;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String uid = getIntent().getStringExtra("uid");

//        Reference to the database in firebase
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        initializing necessary ui elements
        etName = findViewById(R.id.nameEditTextContactDetails);
        etNumber = findViewById(R.id.numberEditText);
        saveButton = findViewById(R.id.retireveButton);
        goToListButton = findViewById(R.id.buttonGoToListView);
        placePickerButton = findViewById(R.id.placePickerButton);

        /*
        *       WHEN THIS BUTTON IS TAPPED, WE EXTRACT EVERYTHING USER HAS WRITTEN ON NAME AND
        *       PHONE NUMBER FIELDS. ALSO, WE CONVERT DATA LATITUDE AND LONGITUDE EXTRACTED FROM
        *       GOOGLE PLACE PICKER API AND STORE IT TO THE DATABASE
        *       IF ANYTHING IS EMPTY FROM ABOVE ALL, WE SIMPLY ADD "" TO THE DATABASE
        *       <CAN BE PROGRAMMED TO GET ONLY VALID DATA>
        * */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();

                UploadContact contact;
                if(latitude != null && longitude != null) {
                    contact = new UploadContact(name, number, latitude.toString(), longitude.toString());
                } else {
                    contact = new UploadContact(name, number, null, null);
                }
                databaseReference.child("users").child(uid).child("contacts").push().setValue(contact);
                Toast.makeText(getBaseContext(), "Contact Saved!", Toast.LENGTH_SHORT).show();
//                After uploading data, we redirect user to the list of saved contacts
                Intent intent = new Intent(getBaseContext(), ListOfContacts.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });


//      This method simply redirects user to the list of saved contacts
        goToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getBaseContext();
                Intent intent = new Intent(getBaseContext(), ListOfContacts.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

//      This button is pressed to launch place picker api activity.
//      The user can select any place from the mapView and we can retrieve its lat and long
//      to save it in the database
        placePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(MainActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });



    }
//  This method is invoked when placepicker activity completes its job.
//  We can retrieve selected place using PlacePicker.getPlace method.
//  From place we can retrieve latitude and longitude of the selected place
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Place place =   PlacePicker.getPlace(data,this);
        Double latitude = place.getLatLng().latitude;
        Double longitude = place.getLatLng().longitude;
        this.latitude = latitude;
        this.longitude = longitude;
        Toast.makeText(getBaseContext(), "Place selected!", Toast.LENGTH_SHORT).show();
        saveButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//  Method to let user sign out from the app
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