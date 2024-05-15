package com.adeeltahir.sewingcircle;

public class PreviousCustomer {
    private String name;
    private String address;

    public PreviousCustomer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}