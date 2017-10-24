package com.malec.uno;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Calendar;
import java.util.Random;

public class MenuActivity extends AppCompatActivity
{
	DatabaseReference database = FirebaseDatabase.getInstance().getReference();

	ImageView LockButton;

	Button CreateRoomButton, FindRoomButton;
	EditText CreateRoomField;

	public static String RoomName = "";

	public static Boolean Animation = true;

	String pass = "0";

	ChildEventListener childlistener;

	protected View.OnClickListener RoomClick = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			LinearLayout Room = (LinearLayout) view;

			RoomName = ((TextView) (((LinearLayout) (Room.getChildAt(0))).getChildAt(0))).getText().toString();

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
		Thing();
	}

	void Thing()
	{
		final LinearLayout RoomListLayout = (LinearLayout) findViewById(R.id.RoomListLayout);

		childlistener = database.addChildEventListener(new ChildEventListener()
		{
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					String RoomName = dataSnapshot.getKey().toString();
					String RoomDate = dataSnapshot.child("Date").getValue().toString();
					String RoomPlayers = dataSnapshot.child("ConnectedPlayers").getValue().toString();
					String RoomPass = dataSnapshot.child("Pass").getValue().toString();

					Calendar c = Calendar.getInstance();
					Integer m = c.get(Calendar.MINUTE);
					if (m - Integer.valueOf(RoomDate) >= 10)
						database.child(dataSnapshot.getKey().toString()).removeValue();

					int norm = 0;
					for (Integer i = 0; i < RoomListLayout.getChildCount(); i++)
						for (Integer j = 0; j < RoomListLayout.getChildCount(); j++)
						{
							LinearLayout ll1 = (LinearLayout) (RoomListLayout.getChildAt(i)), ll2;
							TextView tv1;
							try
							{
								ll2 = (LinearLayout) (ll1).getChildAt(0);
								tv1 = (TextView) (ll2).getChildAt(0);
							} catch (Exception e)
							{
								ll2 = (LinearLayout) (ll1).getChildAt(1);
								tv1 = (TextView) (ll2).getChildAt(0);
							}

							String RoomsRoomName = (tv1).getText().toString();

							if (RoomName.compareTo(RoomsRoomName) == 0)
								norm++;
						}

					if (norm == 0)
					{
						LinearLayout NewRoom = new LinearLayout(RoomListLayout.getContext());
						NewRoom.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						layoutParams.setMargins(0, 4, 0, 4);
						NewRoom.setPadding(4, 4, 4, 4);
						NewRoom.setLayoutParams(layoutParams);
						NewRoom.setBackgroundColor(getResources().getColor(R.color.backL));

						LinearLayout InnerLayout = new LinearLayout(RoomListLayout.getContext());
						InnerLayout.setOrientation(LinearLayout.VERTICAL);
						layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
						layoutParams.setMargins(8, 0, 8, 0);
						InnerLayout.setLayoutParams(layoutParams);

						TextView NewRoomName = new TextView(InnerLayout.getContext());
						NewRoomName.setText(RoomName);
						NewRoomName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
						NewRoomName.setTextSize(16);
						InnerLayout.addView(NewRoomName);

						TextView NewRoomPlayers = new TextView(InnerLayout.getContext());
						NewRoomPlayers.setText(getString(R.string.TotalPlayers) + " " + RoomPlayers);
						NewRoomPlayers.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
						NewRoomPlayers.setTextSize(16);
						InnerLayout.addView(NewRoomPlayers);

						ImageView NewRoomLock = new ImageView(NewRoom.getContext());
						NewRoomLock.setImageResource(R.drawable.lock);
						LinearLayout.LayoutParams wh = new LinearLayout.LayoutParams(80, 80);
						NewRoomLock.setLayoutParams(wh);
						if (RoomPass.compareTo("0") == 0)
							NewRoomLock.setVisibility(View.GONE);
						else
							NewRoomLock.setVisibility(View.VISIBLE);
						NewRoom.addView(NewRoomLock);

						NewRoom.addView(InnerLayout);

						NewRoom.setOnClickListener(RoomClick);
						RoomListLayout.addView(NewRoom);
					}
				} catch (Exception e)
				{
					Log.e("onChildAdded", e.toString());
				}
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					String RoomName = dataSnapshot.getKey().toString();
					String RoomDate = dataSnapshot.child("Date").getValue().toString();
					String RoomPlayers = dataSnapshot.child("ConnectedPlayers").getValue().toString();
					String RoomPass = dataSnapshot.child("Pass").getValue().toString();

					Calendar c = Calendar.getInstance();
					Integer m = c.get(Calendar.MINUTE);
					if (m - Integer.valueOf(RoomDate) >= 10)
						database.child(dataSnapshot.getKey().toString()).removeValue();

					for (Integer i = 0; i < RoomListLayout.getChildCount(); i++)
					{
						LinearLayout ll1 = (LinearLayout) (RoomListLayout.getChildAt(i)), ll2;
						TextView tv1, tv2;
						ImageView im;
						try
						{
							ll2 = (LinearLayout) (ll1).getChildAt(0);
							tv1 = (TextView) (ll2).getChildAt(0);
							tv2 = (TextView) (ll2).getChildAt(1);
							im = (ImageView) (ll1).getChildAt(1);
						} catch (Exception e)
						{
							ll2 = (LinearLayout) (ll1).getChildAt(1);
							tv1 = (TextView) (ll2).getChildAt(0);
							tv2 = (TextView) (ll2).getChildAt(1);
							im = (ImageView) (ll1).getChildAt(0);
						}
						String EventRoomName = (tv1).getText().toString();

						if (EventRoomName.compareTo(RoomName) == 0)
						{
							tv1.setText(RoomName);
							tv2.setText(getString(R.string.TotalPlayers) + " " + RoomPlayers);

							if (RoomPass.compareTo("0") == 0)
								im.setVisibility(View.GONE);
							else
								im.setVisibility(View.VISIBLE);
							break;
						}
					}
				} catch (Exception e)
				{
					Log.e("onChildChanged", e.toString());
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot)
			{
				try
				{
					String RoomName = dataSnapshot.getKey().toString();

					for (Integer i = 0; i < RoomListLayout.getChildCount(); i++)
					{
						LinearLayout ll1 = (LinearLayout) (RoomListLayout.getChildAt(i)), ll2;
						TextView tv1, tv2;
						ImageView im;
						try
						{
							ll2 = (LinearLayout) (ll1).getChildAt(0);
							tv1 = (TextView) (ll2).getChildAt(0);
						} catch (Exception e)
						{
							ll2 = (LinearLayout) (ll1).getChildAt(1);
							tv1 = (TextView) (ll2).getChildAt(0);
						}
						String EventRoomName = (tv1).getText().toString();

						if (EventRoomName.compareTo(RoomName) == 0)
						{
							RoomListLayout.removeView(ll1);

							break;
						}
					}
				} catch (Exception e)
				{
					Log.e("onChildChanged", e.toString());
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
									Calendar c = Calendar.getInstance();
									Integer mm = c.get(Calendar.MINUTE);
									database.child(RoomName).child("Date").setValue(mm);
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
							database.removeEventListener(childlistener);

							Random rnd = new Random();
							if (RoomName.compareTo("") == 0)
								RoomName = "Room" + rnd.nextInt();

							Calendar c = Calendar.getInstance();
							Integer mm = c.get(Calendar.MINUTE);
							database.child(RoomName).child("Date").setValue(mm);
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
