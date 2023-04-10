package com.example.g2_qc.submitNewJob;

import com.example.g2_qc.location.LocationDetails;

public class Post {
    public String jobName, jobDescription, jobPayment, image, jobCategory, timePosted;

    LocationDetails jobLocation;

    public  Post(){}

    /**
     * assigns the values to the variables.
     * @param jobName name or the title of the job
     * @param jobDescription description of the job
     * @param jobPayment the payment of the job
     * @param image a reference of the job image
     * @param jobCategory the category of the job
     * @param timePosted the time when the job is posted
     * @param jobLocation of the job
     */
    public Post(String jobName, String jobDescription, String jobPayment, String image,
                String jobCategory, String timePosted, LocationDetails jobLocation) {
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobPayment = jobPayment;
        this.image = image;
        this.jobCategory = jobCategory;
        this.timePosted = timePosted;
        this.jobLocation = jobLocation;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setJobPayment(String jobPayment) {
        this.jobPayment = jobPayment;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public void setLocation(LocationDetails jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getJobPayment() {
        return jobPayment;
    }

    public String getImage() {
        return image;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public LocationDetails getLocation() {
        return jobLocation;
    }
}
