package com.adeeltahir.sewingcircle;

public class Customer extends User {
    private double[] Dimensions;
    private String Desired_Fashion;
    private Double RatingGiven;
    private String imageUrl;
    public Customer() {

    }
    public Customer(String name, String address, String contactInfo, String email, String password, String category, double payment, String desired_Fashion, Double ratingGiven,String imageUrl) {
        super(name, address, contactInfo, email, password, category, payment);
        Desired_Fashion = desired_Fashion;
        RatingGiven = ratingGiven;
        this.imageUrl = imageUrl;
    }

    public double[] getDimensions() {
        return Dimensions;
    }

    public void setDimensions(double[] dimensions) {
        Dimensions = dimensions;
    }

    public String getDesired_Fashion() {
        return Desired_Fashion;
    }

    public void setDesired_Fashion(String desired_Fashion) {
        Desired_Fashion = desired_Fashion;
    }

    public Double getRatingGiven() {
        return RatingGiven;
    }

    public void setRatingGiven(Double ratingGiven) {
        RatingGiven = ratingGiven;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
