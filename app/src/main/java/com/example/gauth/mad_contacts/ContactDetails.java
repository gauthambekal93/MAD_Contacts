package com.example.gauth.mad_contacts;

import android.provider.ContactsContract;

public class ContactDetails {
    String Name;
    String PhoneNo;
    String Email;
    String ImageId;
    public ContactDetails(String Name, String PhoneNo,String Email,String ImageId)
    {
        this.Name=Name;
        this.PhoneNo=PhoneNo;
        this.Email=Email;
        this.ImageId=ImageId;
    }
    ContactDetails()
    {

    }
}

class ContactDetails2
{    String Name;
    String PhoneNo;
    String Email;
    String ImageId;
    String Key;
    public ContactDetails2(String Name, String PhoneNo,String Email,String ImageId,String Key)
    {
        this.Name=Name;
        this.PhoneNo=PhoneNo;
        this.Email=Email;
        this.ImageId= ImageId;
        this.Key=Key;
    }
    ContactDetails2()
    {

    }
}