package com.malec.uno;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Menu extends AppCompatActivity
{
	DatabaseReference database = FirebaseDatabase.getInstance().getReference();

	ConstraintLayout Room1, Room2, Room3, Room4, Room5;

	TextView Room1Name, Room2Name, Room3Name, Room4Name, Room5Name;
	TextView Room1Players, Room2Players, Room3Players, Room4Players, Room5Players;

	Button CreateRoomButton, FindRoomButton;
	EditText CreateRoomField, FindRoomField;

	public static String RoomName = "";

	protected View.OnClickListener RoomClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			ConstraintLayout Room = (ConstraintLayout) view;

			RoomName = ((TextView) Room.getChildAt(1)).getText().toString();
			startActivity(new Intent(Menu.this, MainActivity.class));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		//region Инициализация
		Room1 = (ConstraintLayout) findViewById(R.id.Room1);
		Room2 = (ConstraintLayout) findViewById(R.id.Room2);
		Room3 = (ConstraintLayout) findViewById(R.id.Room3);
		Room4 = (ConstraintLayout) findViewById(R.id.Room4);
		Room5 = (ConstraintLayout) findViewById(R.id.Room5);
		Room1Name = (TextView) findViewById(R.id.Room1Name);
		Room2Name = (TextView) findViewById(R.id.Room2Name);
		Room3Name = (TextView) findViewById(R.id.Room3Name);
		Room4Name = (TextView) findViewById(R.id.Room4Name);
		Room5Name = (TextView) findViewById(R.id.Room5Name);
		Room1Players = (TextView) findViewById(R.id.Room1Players);
		Room2Players = (TextView) findViewById(R.id.Room2Players);
		Room3Players = (TextView) findViewById(R.id.Room3Players);
		Room4Players = (TextView) findViewById(R.id.Room4Players);
		Room5Players = (TextView) findViewById(R.id.Room5Players);
		CreateRoomButton = (Button) findViewById(R.id.CreateRoomButton);
		FindRoomButton = (Button) findViewById(R.id.FindRoomButton);
		CreateRoomField = (EditText) findViewById(R.id.CreateRoomField);
		FindRoomField = (EditText) findViewById(R.id.FindRoomField);
		//endregion

		database.addValueEventListener(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				int i = 1;
				for (DataSnapshot child : dataSnapshot.getChildren())
				{
					String pl = "0";
					for (DataSnapshot child2 : child.getChildren())
					{
						if (child2.getKey().compareTo("ConnectedPlayers") == 0)
						{
							pl = child2.getValue().toString();
							break;
						}
					}

					switch (i)
					{
						case 1:
							Room1.setVisibility(View.VISIBLE);
							Room1Name.setText(child.getKey());
							Room1Players.setText("Подключено игроков " + pl);
							i++;
							break;
						case 2:
							Room2.setVisibility(View.VISIBLE);
							Room2Name.setText(child.getKey());
							Room2Players.setText("Подключено игроков " + pl);
							i++;
							break;
						case 3:
							Room3.setVisibility(View.VISIBLE);
							Room3Name.setText(child.getKey());
							Room3Players.setText("Подключено игроков " + pl);
							i++;
							break;
						case 4:
							Room4.setVisibility(View.VISIBLE);
							Room4Name.setText(child.getKey());
							Room4Players.setText("Подключено игроков " + pl);
							i++;
							break;
						case 5:
							Room5.setVisibility(View.VISIBLE);
							Room5Name.setText(child.getKey());
							Room5Players.setText("Подключено игроков " + pl);
							i++;
							break;
					}
				}
			}

			@Override public void onCancelled(DatabaseError databaseError) { }
		});

		FindRoomButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				RoomName = FindRoomField.getText().toString();

				database.child(RoomName).addListenerForSingleValueEvent(new ValueEventListener()
				{
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						try
						{
							String s = dataSnapshot.getValue().toString();
							startActivity(new Intent(Menu.this, MainActivity.class));
						} catch (Exception e)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
							builder.setTitle("Поиск");
							builder.setMessage("Комната по данному запросу не найдена\nВы можете создать новую комнату с этим названием");
							builder.setCancelable(true);
							builder.setPositiveButton("Создать", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									Random rnd = new Random();
									if (RoomName.compareTo("") == 0) RoomName = "Room" + rnd.nextLong();

									database.child(RoomName).child("Card").setValue(0);
									database.child(RoomName).child("Color").setValue(0);
									database.child(RoomName).child("ConnectedPlayers").setValue(0);
									database.child(RoomName).child("CurrentPlayer").setValue(1);
									database.child(RoomName).child("MaxDraw").setValue(1);
									database.child(RoomName).child("NewCard").setValue(0);
									database.child(RoomName).child("TurnDir").setValue(1);
									database.child(RoomName).child("Winner").setValue(0);

									startActivity(new Intent(Menu.this, MainActivity.class));
								}
							});
							builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
								}
							});
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) { }
				});
			}
		});

		CreateRoomButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				RoomName = CreateRoomField.getText().toString();

				database.child(RoomName).child("Card").setValue(0);
				database.child(RoomName).child("Color").setValue(0);
				database.child(RoomName).child("ConnectedPlayers").setValue(0);
				database.child(RoomName).child("CurrentPlayer").setValue(1);
				database.child(RoomName).child("MaxDraw").setValue(1);
				database.child(RoomName).child("NewCard").setValue(0);
				database.child(RoomName).child("TurnDir").setValue(1);
				database.child(RoomName).child("Winner").setValue(0);

				startActivity(new Intent(Menu.this, MainActivity.class));
			}
		});

		Room1.setOnClickListener(RoomClick);
		Room2.setOnClickListener(RoomClick);
		Room3.setOnClickListener(RoomClick);
		Room4.setOnClickListener(RoomClick);
		Room5.setOnClickListener(RoomClick);
	}
}
