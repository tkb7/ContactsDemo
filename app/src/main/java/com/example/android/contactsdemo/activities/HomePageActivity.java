package com.example.android.contactsdemo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contactsdemo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HomePageActivity extends AppCompatActivity {

    TextView userNameTextView;
    Button goToContactList;
    Button goToAddContact;
    String userName;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        Retrieving username and unique firebase id of user from sign in activity
        userName = getIntent().getStringExtra("userName");
        uid = getIntent().getStringExtra("uid");



        userNameTextView = findViewById(R.id.userNameTextView);
        goToAddContact = findViewById(R.id.buttonGoToAddContact);
        goToContactList = findViewById(R.id.buttonGoToContactList);

        userNameTextView.setText(userName);

        /*
        * Below two buttons launches two different activities.
        * one for saving new users and another to view saved contacts
        * We pass necessary details to the activity while launching
        * */


        goToAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        goToContactList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ListOfContacts.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }

//    Menu has one option to sign out current user.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getBaseContext(), SignInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
