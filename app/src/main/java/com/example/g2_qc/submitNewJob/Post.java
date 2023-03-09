package com.example.g2_qc.submitNewJob;

public class Post {
    public String jobName, jobDescription, jobPayment, image, jobCategory;

    public  Post(){}

    /**
     * assigns the values to the variables.
     * @param jobName name or the title of the job
     * @param jobDescription description of the job
     * @param jobPayment the payment of the job
     * @param image a reference of the job image
     * @param jobCategory the category of the job
     */
    public Post(String jobName, String jobDescription, String jobPayment, String image, String jobCategory) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobPayment = jobPayment;
        this.image = image;
        this.jobCategory = jobCategory;
    }


    public Post(String jobName, String jobDescription, String jobPayment, String jobCategory) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobPayment = jobPayment;
        this.jobCategory = jobCategory;
    }
}
