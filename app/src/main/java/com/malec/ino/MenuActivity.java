package com.malec.ino;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Random rnd = new Random();
    DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();

    EditText RoomNameText;
    public static String RoomName;
    public static String ClickRoomName;

    boolean adequate = true;

    public static String PhoneKey = "";
    public static String UserName = "Name";
    public static final String APP_PREFERENCES_PhoneKey = "";
    public static final String APP_PREFERENCES_UserName = "Name";

    public static List<String> RoomsNameList = new ArrayList<>();
    public static List<Room> RoomsList = new ArrayList<>();

    private SharedPreferences mSettings;

    public static class Room
    {
        String Name, Pass;
        List<GameActivity.Player> Players;
        List<String> PlayersKeys;
        Integer ConnectedPlayers, Turns;

        Room()
        {
            this.Name = "";
            this.Pass = "";
            this.Players = new ArrayList<>();
            this.PlayersKeys = new ArrayList<>();
            this.ConnectedPlayers = -1;
            this.Turns = -1;
        }
    }

    RecyclerView.OnItemTouchListener ItemTouchListener = new RecyclerView.OnItemTouchListener()
    {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
        {
            if (adequate)
            {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Room ClickRoom = null;
                        try
                        {
                            ClickRoom = RoomsList.get(RoomsNameList.indexOf(ClickRoomName));
                        } catch (Exception e)
                        {
                            return;
                        }

                        if (ClickRoom.Turns <= ClickRoom.ConnectedPlayers || ClickRoom.PlayersKeys.contains(PhoneKey))
                        {
                            if (adequate)
                            {
                                adequate = false;

                                if (ClickRoom.PlayersKeys.contains(PhoneKey))
                                {
                                    dataBase.child(ClickRoomName).child("Players").child(PhoneKey).child("Name").setValue(UserName);
                                    GameActivity.Reconnect = true;
                                } else
                                {
                                    ClickRoom.ConnectedPlayers++;
                                    Map newPlayerData = new HashMap();
                                    Map newRoomData = new HashMap();
                                    newPlayerData.put("ID", ClickRoom.ConnectedPlayers);
                                    newPlayerData.put("Name", UserName);
                                    newRoomData.put("ConnectedPlayers", ClickRoom.ConnectedPlayers);
                                    dataBase.child(ClickRoomName).child("Players").child(PhoneKey).updateChildren(newPlayerData);
                                    dataBase.child(ClickRoomName).updateChildren(newRoomData);
                                }

                                RoomName = ClickRoomName;
                                GameActivity.ThisRoom = ClickRoom;
                                startActivity(new Intent(MenuActivity.this, GameActivity.class));
                            }
                        }
                    }
                }, 100);
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
    };

    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PhoneKey, PhoneKey);
        editor.putString(APP_PREFERENCES_UserName, UserName);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.MenuToolBar);
        setSupportActionBar(toolbar);

        RoomNameText = (EditText) findViewById(R.id.RoomNameText);

        mSettings = getPreferences(MODE_PRIVATE);

        //region Get PhoneKey
        final SharedPreferences.Editor editor = mSettings.edit();
        String savedText = mSettings.getString(APP_PREFERENCES_PhoneKey, "");
        if (savedText.isEmpty())
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            PhoneKey = tm.getNetworkOperatorName() + "☺" + tm.getSimCountryIso() + "☺" + Runtime.getRuntime().maxMemory() + "☺" + dm.widthPixels + "☺" + dm.heightPixels + "☺" + dm.densityDpi + "☺" + android.os.Build.MODEL.replace('.', '☻').replace('#', '☻').replace('$', '☻').replace('[', '☻').replace(']', '☻') + "☺" + android.os.Build.VERSION.RELEASE.replace('.', '☻').replace('#', '☻').replace('$', '☻').replace('[', '☻').replace(']', '☻');
        }else
            PhoneKey = savedText;
        editor.putString(APP_PREFERENCES_PhoneKey, PhoneKey);
        editor.apply();
        //endregion

        //region Get UserName
        savedText = mSettings.getString(APP_PREFERENCES_UserName, "");
        if (savedText.isEmpty())
            UserName = "User_" + Integer.valueOf(rnd.nextInt()).toString().replace("-", "").substring(0, 4);
        else
            UserName = savedText;
        //endregion

        //region Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener()
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) { }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                EditText UserNameText = (EditText) findViewById(R.id.UserNameText);
                    UserNameText.setText(UserName);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                EditText UserNameText = (EditText) findViewById(R.id.UserNameText);
                if (UserNameText.getText().toString().isEmpty())
                    UserNameText.setText("User_" + Integer.valueOf(rnd.nextInt()).toString().replace("-", "").substring(0, 4));
                //шоб сильно умных не было
                if (UserNameText.getText().toString().length() > 20)
                    UserName = UserNameText.getText().toString().substring(0, 20);
                else
                    UserName = UserNameText.getText().toString();

                editor.putString(APP_PREFERENCES_UserName, UserName);
                editor.apply();
            }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });
        toggle.syncState();
        //endregion

        final RecyclerView[] recyclerView = new RecyclerView[1];
        final RoomDataAdapter adapter = new RoomDataAdapter(this, RoomsList);

        recyclerView[0] = (RecyclerView) findViewById(R.id.RoomListRec);
        recyclerView[0].setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView[0].setAdapter(adapter);
        recyclerView[0].addOnItemTouchListener(ItemTouchListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //region CreateRoomFAB
        FloatingActionButton CreateRoomButton = (FloatingActionButton) findViewById(R.id.fab);
        CreateRoomButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RoomName = RoomNameText.getText().toString();

                if (!RoomsNameList.contains(RoomName))
                {
                    if (RoomName.isEmpty())
                        RoomName = "Room_" + Integer.valueOf(rnd.nextInt()).toString().replace("-", "").substring(0, 4);

                    adequate = false;

                    Map newRoomData = new HashMap();
                    Map newPlayerData = new HashMap();
                    newRoomData.put("Pass", 0);
                    newRoomData.put("Turns", 1);
                    newRoomData.put("ConnectedPlayers", 1);
                    newRoomData.put("Color", -1);
                    newRoomData.put("CurrentPlayer", 1);
                    newRoomData.put("MaxDraw", 7);
                    newRoomData.put("TurnDir", 1);
                    newRoomData.put("Winner", "");
                    newRoomData.put("Card", rnd.nextInt(52));
                    newRoomData.put("NewCard", rnd.nextInt(52));
                    newPlayerData.put("ID", 1);
                    newPlayerData.put("Name", UserName);
                    dataBase.child(RoomName).updateChildren(newRoomData);
                    dataBase.child(RoomName).child("Players").child(PhoneKey).updateChildren(newPlayerData);

                    final Room NewRoom = new Room();
                    NewRoom.Name = RoomName;
                    NewRoom.Pass = "0";
                    NewRoom.Turns = 1;
                    GameActivity.Player thisPlayer = new GameActivity.Player();
                    thisPlayer.Key = PhoneKey;
                    thisPlayer.Name = UserName;
                    thisPlayer.ID = 1;
                    NewRoom.Players.add(thisPlayer);
                    NewRoom.PlayersKeys.add(thisPlayer.Key);
                    NewRoom.ConnectedPlayers = 1;

                    RoomsList.add(NewRoom);
                    RoomsNameList.add(NewRoom.Name);
                    recyclerView[0].getAdapter().notifyDataSetChanged();

                    GameActivity.ThisRoom = NewRoom;

                    startActivity(new Intent(MenuActivity.this, GameActivity.class));
                }else
                    Toast.makeText(MenuActivity.this, "Комната уже существует", Toast.LENGTH_SHORT).show();
            }
        });
        //endregion

        dataBase.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                if (adequate)
                {
                    String roomName = dataSnapshot.getKey().toString();

                    final Room NewRoom = new Room();
                    NewRoom.Name = roomName;
                    NewRoom.Players = new ArrayList<>();

                    NewRoom.Pass = dataSnapshot.child("Pass").getValue().toString();
                    NewRoom.Turns = Integer.valueOf(dataSnapshot.child("Turns").getValue().toString());
                    for (DataSnapshot Player : dataSnapshot.child("Players").getChildren())
                    {
                        GameActivity.Player player = new GameActivity.Player();
                        player = player.InitPlayer();
                        player.Key = Player.getKey().toString();
                        player.Name = Player.child("Name").getValue().toString();

                        NewRoom.Players.add(player);
                        NewRoom.PlayersKeys.add(player.Key);
                    }
                    if (NewRoom.Players.isEmpty())
                        NewRoom.ConnectedPlayers = 0;
                    else
                        NewRoom.ConnectedPlayers = NewRoom.Players.size();

                    RoomsList.add(NewRoom);
                    RoomsNameList.add(NewRoom.Name);
                    recyclerView[0].getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                if (adequate)
                {
                    String roomName = dataSnapshot.getKey().toString();

                    Room UpdRoom = new Room();
                    UpdRoom.Name = roomName;
                    UpdRoom.Players = new ArrayList<>();

                    UpdRoom.Pass = dataSnapshot.child("Pass").getValue().toString();
                    UpdRoom.Turns = Integer.valueOf(dataSnapshot.child("Turns").getValue().toString());
                    for (DataSnapshot Player : dataSnapshot.child("Players").getChildren())
                    {
                        GameActivity.Player player = new GameActivity.Player();
                        player = player.InitPlayer();
                        player.Key = Player.getKey().toString();
                        player.Name = Player.child("Name").getValue().toString();

                        UpdRoom.Players.add(player);
                        UpdRoom.PlayersKeys.add(player.Key);
                    }
                    if (UpdRoom.Players.isEmpty())
                        UpdRoom.ConnectedPlayers = 0;
                    else
                        UpdRoom.ConnectedPlayers = UpdRoom.Players.size();

                    RoomsList.set(RoomsNameList.indexOf(roomName), UpdRoom);
                    recyclerView[0].getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                try
                {
                    String roomName = dataSnapshot.getKey().toString();
                    RoomsList.remove(RoomsNameList.indexOf(roomName));
                }
                catch (Exception e) { }

                recyclerView[0].getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.nav_info)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
