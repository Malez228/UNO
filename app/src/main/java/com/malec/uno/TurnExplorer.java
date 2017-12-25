package com.malec.uno;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TurnExplorer extends Service
{
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        database.child(MenuActivity.ClickRoomName).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                if (dataSnapshot.getKey().compareTo("CurrentPlayer") == 0)
                    if (dataSnapshot.getValue().toString().compareTo(MainActivity.Player.toString()) == 0)
                    {
                        KeyguardManager myKM = (KeyguardManager) getSystemService(MainActivity.KEYGUARD_SERVICE);
                        if(myKM.inKeyguardRestrictedInputMode())
                        {
                            long mills = 700L;
                            Vibrator vibrator = (Vibrator) getSystemService(MainActivity.VIBRATOR_SERVICE);
                            vibrator.vibrate(mills);
                            Log.i("TurnExplorer", "Vibrate!");
                        }
                    }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
