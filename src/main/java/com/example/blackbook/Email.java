package com.example.blackbook;

import java.io.Serializable;

public class Email implements Serializable {

    private String email;
    private String type;

    public Email(String email, String type){
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }
}