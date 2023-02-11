package com.example.blackbook;

import java.io.Serializable;

public class StreetAddress implements Serializable {

    private String address;
    private String type;

    public StreetAddress(String address, String type){
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}