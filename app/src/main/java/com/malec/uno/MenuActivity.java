package com.malec.uno;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MenuActivity extends AppCompatActivity
{
	DatabaseReference database = FirebaseDatabase.getInstance().getReference();

	ImageView LockButton;

	Button CreateRoomButton, FindRoomButton;
	EditText CreateRoomField;

	public static String RoomName = "";
	public static String ClickRoomName = "";

	public static Boolean Animation = true, CreateRoom = false;

	String pass = "0";

	List<RoomClass> Rooms = new ArrayList<>();

	ChildEventListener childlistener;

	Random rnd = new Random();
	@Override
	protected void onResume()
	{
		super.onResume();

		((RecyclerView)(findViewById(R.id.RoomList))).getAdapter().notifyDataSetChanged();
		Thing();
	}

	RecyclerView.OnItemTouchListener ItemTouchListener = new RecyclerView.OnItemTouchListener()
	{
		@Override
		public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
		{
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					if (!CreateRoom)
						for (Integer i = 0; i < Rooms.size(); i++)
						{
							if (Rooms.get(i).getName().compareTo(ClickRoomName) == 0)
							{
								final String CurPass = Rooms.get(i).getPass();
								final Integer Players = Integer.valueOf(Rooms.get(i).getPlayers().split(" ")[2]);
								final Integer Turns = Integer.valueOf(Rooms.get(i).getTurns());

								String IMEI = "";

								if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
								{
									ActivityCompat.requestPermissions(MenuActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);

									TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
									IMEI = tm.getDeviceId();
								}else {
									TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
									IMEI = tm.getDeviceId();
								}

								if (CurPass.compareTo("0") == 0)
								{
									if (Turns <= Players)
									{
										if (IMEI.compareTo("") != 0)
										{
											for (int ind = 2; ind <= Players; ind++)
											{
												final String finalIMEI = IMEI;
												if (finalIMEI.compareTo(Rooms.get(i).getPlayerIMEI(ind - 2)) == 0)
												{
													CreateRoom = true;
													MainActivity.Reconnect = ind;
													startActivity(new Intent(MenuActivity.this, MainActivity.class));
													break;
												}
											}
											CreateRoom = true;
											startActivity(new Intent(MenuActivity.this, MainActivity.class));
											break;
										}
									} else
									{
										if (IMEI.compareTo("") != 0)
										{
											for (int ind = 2; ind <= Players; ind++)
											{
												final String finalIMEI = IMEI;
												if (finalIMEI.compareTo(Rooms.get(i).getPlayerIMEI(ind - 2)) == 0)
												{
													CreateRoom = true;
													MainActivity.Reconnect = ind;
													startActivity(new Intent(MenuActivity.this, MainActivity.class));
													break;
												}
											}
											CreateRoom = true;
											startActivity(new Intent(MenuActivity.this, MainActivity.class));
											break;
										}else
											Toast.makeText(MenuActivity.this, getString(R.string.GameIsRunning), Toast.LENGTH_SHORT).show();
									}
								} else
								{
									AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);

									alert.setTitle(getString(R.string.EnterPass));

									final EditText input = new EditText(MenuActivity.this);
									alert.setView(input);

									final String finalIMEI1 = IMEI;
									final Integer finalI = i;
									alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
									{
										public void onClick(DialogInterface dialog, int whichButton)
										{
											if (input.getText().toString().compareTo(CurPass) == 0)
											{
												if (Turns <= Players)
												{
													if (finalIMEI1.compareTo("") != 0)
													{
														for (int ind = 2; ind <= Players; ind++)
														{
															final String finalIMEI = finalIMEI1;
															if (finalIMEI.compareTo(Rooms.get(finalI).getPlayerIMEI(ind - 2)) == 0)
															{
																CreateRoom = true;
																MainActivity.Reconnect = ind;
																startActivity(new Intent(MenuActivity.this, MainActivity.class));
																break;
															}
														}
														CreateRoom = true;
														startActivity(new Intent(MenuActivity.this, MainActivity.class));
													}
												} else
													if (finalIMEI1.compareTo("") != 0)
													{
														for (int ind = 2; ind <= Players; ind++)
														{
															final String finalIMEI = finalIMEI1;
															if (finalIMEI.compareTo(Rooms.get(finalI).getPlayerIMEI(ind - 2)) == 0)
															{
																CreateRoom = true;
																MainActivity.Reconnect = ind;
																startActivity(new Intent(MenuActivity.this, MainActivity.class));
																break;
															}
														}
														CreateRoom = true;
														startActivity(new Intent(MenuActivity.this, MainActivity.class));
													}else
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
								}
							}
						}
				}
			}, 100);

			return false;
		}

		@Override
		public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

		@Override
		public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
	};

	void Thing()
	{
		final RecyclerView[] recyclerView = new RecyclerView[1];
		final RoomDataAdapter adapter = new RoomDataAdapter(getApplicationContext(), Rooms);
		recyclerView[0] = (RecyclerView) findViewById(R.id.RoomList);
		recyclerView[0].setAdapter(adapter);

		childlistener = database.addChildEventListener(new ChildEventListener()
		{
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					if (!CreateRoom)
					{
						String RoomName = dataSnapshot.getKey().toString();
						String RoomDate = dataSnapshot.child("Date").getValue().toString();
						String RoomPlayers = dataSnapshot.child("ConnectedPlayers").getValue().toString();
						String RoomPass = dataSnapshot.child("Pass").getValue().toString();
						String RoomTurns = dataSnapshot.child("Turns").getValue().toString();

						Integer m = Calendar.getInstance().get(Calendar.MINUTE);

						int norm = 0;
						for (Integer i = 0; i < Rooms.size(); i++)
							if (Rooms.get(i).getName().compareTo(RoomName) == 0)
								norm++;

						if (norm == 0)
						{
							if (m - Integer.valueOf(RoomDate) >= 10 || (m - Integer.valueOf(RoomDate) >= -50 && m - Integer.valueOf(RoomDate) < 0))
								database.child(dataSnapshot.getKey().toString()).removeValue();
							else
							{
								List<RoomClass.Player> Pls = new ArrayList<>();
								for (Integer ind = 2; ind <= Integer.valueOf(RoomPlayers); ind ++)
								{
									String IMEI = dataSnapshot.child("Player-" + ind).child("IMEI").getValue().toString();
									Pls.add(new RoomClass.Player(IMEI, ind.toString()));
								}
								Rooms.add(new RoomClass(RoomName, RoomPass, getString(R.string.TotalPlayers) + " " + RoomPlayers, RoomTurns, Pls));

								recyclerView[0].getAdapter().notifyDataSetChanged();
								recyclerView[0].addOnItemTouchListener(ItemTouchListener);
							}
						}
					}
				} catch (Exception e)
				{
					if (!CreateRoom)
					{
						Log.e("onChildAdded", e.toString());

						String RoomName = dataSnapshot.getKey().toString();
						if (RoomName.compareTo("Msg") != 0)
							database.child(dataSnapshot.getKey().toString()).removeValue();
					}
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
					else
						for (Integer i = 0; i < Rooms.size(); i++)
							if (Rooms.get(i).getName().compareTo(RoomName) == 0)
							{
								Rooms.get(i).setName(RoomName);
								Rooms.get(i).setPass(RoomPass);
								Rooms.get(i).setPlayers(getString(R.string.TotalPlayers) + " " + RoomPlayers);

								recyclerView[0].getAdapter().notifyDataSetChanged();
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

					for (Integer i = 0; i < Rooms.size(); i++)
						if (Rooms.get(i).getName().compareTo(RoomName) == 0)
						{
							Rooms.remove(i);

							recyclerView[0].getAdapter().notifyDataSetChanged();
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

		//region Проверка новой версии
		String HTML = null;
		InternetRequest htm = new InternetRequest();
		htm.execute("https://raw.githubusercontent.com/Malez228/UNO/Recycler/app/build.gradle");
		try { HTML = htm.get().toString(); } catch (Exception e){ }
		htm.cancel(true);
		String HTMLVersion = HTML.split("versionName \"")[1].split("\"")[0];
		String Version = "";
		try
		{
			PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			Version = pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) { }
		if (Version.compareTo(HTMLVersion) != 0)
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(MenuActivity.this);
			alert.setTitle("New version available").setNegativeButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
			}).setPositiveButton("Download", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialogInterface, int i)
				{
					Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Malez228/UNO/raw/Recycler/app/release/app-release.apk"));
					startActivity(browseIntent);
				}
			});
			alert.show();
		}
		//endregion

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
											{
												ClickRoomName = RoomName;
												startActivity(new Intent(MenuActivity.this, MainActivity.class));
											}
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
								{
									ClickRoomName = RoomName;
									startActivity(new Intent(MenuActivity.this, MainActivity.class));
								}
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

									ClickRoomName = RoomName;
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

				if (RoomName.compareTo("") == 0)
					RoomName = "Room" + rnd.nextInt();

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
							Log.e("gavno", "jopa 1");
							database.removeEventListener(childlistener);
							CreateRoom = true;
							Log.e("gavno", "jopa 2");

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

							ClickRoomName = RoomName;
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
