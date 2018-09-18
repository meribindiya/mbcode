package com.deecoders.meribindiya.model;

public class TimeSlotModel {
    String time;
    boolean selected;

    public TimeSlotModel(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
