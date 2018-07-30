package com.malec.ino;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static Context context;
    Random rnd = new Random();
    DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();

    public static String RoomName;
    public static String ClickRoomName;

    boolean adequate = true;

    public static String Data = "";

    String HTMLVersion = "";
    public static String PhoneKey = "";
    public static String UserName = "Name";
    public static final String APP_PREFERENCES_PhoneKey = "";
    public static final String APP_PREFERENCES_UserName = "Name";

    public static List<String> RoomsNameList = new ArrayList<>();
    public static List<Room> RoomsList = new ArrayList<>();
    public static List<String> SearchRoomsNameList = new ArrayList<>();
    List<Room> SearchRoomsList = new ArrayList<>();

    private SharedPreferences mSettings;

    final RecyclerView[] recyclerView = new RecyclerView[1];
    RoomDataAdapter adapter = null;

    public void CheckVersion()
    {
        String HTML = null;
        InternetRequest htm = new InternetRequest();
        htm.execute("https://raw.githubusercontent.com/Malez228/UNO/master/app/build.gradle");
        try { HTML = htm.get().toString(); } catch (Exception e){ }
        htm.cancel(true);
        HTMLVersion = HTML.split("versionName \"")[1].split("\"")[0];
        String Version = "";
        try
        {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            Version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) { }
        if (Version.compareTo(HTMLVersion) != 0)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
            alert.setTitle("New version available").setPositiveButton("Download", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    if (ContextCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    else
                        DownloadNewVersion();
                }
            }).setNegativeButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
            });
            alert.show();
        } else
        {
            Toast.makeText(context, "This is the latest version", Toast.LENGTH_SHORT).show();
        }
    }

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
                            ClickRoom = SearchRoomsList.get(SearchRoomsNameList.indexOf(ClickRoomName));
                        } catch (Exception e)
                        {
                            return;
                        }

                        if ((ClickRoom.Turns <= ClickRoom.ConnectedPlayers || ClickRoom.PlayersKeys.contains(PhoneKey)) && adequate)
                        {
                            adequate = false;

                            if (ClickRoom.Pass.compareTo("0") == 0 || ClickRoom.PlayersKeys.contains(PhoneKey))
                            {
                                if (ClickRoom.PlayersKeys.contains(PhoneKey))
                                {
                                    dataBase.child(ClickRoomName).child("Players").child(PhoneKey).child("Name").setValue(UserName);
                                    GameActivity.Reconnect = true;
                                } else
                                {
                                    ClickRoom.ConnectedPlayers++;
                                    Map newPlayerData = new HashMap();
                                    newPlayerData.put("ID", ClickRoom.ConnectedPlayers);
                                    newPlayerData.put("Name", UserName);
                                    newPlayerData.put("Cards", "");
                                    dataBase.child(ClickRoomName).child("Players").child(PhoneKey).updateChildren(newPlayerData);
                                    dataBase.child(ClickRoomName).child("ConnectedPlayers").setValue(ClickRoom.ConnectedPlayers);
                                }

                                RoomName = ClickRoomName;
                                GameActivity.ThisRoom = ClickRoom;
                                startActivity(new Intent(MenuActivity.this, GameActivity.class));
                            } else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                                LayoutInflater inflater = MenuActivity.this.getLayoutInflater();
                                final View Field = inflater.inflate(R.layout.room_creator_layout, null);

                                final Room finalClickRoom = ClickRoom;
                                final Room finalClickRoom1 = ClickRoom;

                                EditText RoomNameField = (EditText) Field.findViewById(R.id.RoomNameField);
                                TextView ActionText = (TextView) Field.findViewById(R.id.ActionText);

                                RoomNameField.setVisibility(View.GONE);
                                ActionText.setText(R.string.EnterPassword);

                                builder.setView(Field).setPositiveButton(getString(R.string.EnterRoom), new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        EditText PassField = (EditText) Field.findViewById(R.id.PassField);
                                        String Pass = PassField.getText().toString();

                                        if (Pass.compareTo(finalClickRoom.Pass) == 0)
                                        {
                                            if (finalClickRoom.PlayersKeys.contains(PhoneKey))
                                            {
                                                dataBase.child(ClickRoomName).child("Players").child(PhoneKey).child("Name").setValue(UserName);
                                                GameActivity.Reconnect = true;
                                            } else
                                            {
                                                finalClickRoom.ConnectedPlayers++;
                                                Map newPlayerData = new HashMap();
                                                newPlayerData.put("ID", finalClickRoom.ConnectedPlayers);
                                                newPlayerData.put("Name", UserName);
                                                newPlayerData.put("Cards", "");
                                                dataBase.child(ClickRoomName).child("Players").child(PhoneKey).updateChildren(newPlayerData);
                                                dataBase.child(ClickRoomName).child("ConnectedPlayers").setValue(finalClickRoom.ConnectedPlayers);
                                            }

                                            RoomName = ClickRoomName;
                                            GameActivity.ThisRoom = finalClickRoom1;
                                            startActivity(new Intent(MenuActivity.this, GameActivity.class));
                                        } else
                                        {
                                            Toast.makeText(MenuActivity.this, R.string.WrongPassword, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            adequate = true;
                                        }
                                    }
                                }).setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        dialog.dismiss();
                                        adequate = true;
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }else if (ClickRoom.Turns > ClickRoom.ConnectedPlayers && !ClickRoom.PlayersKeys.contains(PhoneKey))
                            Toast.makeText(MenuActivity.this, ""+getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
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

    public void DownloadNewVersion()
    {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(dir, "INO " + HTMLVersion + ".apk");
        if (file.exists()) file.delete();

        Uri uri = Uri.parse("https://raw.githubusercontent.com/Malez228/UNO/master/app/release/INO.apk");
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        final DownloadManager mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        final long lastDownload = mgr.enqueue(new DownloadManager.Request(uri).setTitle("INO").setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDescription("v " + HTMLVersion).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "INO " + HTMLVersion + ".apk"));

        BroadcastReceiver receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action))
                {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(lastDownload);
                    Cursor c = mgr.query(query);
                    if (c.moveToFirst() && DownloadManager.STATUS_SUCCESSFUL == c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)))
                        Toast.makeText(context, "Откройте приложение вручную, используя диспетчер файлов", Toast.LENGTH_LONG).show();
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

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
    protected void onResume()
    {
        super.onResume();

        SearchView RoomSearch = findViewById(R.id.RoomSearch);
        RoomSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s)
            {
                SearchRoomsList.clear();
                SearchRoomsNameList.clear();

                if (!s.isEmpty() || s.compareTo("") != 0)
                {
                    for (Room r : RoomsList)
                        if (r.Name.contains(s))
                        {
                            SearchRoomsList.add(r);
                            SearchRoomsNameList.add(r.Name);
                        }
                }else
                    for (Room r : RoomsList)
                    {
                        SearchRoomsList.add(r);
                        SearchRoomsNameList.add(r.Name);
                    }

                recyclerView[0].getAdapter().notifyDataSetChanged();

                return false;
            }
        });

        if (!Data.isEmpty())
        {
            Data = "";
            recreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            DownloadNewVersion();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.MenuToolBar);
        setSupportActionBar(toolbar);
        mSettings = getPreferences(MODE_PRIVATE);
        context = getApplicationContext();

        adapter = new RoomDataAdapter(this, SearchRoomsList);
        recyclerView[0] = findViewById(R.id.RoomListRec);
        recyclerView[0].setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView[0].setAdapter(adapter);
        recyclerView[0].addOnItemTouchListener(ItemTouchListener);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //region Get PhoneKey
        final SharedPreferences.Editor editor = mSettings.edit();
        String savedText = mSettings.getString(APP_PREFERENCES_PhoneKey, "");
        if (savedText.isEmpty())
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            PhoneKey = tm.getNetworkOperatorName() + "☺" + tm.getSimCountryIso() + "☺" + Runtime.getRuntime().maxMemory() + "☺" + dm.widthPixels + "☺" + dm.heightPixels + "☺" + dm.densityDpi + "☺" + android.os.Build.MODEL.replace('.', '☻').replace('#', '☻').replace('$', '☻').replace('[', '☻').replace(']', '☻') + "☺" + android.os.Build.VERSION.RELEASE.replace('.', '☻').replace('#', '☻').replace('$', '☻').replace('[', '☻').replace(']', '☻');
        } else
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        //region CreateRoomFAB
        FloatingActionButton CreateRoomButton = (FloatingActionButton) findViewById(R.id.fab);
        CreateRoomButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                LayoutInflater inflater = MenuActivity.this.getLayoutInflater();
                final View Field = inflater.inflate(R.layout.room_creator_layout, null);

                builder.setView(Field).setPositiveButton(getString(R.string.CreateRoom), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        EditText RoomNameField = (EditText) Field.findViewById(R.id.RoomNameField);
                        EditText PassField = (EditText) Field.findViewById(R.id.PassField);

                        RoomName = RoomNameField.getText().toString();
                        if (RoomName.isEmpty())
                            RoomName = "Room_" + Integer.valueOf(rnd.nextInt()).toString().replace("-", "").substring(0, 4);
                        String Pass = PassField.getText().toString();
                        if (Pass.isEmpty())
                            Pass = "0";

                        if (!RoomsNameList.contains(RoomName))
                        {
                            adequate = false;

                            Integer mm = Calendar.getInstance().get(Calendar.MINUTE);
                            Map newRoomData = new HashMap();
                            Map newPlayerData = new HashMap();
                            newRoomData.put("Pass", Pass);
                            newRoomData.put("Turns", 1);
                            newRoomData.put("ConnectedPlayers", 1);
                            newRoomData.put("Color", -1);
                            newRoomData.put("CurrentPlayer", 1);
                            newRoomData.put("MaxDraw", 7);
                            newRoomData.put("TurnDir", 1);
                            newRoomData.put("Winner", "");
                            newRoomData.put("Msg", "");
                            newRoomData.put("Card", rnd.nextInt(52));
                            newRoomData.put("NewCard", rnd.nextInt(52));
                            newRoomData.put("Time", mm);
                            newRoomData.put("Ready", 0);
                            newPlayerData.put("ID", 1);
                            newPlayerData.put("Name", UserName);
                            newPlayerData.put("Cards", "");
                            dataBase.child(RoomName).updateChildren(newRoomData);
                            dataBase.child(RoomName).child("Players").child(PhoneKey).updateChildren(newPlayerData);

                            final Room NewRoom = new Room();
                            NewRoom.Name = RoomName;
                            NewRoom.Pass = Pass;
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
                            //recyclerView[0].getAdapter().notifyDataSetChanged();

                            GameActivity.ThisRoom = NewRoom;

                            startActivity(new Intent(MenuActivity.this, GameActivity.class));
                        } else
                            Toast.makeText(MenuActivity.this, "Комната уже существует", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

                    Integer mm = Calendar.getInstance().get(Calendar.MINUTE);
                    Integer BaseMM;
                    try
                    {
                        BaseMM = Integer.valueOf(dataSnapshot.child("Time").getValue().toString());
                        if (mm - Integer.valueOf(BaseMM) >= 10 || (mm - Integer.valueOf(BaseMM) >= -50 && mm - Integer.valueOf(BaseMM) < 0))
                            dataBase.child(roomName).removeValue();
                        else
                        {
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
                                player.ID = Integer.valueOf(Player.child("ID").getValue().toString());
                                player.Name = Player.child("Name").getValue().toString();

                                NewRoom.Players.add(player);
                                NewRoom.PlayersKeys.add(player.Key);
                            }
                            if (NewRoom.Players.isEmpty())
                                NewRoom.ConnectedPlayers = 0;
                            else
                                NewRoom.ConnectedPlayers = NewRoom.Players.size();

                            RoomsList.add(NewRoom);
                            SearchRoomsList.add(NewRoom);
                            SearchRoomsNameList.add(NewRoom.Name);
                            RoomsNameList.add(NewRoom.Name);
                            recyclerView[0].getAdapter().notifyDataSetChanged();
                        }
                    } catch (Exception e)
                    {
                        dataBase.child(roomName).removeValue();
                        return;
                    }
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
                    SearchRoomsList.set(RoomsNameList.indexOf(roomName), UpdRoom);
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
                    RoomsNameList.remove(roomName);
                    SearchRoomsList.remove(SearchRoomsNameList.indexOf(roomName));
                    SearchRoomsNameList.remove(roomName);
                } catch (Exception e) { }

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();

        if (id == R.id.nav_info)
        {
            Toast.makeText(context, "хренакнуть суда текст", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_vers)
        {
            CheckVersion();

        } else if (id == R.id.nav_send)
        {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
