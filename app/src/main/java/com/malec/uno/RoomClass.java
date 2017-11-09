package com.malec.uno;

public class RoomClass
{
    private String Name, Pass, Players;

    public RoomClass(String name, String pass, String players)
    {
        this.Name = name;
        this.Pass = pass;
        this.Players = players;
    }

    public void setName(String newName)
    {
        this.Name = newName;
    }

    public String getName()
    {
        return this.Name;
    }

    public void setPass(String newPass)
    {
        this.Pass = newPass;
    }

    public String getPass()
    {
        return this.Pass;
    }

    public void setPlayers(String newPlayers)
    {
        this.Players = newPlayers;
    }

    public String getPlayers()
    {
        return this.Players;
    }
}
