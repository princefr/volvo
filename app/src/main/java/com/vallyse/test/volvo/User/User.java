package com.vallyse.test.volvo.User;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by princejackes on 04/02/2018.
 */

public  class User {
    public String firstname;
    public String lastname;
    public String photoUrl;
    public String email;
    public String displayName;
    public String uid;
    public String prodviderId;
    public String phoneNumber;
    public Boolean isVerified;
    public String stripeCustomerId;
    public String stripeAcountId;
    public String adresse;
    public String nationality;
    public String gender;
    public String locale;
    public String timezone;
    public String age_range;



    public User() {

    }


    public User(String firstname, String lastname, String displayName, String uid, String email, String phoneNumber, String photoUrl, String adresse,  String nationality, Boolean isVerified, String stripeCustomerId, String stripeAcountId, String gender, String locale, String timezone, String age_range) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.displayName = displayName;
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.adresse = adresse;
        this.nationality = nationality;
        this.isVerified = isVerified;
        this.stripeCustomerId = stripeCustomerId;
        this.stripeAcountId = stripeAcountId;
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.locale = locale;
        this.timezone = timezone;
        this.age_range = age_range;

    }






}
