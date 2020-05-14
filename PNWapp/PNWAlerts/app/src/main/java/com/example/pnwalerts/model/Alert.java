package com.example.pnwalerts.model;

public class Alert {

    private String header;
    private String text;
    private String alertId;
    private String category;
    private String location;
    private Date date;

    public java.util.Date getDate() {
        return new java.util.Date(date.get_seconds() - date.get_nanoseconds());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String newsText) {
        this.text = newsText;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String attributes) {
        this.location = attributes;
    }
}
