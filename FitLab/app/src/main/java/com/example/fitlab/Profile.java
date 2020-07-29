package com.example.fitlab;

import androidx.annotation.NonNull;

public class Profile
{
    int id;
    String userName;
    int userWeight;

    public Profile() {}

    public Profile(String userName, int userWeight)
    {
        this.userName = userName;
        this.userWeight = userWeight;
    }

    public void setId(int id) { this.id = id; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setUserWeight(int userWeight) { this.userWeight = userWeight; }

    public int getId() { return this.id; }
    public String getUserName() { return this.userName; }
    public int getUserWeight() { return this.userWeight; }


    @NonNull
    @Override
    public String toString() {
        return this.userName;
    }

}
