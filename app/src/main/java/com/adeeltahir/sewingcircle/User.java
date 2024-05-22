package com.adeeltahir.sewingcircle;

public abstract class User implements IUser{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return ContactInfo;
    }

    public void setContactInfo(String contactInfo) {
        ContactInfo = contactInfo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    private String name,address,ContactInfo,Email,Password,Category;

    public User() {
    }

    private double Payment;
    public User(String name, String address, String contactInfo, String email, String password, String category, double payment) {
        this.name = name;
        this.address = address;
        ContactInfo = contactInfo;
        Email = email;
        Password = password;
        Category = category;
        Payment = payment;
    }



    public double getPayment() {
        return Payment;
    }

    public void setPayment(double payment) {
        Payment = payment;
    }


}
