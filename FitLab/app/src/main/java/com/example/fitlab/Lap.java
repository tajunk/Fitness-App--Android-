package com.example.fitlab;

public class Lap
{
    int id;
    String listName;
    int lapNumber;
    long lapTime;

    public Lap() {}

    public Lap(String listName, int lapNumber, long lapTime)
    {
        this.listName = listName;
        this.lapNumber = lapNumber;
        this.lapTime = lapTime;
    }

    public void setId(int id) { this.id = id; }
    public void setListName(String listName) {this.listName = listName; }
    public void setLapNumber(int lapNumber) { this.lapNumber = lapNumber; }
    public void setLapTime(long lapTime) { this.lapTime = lapTime; }

    public int getId()
    {
        return this.id;
    }
    public String getListName() { return this.listName; }
    public int getLapNumber() { return this.lapNumber; }
    public long getLapTime() { return  this.lapTime; }
}
