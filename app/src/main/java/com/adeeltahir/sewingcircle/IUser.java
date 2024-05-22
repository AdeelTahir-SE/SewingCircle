package com.adeeltahir.sewingcircle;

public interface IUser {
    String getName();
    void setName(String name);

    String getAddress();
    void setAddress(String address);

    String getContactInfo();
    void setContactInfo(String contactInfo);

    String getEmail();
    void setEmail(String email);

    String getPassword();
    void setPassword(String password);

    String getCategory();
    void setCategory(String category);

    double getPayment();
    void setPayment(double payment);
}
