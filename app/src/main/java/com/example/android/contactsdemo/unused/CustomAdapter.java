package com.example.android.contactsdemo.unused;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contactsdemo.R;
import com.example.android.contactsdemo.datamodels.Contact;

import java.util.List;

/**
 * Created by tkb on 11-Jan-18.
 *
 * THE CLASSES IN THIS PACKAGE WERE BUILT IN ORDER TO CREATE A RECYCLER VIEW BUT FEW FUNCTIONALITY WERE
 * MISSING SO I USED LISTVIEW INSTEAD.
 *
 * THIS CLASSES ARE KEPT SO THAT RECYCLERVIW CAN BE IMPLEMENTED IN FUTURE
 *
 */

public class CustomAdapter extends RecyclerView.Adapter<MyViewHolder> {


    public interface MyOnClickListener {
        void myOnItemClick();
    }

    private List<Contact> contacts;
    private MyOnClickListener myOnClickListener;

    public CustomAdapter(List<Contact> contacts, MyOnClickListener myOnClickListener) {
        this.contacts = contacts;
        this.myOnClickListener = myOnClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_contact,parent,false);

        MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

//        Contact c = contacts.get(position);
//        holder.name.setText(c.name);
//        holder.phoneNumber.setText(c.phone);
        holder.bind(contacts.get(position), myOnClickListener);

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
