package com.example.android.contactsdemo.datamodels;

/**
 * Created by tkb on 12-Jan-18.
 *
 *      THIS MODEL OF STUDENT CONTACT IS USED TO UPLOAD THE USER DETAILS TO THE DATABASE
 */

public class UploadContact {

    public String name;
    public String phone;
    public String latitude;
    public String longitude;

    public UploadContact() {
    }

    public UploadContact(String name, String phone, String latitude, String longitude) {
        this.name = name;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
