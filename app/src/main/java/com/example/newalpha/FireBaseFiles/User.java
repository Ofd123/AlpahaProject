package com.example.newalpha.FireBaseFiles;

public class User
{
    private int userID;
    private String name;
    private String email;
    private String password;

    public User (int userID, String name, String email, String password)
    {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public User()
    {
        this.userID = 0;
        this.name = "";
        this.email = "";
        this.password = "";
    }
}
