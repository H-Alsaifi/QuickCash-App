package com.example.g2_qc.user_profile.job_details;

public class jobDetails {

    public String jobName, jobCategory, jobDescription, jobPayment, timePosted;

    public  jobDetails(){}

    public jobDetails(String jobName, String jobCategory, String jobDescription, String jobPayment, String timePosted){
        this.jobName= jobName;
        this.jobCategory=jobCategory;
        this.jobDescription = jobDescription;
        this.jobPayment = jobPayment;
        this.timePosted = timePosted;
    }
}
