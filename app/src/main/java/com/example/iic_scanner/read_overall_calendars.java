package com.example.iic_scanner;

public class read_overall_calendars {
    private String id;
    private String Date;

    public read_overall_calendars() {
    }

    public read_overall_calendars(String id, String date) {
        this.id = id;
        Date = date;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
}
