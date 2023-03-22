package com.example.g2_qc.user_profile.personal_information;

import com.example.g2_qc.signup_page.User;

public class userDetails extends User {

    public  userDetails(){}

    public userDetails(String firstName, String lastname, String emailAddress, String agePerson){
        this.firstName = firstName;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.agePerson = agePerson;
    }
}
