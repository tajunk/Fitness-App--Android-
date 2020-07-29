package com.example.fitlab;

import androidx.annotation.NonNull;

public class Session
{
    int id;
    String listName;
    String createdDate;
    long elapsedTime;
    String caloriesCalc;
    int totalLaps;

    public Session() {}

    public Session(String listName) {
        this.listName = listName;
    }

    public Session(int id, long elapsedTime, int totalLaps)
    {
        this.id = id;
        this.elapsedTime = elapsedTime;
        this.totalLaps = totalLaps;
    }

    public Session(int id, String listName, long elapsedTime, String caloriesCalc, int totalLaps)
    {
        this.id = id;
        this.listName = listName;
        this.elapsedTime = elapsedTime;
        this.caloriesCalc = caloriesCalc;
        this.totalLaps = totalLaps;
    }

    public Session(String listName, long elapsedTime, String caloriesCalc, int totalLaps)
    {
        this.listName = listName;
        this.elapsedTime = elapsedTime;
        this.caloriesCalc = caloriesCalc;
        this.totalLaps = totalLaps;
    }

    public void setId(int id) { this.id = id; }
    public void setListName(String listName) {this.listName = listName; }
    public void setCreatedDate(String createdDate)
    {
        this.createdDate = createdDate;
    }
    public void setElapsedTime(long elapsedTime) { this.elapsedTime = elapsedTime; }
    public void setCaloriesCalc(String caloriesCalc) { this.caloriesCalc = caloriesCalc; }
    public void setTotalLaps(int totalLaps) { this.totalLaps = totalLaps; }

    public int getId()
    {
        return this.id;
    }
    public String getListName() { return this.listName; }
    public String getCreatedDate() { return this.createdDate; }
    public long getElapsedTime() { return this.elapsedTime; }
    public String getCaloriesCalc() { return this.caloriesCalc; }
    public int getTotalLaps() { return this.totalLaps; }

    @NonNull
    @Override
    public String toString() {
        return this.listName;
    }
}
