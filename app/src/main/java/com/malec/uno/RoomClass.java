package com.malec.uno;

public class RoomClass
{
    private String Name, Pass, Players, Turns;

    public RoomClass(String name, String pass, String players, String turns)
    {
        this.Name = name;
        this.Pass = pass;
        this.Players = players;
        this.Turns = turns;
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

    public String getTurns()
    {
        return this.Turns;
    }
}
