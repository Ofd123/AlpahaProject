package com.example.newalpha.FireBaseFiles;

public class KeyAndVal
{
    private String key;
    private String val;
    public String id;
    public KeyAndVal(String key, String val)
    {
        this.key = key;
        this.val = val;
    }
    public KeyAndVal()
    {
        this.key = "";
        this.val = "";
    }
    public void setID(String id)
    {
        this.id = id;
    }
    public String getID()
    {
        return this.id;
    }
    public void setKey(String key)
    {
        this.key = key;
    }
    public String getKey()
    {
        return this.key;
    }
    public void setVal(String val)
    {
        this.val = val;
    }
    public String getVal()
    {
        return this.val;
    }
}

