package com.example.pnwalerts.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private List<Subscriptions> subscriptions = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Subscriptions> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Subscriptions subscriptions) {
        this.subscriptions.add(subscriptions);
    }
}