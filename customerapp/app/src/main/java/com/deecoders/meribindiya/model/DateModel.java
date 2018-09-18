package com.deecoders.meribindiya.model;

public class DateModel {
    String completeDate;
    String date;
    String day;
    boolean selected;

    public DateModel(String completeDate, String date, String day, boolean selected) {
        this.completeDate = completeDate;
        this.date = date;
        this.day = day;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
