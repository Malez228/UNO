package com.malec.ino;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TurnExplorer extends Service
{
    Random rnd = new Random();
    DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();

    @Override
    public IBinder onBind(Intent intent)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        dataBase.child(GameActivity.ThisRoom.Name).child("CurrentPlayer").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                    if (Integer.valueOf(dataSnapshot.getValue().toString()) == GameActivity.player.ID)
                    {
                        KeyguardManager myKM = (KeyguardManager) getSystemService(GameActivity.KEYGUARD_SERVICE);
                        if (myKM.inKeyguardRestrictedInputMode())
                        {
                            long mills = 500L;
                            Vibrator vibrator = (Vibrator) getSystemService(GameActivity.VIBRATOR_SERVICE);
                            vibrator.vibrate(mills);
                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
