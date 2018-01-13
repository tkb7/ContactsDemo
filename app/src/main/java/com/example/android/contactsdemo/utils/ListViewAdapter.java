package com.example.android.contactsdemo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;

import java.util.List;

/**
 * Created by tkb on 12-Jan-18.
 *
 *  THIS ADAPTER TAKES CONTACT FROM THE LIST AND SETS ITS ARGUMENTS TO THE RELEVANT FIELD
 *
 *  I HAVE USED THREE INVISIBLE TEXTVIEWS TO STORE LOCATION AND UNIQUE ID OF THE CONTACT BECAUSE
 *  THESE DETAILS ARE NOT SUPPOSED TO BE SEEN BY USERS BUT THEY ARE NEEDED TO BE PASSED WHEN ITEM IS
 *  TAPPED FROM THE LISTVIEW
 *
 */

public class ListViewAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private List<Contact> contacts;

    public ListViewAdapter(Context context, List<Contact> contactList) {
        super(context, R.layout.single_contact, contactList);
        this.context = context;
        this.contacts = contactList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if(row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.single_contact, parent, false);
        }
        Contact contact = contacts.get(position);
        TextView name = row.findViewById(R.id.nameTextView);
        TextView phone = row.findViewById(R.id.phoneNumberTextView);
        TextView uniqueIdTextView = row.findViewById(R.id.uniqueIdTextView);
        TextView latitude = row.findViewById(R.id.latitudeTextView);
        TextView longitude = row.findViewById(R.id.longitudeTextView);
        name.setText(contact.name);
        phone.setText(contact.phone);
        latitude.setText(contact.latitude);
        longitude.setText(contact.longitude);
        uniqueIdTextView.setText(contact.uniqueContactId);
        return row;

    }
}
