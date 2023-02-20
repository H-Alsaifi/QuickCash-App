package com.example.g2_qc.user_profile;

public class userDetails {
    public String age, firstName, lastName, email;

    public userDetails() {}

    public userDetails(String user_age, String first_Name, String last_Name, String email_address) {
        this.age = user_age;
        this.firstName = first_Name;
        this.lastName = last_Name;
        this.email = email_address;
    }
}
