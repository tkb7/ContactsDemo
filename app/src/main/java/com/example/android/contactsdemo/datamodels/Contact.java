package com.example.android.contactsdemo.datamodels;

/**
 * Created by tkb on 10-Jan-18.
 *
 * THIS IS THE CLASS THAT STORES CONTACT DETAILS ALONG WITH UNIQUE ID OF THE USER
 *
 * THIS CLASS IS USED TO HANDLE OPERATIONS OF THE USER DETAILS THAT ARE RETRIEVED FROM DATABASE
 *
 */

public class Contact {

    public String name;
    public String phone;
    public String latitude;
    public String longitude;
    public String uniqueContactId;

    public Contact() {
    }

    public Contact(String name, String phone, String latitude, String longitude) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
