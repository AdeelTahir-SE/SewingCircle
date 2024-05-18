package com.adeeltahir.sewingcircle;

public class PreviousCustomer {
    private String id;
    private String name;
    private String address;
    private String email;
    private String contactInfo;

    // Default constructor required for calls to DataSnapshot.getValue(PreviousCustomer.class)
    public PreviousCustomer() {
    }

    public PreviousCustomer(String id, String name, String address, String email, String contactInfo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contactInfo = contactInfo;
    }
    public PreviousCustomer(String id, Customer customer) {
        this.id = id;
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.email = customer.getEmail();
        this.contactInfo = customer.getContactInfo();
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
