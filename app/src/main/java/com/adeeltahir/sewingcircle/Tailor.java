package com.adeeltahir.sewingcircle;

import android.widget.ImageView;

public class Tailor extends User{
    private double Cost;
    private String Speciality;
    private ImageView[] SampleImages;
    private double Ratings;

    public Tailor(String name, String address, String contactInfo, String email, String password, String category, double payment, double cost, String speciality,  double ratings) {
        super(name, address, contactInfo, email, password, category, payment);
        Cost = cost;
        Speciality = speciality;
        Ratings = ratings;
    }

    public double getCost() {
        return Cost;
    }

    public void setCost(double cost) {
        Cost = cost;
    }

    public String getSpeciality() {
        return Speciality;
    }

    public void setSpeciality(String speciality) {
        Speciality = speciality;
    }

    public ImageView[] getSampleImages() {
        return SampleImages;
    }

    public void setSampleImages(ImageView[] sampleImages) {
        SampleImages = sampleImages;
    }

    public Tailor() {
    }

    public double getRatings() {
        return Ratings;
    }

    public void setRatings(float ratings) {
        Ratings = ratings;
    }
}
