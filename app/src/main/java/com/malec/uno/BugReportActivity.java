package com.malec.uno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class BugReportActivity extends AppCompatActivity
{
    Button SendBtn;

    EditText MsgText;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        this.setTitle("INO - Сообщение разработчику");

        SendBtn = (Button) findViewById(R.id.SendBtn);
        MsgText = (EditText) findViewById(R.id.MsgText);

        SendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Random rnd = new Random();
                Integer child = rnd.nextInt();
                database.child("Msg").child(child.toString()).setValue(MsgText.getText().toString());
                finish();
            }
        });
    }
}
