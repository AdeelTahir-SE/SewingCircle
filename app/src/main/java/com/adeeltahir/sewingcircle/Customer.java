package com.adeeltahir.sewingcircle;

public class Customer extends User {
    private double[] Dimensions;
    private String Desired_Fashion;
    private Double RatingGiven;

    public Customer() {
    }

    public Customer(String name, String address, String contactInfo, String email, String password, String category, double payment, String desired_Fashion, Double ratingGiven) {
        super(name, address, contactInfo, email, password, category, payment);
        Desired_Fashion = desired_Fashion;
        RatingGiven = ratingGiven;
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
}
