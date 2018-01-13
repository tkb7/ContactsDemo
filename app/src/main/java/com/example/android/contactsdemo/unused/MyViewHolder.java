package com.example.android.contactsdemo.unused;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;
import com.example.android.contactsdemo.unused.CustomAdapter;

/**
 * Created by tkb on 11-Jan-18.
 *
 * THE CLASSES IN THIS PACKAGE WERE BUILT IN ORDER TO CREATE A RECYCLER VIEW BUT FEW FUNCTIONALITY WERE
 * MISSING SO I USED LISTVIEW INSTEAD.
 *
 * THIS CLASSES ARE KEPT SO THAT RECYCLERVIW CAN BE IMPLEMENTED IN FUTURE
 *
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView phoneNumber;

    public MyViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameTextView);
        phoneNumber = itemView.findViewById(R.id.phoneNumberTextView);

    }

    public void bind(final Contact contact, final CustomAdapter.MyOnClickListener myOnClickListener) {
        name.setText(contact.name);
        phoneNumber.setText(contact.phone);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myOnClickListener.myOnItemClick();
                Log.i("OnClick", "tapped");
            }
        });
    }
}
