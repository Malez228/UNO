package com.malec.uno;

import java.util.List;

public class RoomClass
{
    public static class Player
    {
        String IMEI, Index;
        public Player(String imei, String index)
        {
            this.IMEI = imei;
            this.Index = index;
        }

        private String getIMEI()
        {
            return this.IMEI;
        }
    }

    private String Name, Pass, Players, Turns;
    private List<Player> Pls;

    public RoomClass(String name, String pass, String players, String turns, List<Player> pls)
    {
        this.Name = name;
        this.Pass = pass;
        this.Players = players;
        this.Turns = turns;
        this.Pls = pls;
    }

    public String getPlayerIMEI(Integer Index)
    {
        return Pls.get(Index).getIMEI();
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
