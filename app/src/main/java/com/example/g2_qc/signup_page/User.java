package com.example.g2_qc.signup_page;


public class User {

    // instance variables
    public String firstName, lastname, emailAddress, agePerson, location = "", experience;
    public String jobName, jobCategory, jobDescription, jobPayment, timePosted, preferredExperience;

    // default constructor
    public  User(){}

    /**
     * assigns the values to the variables.
     * @param firstName user's first name
     * @param lastname user's last name
     * @param emailAddress user's email address
     * @param agePerson user's age
     * @param experience user's experience
     */
    public User(String firstName, String lastname, String emailAddress, String agePerson, String experience){
        this.firstName = firstName;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.agePerson = agePerson;
        this.experience = experience;
    }
}
