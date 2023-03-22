package com.example.g2_qc.signup_page;


public class User {

    public String firstName, lastname, emailAddress, agePerson, location = "";
    public String jobName, jobCategory, jobDescription, jobPayment, timePosted;


    public  User(){}

    /**
     * assigns the values to the variables.
     * @param firstName user's first name
     * @param lastname user's last name
     * @param emailAddress user's email address
     * @param agePerson user's age
     */
    public User(String firstName, String lastname, String emailAddress, String agePerson){
        this.firstName = firstName;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.agePerson = agePerson;
    }
}
