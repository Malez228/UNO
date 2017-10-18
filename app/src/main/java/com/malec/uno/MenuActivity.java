package com.malec.uno;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MenuActivity extends AppCompatActivity
{
	DatabaseReference database = FirebaseDatabase.getInstance().getReference();

	ConstraintLayout Room1, Room2, Room3, Room4, Room5, Room6, Room7, Room8, Room9, Room10;

	TextView Room1Name, Room2Name, Room3Name, Room4Name, Room5Name, Room6Name, Room7Name, Room8Name, Room9Name, Room10Name;
	TextView Room1Players, Room2Players, Room3Players, Room4Players, Room5Players, Room6Players, Room7Players, Room8Players, Room9Players, Room10Players;

	ImageView LockButton;

	Button CreateRoomButton, FindRoomButton;
	EditText CreateRoomField;

	public static String RoomName = "";

	public static Boolean Animation = true;

	Integer PochemuOnoNeRabotaetNormalno = 4;

	String pass = "0";

	protected View.OnClickListener RoomClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			ConstraintLayout Room = (ConstraintLayout) view;

			RoomName = ((TextView) Room.getChildAt(1)).getText().toString();

			database.child(RoomName).addListenerForSingleValueEvent(new ValueEventListener()
			{
				@Override
				public void onDataChange(final DataSnapshot dataSnapshot)
				{
					if (dataSnapshot.child("Pass").getValue().toString().compareTo("0") != 0)
					{
						AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);

						alert.setTitle(getString(R.string.EnterPass));

						final EditText input = new EditText(MenuActivity.this);
						alert.setView(input);

						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int whichButton)
							{
								if (input.getText().toString().compareTo(dataSnapshot.child("Pass").getValue().toString()) == 0)
								{
									if (Integer.valueOf(dataSnapshot.child("Turns").getValue().toString()) <= Integer.valueOf(dataSnapshot.child("ConnectedPlayers").getValue().toString()))
										startActivity(new Intent(MenuActivity.this, MainActivity.class));
									else
										Toast.makeText(MenuActivity.this, getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
								} else
								{
									Toast.makeText(MenuActivity.this, getString(R.string.WrongPassword), Toast.LENGTH_SHORT).show();
								}
							}
						});

						alert.setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int whichButton) { }
						});

						alert.show();
					} else if (Integer.valueOf(dataSnapshot.child("Turns").getValue().toString()) <= Integer.valueOf(dataSnapshot.child("ConnectedPlayers").getValue().toString()))
						startActivity(new Intent(MenuActivity.this, MainActivity.class));
					else
						Toast.makeText(MenuActivity.this, getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onCancelled(DatabaseError databaseError) { }
			});
		}
	};

	@Override
	protected void onResume()
	{
		super.onResume();
		PochemuOnoNeRabotaetNormalno = 4;

		LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);
		for (int i = 0; i < RoomListLayout.getChildCount(); i++)
		{
			ConstraintLayout layout = (ConstraintLayout) RoomListLayout.getChildAt(i);
			layout.setVisibility(View.GONE);

			TextView t1 = (TextView) layout.getChildAt(1);
			TextView t2 = (TextView) layout.getChildAt(0);
			t1.setText("Комната " + i);
			t2.setText("Количество игроков");
		};

		Thing();
	}

	void Thing()
	{
		LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);
		for (int i = 0; i < RoomListLayout.getChildCount(); i++)
		{
			ConstraintLayout layout = (ConstraintLayout) RoomListLayout.getChildAt(i);
			layout.setVisibility(View.GONE);
		};

		database.addChildEventListener(new ChildEventListener()
		{
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					if (PochemuOnoNeRabotaetNormalno % 2 == 0)
					{
						String room = dataSnapshot.getKey().toString();

						if (dataSnapshot.getKey().toString().compareTo("Msg") != 0)
						{
							LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);
							for (int i = 0; i < RoomListLayout.getChildCount(); i++)
							{
								ConstraintLayout layout = (ConstraintLayout) RoomListLayout.getChildAt(i);

								if (layout.getVisibility() == View.GONE)
								{
									TextView t1 = (TextView) layout.getChildAt(1);
									TextView t2 = (TextView) layout.getChildAt(0);
									if (t1.getText().toString().startsWith("Комната"))
									{
										layout.setVisibility(View.VISIBLE);
										t1.setText(room);
										t2.setText(getString(R.string.TotalPlayers) + " " + dataSnapshot.child("ConnectedPlayers").getValue().toString());
										break;
									}
								}
							}
						}
					}

					PochemuOnoNeRabotaetNormalno++;
				} catch (Exception e)
				{
					Log.e("pizda", e.toString());
				}
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					String room = dataSnapshot.getKey().toString();

					if (dataSnapshot.getKey().toString().compareTo("Msg") != 0)
					{
						LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);
						for (int i = 0; i < RoomListLayout.getChildCount(); i++)
						{
							ConstraintLayout layout = (ConstraintLayout) RoomListLayout.getChildAt(i);

							if (layout.getVisibility() != View.GONE)
							{
								TextView t1 = (TextView) layout.getChildAt(1);
								TextView t2 = (TextView) layout.getChildAt(0);
								if (room.compareTo(t1.getText().toString()) == 0)
								{
									t2.setText(getString(R.string.TotalPlayers) + " " + dataSnapshot.child("ConnectedPlayers").getValue().toString());
									break;
								}
							}
						}
					}
				} catch (Exception e)
				{
					Log.e("jopa", e.toString());
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot)
			{
				try
				{
					String room = dataSnapshot.getKey().toString();

					if (dataSnapshot.getKey().toString().compareTo("Msg") != 0)
					{
						LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);
						for (int i = 0; i < RoomListLayout.getChildCount(); i++)
						{
							ConstraintLayout layout = (ConstraintLayout) RoomListLayout.getChildAt(i);

							if (layout.getVisibility() != View.GONE)
							{
								TextView t1 = (TextView) layout.getChildAt(1);
								TextView t2 = (TextView) layout.getChildAt(0);
								if (room.compareTo(t1.getText().toString()) == 0)
								{
									layout.setVisibility(View.GONE);
									t1.setText("Комната " + i);
									t2.setText("Количество игроков");
									break;
								}
							}
						}
					}
				} catch (Exception e)
				{
					Log.e("gavno", e.toString());
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setTitle(getString(R.string.app_name) + " - " + getString(R.string.FindCreateRoom));
		setSupportActionBar(myToolbar);

		//region Инициализация
		Room1 = (ConstraintLayout) findViewById(R.id.Room1);
		Room2 = (ConstraintLayout) findViewById(R.id.Room2);
		Room3 = (ConstraintLayout) findViewById(R.id.Room3);
		Room4 = (ConstraintLayout) findViewById(R.id.Room4);
		Room5 = (ConstraintLayout) findViewById(R.id.Room5);
		Room6 = (ConstraintLayout) findViewById(R.id.Room6);
		Room7 = (ConstraintLayout) findViewById(R.id.Room7);
		Room8 = (ConstraintLayout) findViewById(R.id.Room8);
		Room9 = (ConstraintLayout) findViewById(R.id.Room9);
		Room10 = (ConstraintLayout) findViewById(R.id.Room10);
		Room1Name = (TextView) findViewById(R.id.Room1Name);
		Room2Name = (TextView) findViewById(R.id.Room2Name);
		Room3Name = (TextView) findViewById(R.id.Room3Name);
		Room4Name = (TextView) findViewById(R.id.Room4Name);
		Room5Name = (TextView) findViewById(R.id.Room5Name);
		Room6Name = (TextView) findViewById(R.id.Room6Name);
		Room7Name = (TextView) findViewById(R.id.Room7Name);
		Room8Name = (TextView) findViewById(R.id.Room8Name);
		Room9Name = (TextView) findViewById(R.id.Room9Name);
		Room10Name = (TextView) findViewById(R.id.Room10Name);
		Room1Players = (TextView) findViewById(R.id.Room1Players);
		Room2Players = (TextView) findViewById(R.id.Room2Players);
		Room3Players = (TextView) findViewById(R.id.Room3Players);
		Room4Players = (TextView) findViewById(R.id.Room4Players);
		Room5Players = (TextView) findViewById(R.id.Room5Players);
		;
		Room6Players = (TextView) findViewById(R.id.Room6Players);
		Room7Players = (TextView) findViewById(R.id.Room7Players);
		Room8Players = (TextView) findViewById(R.id.Room8Players);
		Room9Players = (TextView) findViewById(R.id.Room9Players);
		Room10Players = (TextView) findViewById(R.id.Room10Players);
		CreateRoomButton = (Button) findViewById(R.id.CreateRoomButton);
		CreateRoomField = (EditText) findViewById(R.id.CreateRoomField);
		LockButton = (ImageView) findViewById(R.id.LockButton);
		FindRoomButton = (Button) findViewById(R.id.FindRoomButton);
		//endregion

		Thing();

		FindRoomButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				RoomName = CreateRoomField.getText().toString();

				database.child(RoomName).addListenerForSingleValueEvent(new ValueEventListener()
				{
					@Override
					public void onDataChange(final DataSnapshot dataSnapshot)
					{
						try
						{
							String s = dataSnapshot.getValue().toString();

							if (dataSnapshot.child("Pass").getValue().toString().compareTo("0") != 0)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
								alert.setTitle(getString(R.string.EnterPass));
								final EditText input = new EditText(MenuActivity.this);
								alert.setView(input);
								alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int whichButton)
									{
										if (input.getText().toString().length() >= 3)
											pass = input.getText().toString();
										else
											pass = "0";

										if (dataSnapshot.child("Pass").getValue().toString().compareTo(pass) == 0)
										{
											if (Integer.valueOf(dataSnapshot.child("Turns").getValue().toString()) <= Integer.valueOf(dataSnapshot.child("ConnectedPlayers").getValue().toString()))
												startActivity(new Intent(MenuActivity.this, MainActivity.class));
											else
												Toast.makeText(MenuActivity.this, getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
										} else
											Toast.makeText(MenuActivity.this, getString(R.string.WrongPassword), Toast.LENGTH_SHORT).show();
									}
								});
								alert.setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog, int whichButton) { }
								});
								alert.show();
							} else
							{
								if (Integer.valueOf(dataSnapshot.child("Turns").getValue().toString()) <= Integer.valueOf(dataSnapshot.child("ConnectedPlayers").getValue().toString()))
									startActivity(new Intent(MenuActivity.this, MainActivity.class));
								else
									Toast.makeText(MenuActivity.this, getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e)
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
							builder.setTitle(getString(R.string.FindRoomLabelText));
							builder.setMessage(getString(R.string.FindRoomDialog));
							builder.setCancelable(true);
							builder.setPositiveButton(getString(R.string.CreateRoom), new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									Random rnd = new Random();
									if (RoomName.compareTo("") == 0)
										RoomName = "Room" + rnd.nextInt();

									database.child(RoomName).child("Card").setValue(0);
									database.child(RoomName).child("Color").setValue(0);
									database.child(RoomName).child("ConnectedPlayers").setValue(0);
									database.child(RoomName).child("CurrentPlayer").setValue(1);
									database.child(RoomName).child("MaxDraw").setValue(1);
									database.child(RoomName).child("NewCard").setValue(0);
									database.child(RoomName).child("TurnDir").setValue(1);
									database.child(RoomName).child("Msg").setValue(0);
									database.child(RoomName).child("Winner").setValue(0);
									if (pass.length() >= 3)
										database.child(RoomName).child("Pass").setValue(pass);
									else
										database.child(RoomName).child("Pass").setValue(0);

									startActivity(new Intent(MenuActivity.this, MainActivity.class));
								}
							});
							builder.setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
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

		LockButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);

				alert.setTitle(getString(R.string.EnterPass));
				alert.setMessage(getString(R.string.SizePassword));

				final EditText input = new EditText(MenuActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if (input.getText().toString().length() >= 3)
							pass = input.getText().toString();
						else
							pass = "0";
					}
				});

				alert.setNegativeButton(getString(R.string.FindRoomDialogCancel), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton) { }
				});

				alert.show();
			}
		});

		CreateRoomButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				RoomName = CreateRoomField.getText().toString();

				database.child(RoomName).addListenerForSingleValueEvent(new ValueEventListener()
				{
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						try
						{
							String s = dataSnapshot.getValue().toString();

							AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
							alert.setTitle(getString(R.string.CreateRoomLabelText));
							alert.setMessage(getString(R.string.RoomAlive));
							alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int whichButton) { }
							});
							alert.show();

						} catch (Exception e)
						{
							Random rnd = new Random();
							if (RoomName.compareTo("") == 0) RoomName = "Room" + rnd.nextInt();

							database.child(RoomName).child("Card").setValue(0);
							database.child(RoomName).child("Color").setValue(0);
							database.child(RoomName).child("ConnectedPlayers").setValue(0);
							database.child(RoomName).child("CurrentPlayer").setValue(1);
							database.child(RoomName).child("MaxDraw").setValue(1);
							database.child(RoomName).child("NewCard").setValue(0);
							database.child(RoomName).child("TurnDir").setValue(1);
							database.child(RoomName).child("Msg").setValue(0);
							database.child(RoomName).child("Winner").setValue(0);
							database.child(RoomName).child("Turns").setValue(0);
							if (pass.length() >= 3)
								database.child(RoomName).child("Pass").setValue(pass);
							else
								database.child(RoomName).child("Pass").setValue(0);

							startActivity(new Intent(MenuActivity.this, MainActivity.class));
						}
					}

					@Override
					public void onCancelled(DatabaseError databaseError) { }
				});
			}
		});

		Room1.setOnClickListener(RoomClick);
		Room2.setOnClickListener(RoomClick);
		Room3.setOnClickListener(RoomClick);
		Room4.setOnClickListener(RoomClick);
		Room5.setOnClickListener(RoomClick);
		Room6.setOnClickListener(RoomClick);
		Room7.setOnClickListener(RoomClick);
		Room8.setOnClickListener(RoomClick);
		Room9.setOnClickListener(RoomClick);
		Room10.setOnClickListener(RoomClick);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.bug_report:
				startActivity(new Intent(MenuActivity.this, BugReportActivity.class));
				return true;

			case R.id.VideoSettings:
				Animation = !Animation;
				if (Animation)
					Toast.makeText(this, "Анимации включены", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "Анимации выключены", Toast.LENGTH_SHORT).show();
				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}
}
