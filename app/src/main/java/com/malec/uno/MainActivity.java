package com.malec.uno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
	ImageView LeftCard0, LeftCard1, CenterCard, RightCard1, RightCard0;
	ImageView Deck, CurrentCard;
	TextView PlayerTurn;

	List<String> HandCards = new ArrayList();
	List<String> Cards = new ArrayList();

	Integer CardNum = 0;

	Boolean Logined = false, Turn = false, TurnShow = false;

	Integer StartPosX, StartPosY;

	Random rnd = new Random();

	Integer Player = 0;

	public static String CurrentCardV;

	DatabaseReference database = FirebaseDatabase.getInstance().getReference();

	int GetCardImage(String name)
	{
		switch (name)
		{
			case "RED 0":
				return R.drawable.red0;
			case "RED 1":
				return R.drawable.red1;
			case "RED 2":
				return R.drawable.red2;
			case "RED 3":
				return R.drawable.red3;
			case "RED 4":
				return R.drawable.red4;
			case "RED 5":
				return R.drawable.red5;
			case "RED 6":
				return R.drawable.red6;
			case "RED 7":
				return R.drawable.red7;
			case "RED 8":
				return R.drawable.red8;
			case "RED 9":
				return R.drawable.red9;
			case "RED $":
				return R.drawable.red10;
			case "RED @":
				return R.drawable.red11;
			case "RED ^":
				return R.drawable.red12;

			case "YELLOW 0":
				return R.drawable.yellow0;
			case "YELLOW 1":
				return R.drawable.yellow1;
			case "YELLOW 2":
				return R.drawable.yellow2;
			case "YELLOW 3":
				return R.drawable.yellow3;
			case "YELLOW 4":
				return R.drawable.yellow4;
			case "YELLOW 5":
				return R.drawable.yellow5;
			case "YELLOW 6":
				return R.drawable.yellow6;
			case "YELLOW 7":
				return R.drawable.yellow7;
			case "YELLOW 8":
				return R.drawable.yellow8;
			case "YELLOW 9":
				return R.drawable.yellow9;
			case "YELLOW $":
				return R.drawable.yellow10;
			case "YELLOW @":
				return R.drawable.yellow11;
			case "YELLOW ^":
				return R.drawable.yellow12;

			case "GREEN 0":
				return R.drawable.green0;
			case "GREEN 1":
				return R.drawable.green1;
			case "GREEN 2":
				return R.drawable.green2;
			case "GREEN 3":
				return R.drawable.green3;
			case "GREEN 4":
				return R.drawable.green4;
			case "GREEN 5":
				return R.drawable.green5;
			case "GREEN 6":
				return R.drawable.green6;
			case "GREEN 7":
				return R.drawable.green7;
			case "GREEN 8":
				return R.drawable.green8;
			case "GREEN 9":
				return R.drawable.green9;
			case "GREEN $":
				return R.drawable.green10;
			case "GREEN @":
				return R.drawable.green11;
			case "GREEN ^":
				return R.drawable.green12;

			case "BLUE 0":
				return R.drawable.blue0;
			case "BLUE 1":
				return R.drawable.blue1;
			case "BLUE 2":
				return R.drawable.blue2;
			case "BLUE 3":
				return R.drawable.blue3;
			case "BLUE 4":
				return R.drawable.blue4;
			case "BLUE 5":
				return R.drawable.blue5;
			case "BLUE 6":
				return R.drawable.blue6;
			case "BLUE 7":
				return R.drawable.blue7;
			case "BLUE 8":
				return R.drawable.blue8;
			case "BLUE 9":
				return R.drawable.blue9;
			case "BLUE $":
				return R.drawable.blue10;
			case "BLUE @":
				return R.drawable.blue11;
			case "BLUE ^":
				return R.drawable.blue12;

			case "BLACK #":
				return R.drawable.black1;
			case "BLACK +":
				return R.drawable.black2;

			default:
				return R.drawable.empty_card;
		}
	}

	void DrawHand()
	{
		try
		{
			LeftCard0.setImageResource(GetCardImage(HandCards.get(CardNum + 4)));
		} catch (Exception e) { LeftCard0.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			LeftCard1.setImageResource(GetCardImage(HandCards.get(CardNum + 3)));
		} catch (Exception e) { LeftCard1.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			CenterCard.setImageResource(GetCardImage(HandCards.get(CardNum + 2)));
		} catch (Exception e) { CenterCard.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			RightCard1.setImageResource(GetCardImage(HandCards.get(CardNum + 1)));
		} catch (Exception e) { RightCard1.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			RightCard0.setImageResource(GetCardImage(HandCards.get(CardNum)));
		} catch (Exception e) { RightCard0.setImageResource(GetCardImage("EmptyCard")); }


		try { CurrentCard.setImageResource(GetCardImage(CurrentCardV)); } catch (Exception e)
		{
			CurrentCard.setImageResource(GetCardImage("EmptyCard"));
		}
	}

	//region OnTochListener
	private View.OnTouchListener myListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			try
			{
				if (Turn)
				{
					int x = (int) motionEvent.getX();
					int y = (int) motionEvent.getY();

					switch (motionEvent.getAction())
					{
						case MotionEvent.ACTION_DOWN:
							StartPosX = x;
							StartPosY = y;
							break;
						case MotionEvent.ACTION_MOVE:
							Log.i("TAG", "Moving: (" + x + ", " + y + ")" + " Start: (" + StartPosX + ", " + StartPosY + ")");
							break;
						case MotionEvent.ACTION_UP:
							if (y + StartPosY <= -400)
							{
								ImageView Card = (ImageView) view;

								if (Card.getId() == LeftCard0.getId())
								{
									if (HandCards.get(CardNum + 4).split(" ")[0].contains(CurrentCardV.split(" ")[0]) || HandCards.get(CardNum + 4).split(" ")[1].contains(CurrentCardV.split(" ")[1]))
									{
										database.child("Card").setValue(HandCards.get(CardNum + 4));
										HandCards.remove(CardNum + 4);
										database.child("CurrentPlayer").setValue(Player + 1);
									}
								}
								if (Card.getId() == LeftCard1.getId())
								{
									if (HandCards.get(CardNum + 3).split(" ")[0].contains(CurrentCardV.split(" ")[0]) || HandCards.get(CardNum + 3).split(" ")[1].contains(CurrentCardV.split(" ")[1]))
									{
										database.child("Card").setValue(HandCards.get(CardNum + 3));
										HandCards.remove(CardNum + 3);
										database.child("CurrentPlayer").setValue(Player + 1);
									}
								}
								if (Card.getId() == CenterCard.getId())
								{
									if (HandCards.get(CardNum + 2).split(" ")[0].contains(CurrentCardV.split(" ")[0]) || HandCards.get(CardNum + 2).split(" ")[1].contains(CurrentCardV.split(" ")[1]))
									{
										database.child("Card").setValue(HandCards.get(CardNum + 2));
										HandCards.remove(CardNum + 2);
										database.child("CurrentPlayer").setValue(Player + 1);
									}
								}
								if (Card.getId() == RightCard1.getId())
								{
									if (HandCards.get(CardNum + 1).split(" ")[0].contains(CurrentCardV.split(" ")[0]) || HandCards.get(CardNum + 1).split(" ")[1].contains(CurrentCardV.split(" ")[1]))
									{
										database.child("Card").setValue(HandCards.get(CardNum + 1));
										HandCards.remove(CardNum + 1);
										database.child("CurrentPlayer").setValue(Player + 1);
									}
								}
								if (Card.getId() == RightCard0.getId())
								{
									if (HandCards.get(CardNum + 0).split(" ")[0].contains(CurrentCardV.split(" ")[0]) || HandCards.get(CardNum + 0).split(" ")[1].contains(CurrentCardV.split(" ")[1]))
									{
										database.child("Card").setValue(HandCards.get(CardNum + 0));
										HandCards.remove(CardNum + 0);
										database.child("CurrentPlayer").setValue(Player + 1);
									}
								}

								//TODO проверку если на столе нет карты
								if (CardNum > 0) CardNum--;

								DrawHand();
							}
							break;
					}
				}
				else
				{
					if (TurnShow)
					{
						Toast.makeText(MainActivity.this, "Сейчас не ваш ход", Toast.LENGTH_SHORT).show();
						TurnShow = false;
					}
				}
			} catch (Exception e)
			{
				Log.i("TAG", e.toString());
			}

			return false;
		}
	};
	//endregion

	void GenerateCards()
	{
		Cards.add("YELLOW 0");
		Cards.add("RED 0");
		Cards.add("BLUE 0");
		Cards.add("GREEN 0");

		for (int i = 1; i < 10; i++)
			Cards.add("YELLOW " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("YELLOW " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("RED " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("RED " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("BLUE " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("BLUE " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("GREEN " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("GREEN " + i);

		Cards.add("YELLOW @");
		Cards.add("YELLOW @");
		Cards.add("RED @");
		Cards.add("RED @");
		Cards.add("BLUE @");
		Cards.add("BLUE @");
		Cards.add("GREEN @");
		Cards.add("GREEN @");

		Cards.add("YELLOW ^");
		Cards.add("YELLOW ^");
		Cards.add("RED ^");
		Cards.add("RED ^");
		Cards.add("BLUE ^");
		Cards.add("BLUE ^");
		Cards.add("GREEN ^");
		Cards.add("GREEN ^");

		Cards.add("YELLOW $");
		Cards.add("YELLOW $");
		Cards.add("RED $");
		Cards.add("RED $");
		Cards.add("BLUE $");
		Cards.add("BLUE $");
		Cards.add("GREEN $");
		Cards.add("GREEN $");

		for (int i = 0; i < 4; i++)
			Cards.add("BLACK #");
		for (int i = 0; i < 4; i++)
			Cards.add("BLACK +");
	}

	void PlayerInitialization()
	{
		database.child("ConnectedPlayers").addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				if (!Logined)
				{
					Integer ConnectedPlayers = Integer.valueOf(dataSnapshot.toString().split(" value = ")[1].split(" ")[0]);
					Player = ConnectedPlayers + 1;
					database.child("ConnectedPlayers").setValue(ConnectedPlayers + 1);

					Logined = true;
				}
			}

			@Override
			public void onCancelled(DatabaseError error)
			{ Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show(); }
		});
	}

	void GetCurrentCard()
	{
		database.child("Card").addValueEventListener(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				String s = dataSnapshot.toString().split(" value = ")[1];
				CurrentCardV = s.substring(0, s.length() - 2);

				DrawHand();
			}

			@Override
			public void onCancelled(DatabaseError error)
			{ Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show(); }
		});
	}

	void SetTurnListener()
	{
		database.child("CurrentPlayer").addValueEventListener(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				String CplayerS = dataSnapshot.toString().split(" value = ")[1];
				Integer Cplayer = Integer.valueOf(CplayerS.substring(0, CplayerS.length() - 2));


				if (Player == Cplayer)
				{
					Turn = true;
					TurnShow = true;
					PlayerTurn.setText("Ваш ход");
				}
				else
				{
					Turn = false;
					PlayerTurn.setText("Ход игрока " + Cplayer.toString());
				}
			}

			@Override
			public void onCancelled(DatabaseError error)
			{ Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show(); }
		});
	}

	void SetTopDeckListener()
	{
		Deck.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child("NewCard").addListenerForSingleValueEvent(new ValueEventListener()
				{
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						String NewCard = dataSnapshot.toString().split(" value = ")[1];
						NewCard = NewCard.substring(0, NewCard.length() - 2);
						HandCards.add(NewCard);
						DrawHand();
						database.child("NewCard").setValue(0);
					}

					@Override
					public void onCancelled(DatabaseError error)
					{ Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show(); }
				});
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//region Инициализация
		LeftCard0 = (ImageView) findViewById(R.id.LeftCard0);
		LeftCard1 = (ImageView) findViewById(R.id.LeftCard1);
		CenterCard = (ImageView) findViewById(R.id.CenterCard);
		RightCard1 = (ImageView) findViewById(R.id.RightCard1);
		RightCard0 = (ImageView) findViewById(R.id.RightCard0);
		Deck = (ImageView) findViewById(R.id.Deck);
		CurrentCard = (ImageView) findViewById(R.id.CurrentCard);
		PlayerTurn = (TextView) findViewById(R.id.PlayerTurn);
		//endregion

		PlayerInitialization();

		GetCurrentCard();

		DrawHand();

		SetTurnListener();

		SetTopDeckListener();

		//region Listeners
		LeftCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardNum + 1 <= HandCards.size() - 5) CardNum++;
				DrawHand();
			}
		});

		RightCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardNum - 1 >= 0) CardNum--;
				DrawHand();
			}
		});

		RightCard1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view) { }
		});

		LeftCard1.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view) { }
		});

		CenterCard.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view) { }
		});

		LeftCard0.setOnTouchListener(myListener);
		LeftCard1.setOnTouchListener(myListener);
		CenterCard.setOnTouchListener(myListener);
		RightCard1.setOnTouchListener(myListener);
		RightCard0.setOnTouchListener(myListener);
		//endregion
	}
}
