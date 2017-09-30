package com.malec.uno;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
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
	TextView PlayerTurn, HandCardsCount, ColorView;

	ConstraintLayout WildChoice;

	RadioButton RadioRed, RadioYellow, RadioGreen, RadioBlue;

	Button SubmitRadio, GiveTurn;

	List<String> HandCards = new ArrayList();
	List<String> Cards = new ArrayList();

	String BaseCard, BaseColor, BaseConnectedPlayers, BaseCurrentPlayer, BaseMaxDraw, BaseNewCard, BaseTurnDir;

	Integer CardOffset = 0;

	Integer StartPosX, StartPosY;

	Random rnd = new Random();

	static Integer Player = 0;

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
			LeftCard0.setImageResource(GetCardImage(HandCards.get(CardOffset + 4)));
		} catch (Exception e) { LeftCard0.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			LeftCard1.setImageResource(GetCardImage(HandCards.get(CardOffset + 3)));
		} catch (Exception e) { LeftCard1.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			CenterCard.setImageResource(GetCardImage(HandCards.get(CardOffset + 2)));
		} catch (Exception e) { CenterCard.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			RightCard1.setImageResource(GetCardImage(HandCards.get(CardOffset + 1)));
		} catch (Exception e) { RightCard1.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			RightCard0.setImageResource(GetCardImage(HandCards.get(CardOffset)));
		} catch (Exception e) { RightCard0.setImageResource(GetCardImage("EmptyCard")); }


		try { CurrentCard.setImageResource(GetCardImage(BaseCard)); } catch (Exception e)
		{
			CurrentCard.setImageResource(GetCardImage("EmptyCard"));
		}

		try
		{
			HandCardsCount.setText("Карт в руке " + HandCards.size());
		} catch (Exception e)
		{
			HandCardsCount.setText("Карт в руке 0");
		}
	}

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

	//region OnTouchListener
	private View.OnTouchListener myListener = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
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
						Log.i("MotionEvent.ACTION_MOVE", "Moving: (" + x + ", " + y + ")" + " Start: (" + StartPosX + ", " + StartPosY + ")");
						break;
					case MotionEvent.ACTION_UP:
						if (y + StartPosY <= -400)
						{
							ImageView Card = (ImageView) view;

							Integer Offset = 0;
							if (Card.getId() == LeftCard0.getId()) Offset = 4;
							if (Card.getId() == LeftCard1.getId()) Offset = 3;
							if (Card.getId() == CenterCard.getId()) Offset = 2;
							if (Card.getId() == RightCard1.getId()) Offset = 1;

							String HandColor = HandCards.get(CardOffset + Offset).split(" ")[0];
							String BoardColor = BaseCard.split(" ")[0];
							String HandType = HandCards.get(CardOffset + Offset).split(" ")[1];
							String BoardType = BaseCard.split(" ")[1];

							if (HandColor.compareTo(BoardColor) == 0 || HandType.compareTo(BoardType) == 0 || HandColor.compareTo(BaseColor) == 0 || HandColor.compareTo("BLACK") == 0)
							{
								if (HandColor.compareTo("BLACK") == 0)
								{
									WildChoice.setVisibility(View.VISIBLE);
								} else
								{
									Integer SkipTurn = 1;
									if (HandType.compareTo("@") == 0) SkipTurn = 2;
									if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) > Integer.valueOf(BaseConnectedPlayers))
										database.child("Room1").child("CurrentPlayer").setValue(1);
									else database.child("Room1").child("CurrentPlayer").setValue(Player + SkipTurn * Integer.valueOf(BaseTurnDir));

									if (HandType.compareTo("$") == 0) database.child("Room1").child("MaxDraw").setValue(3);

									if (HandType.compareTo("^") == 0)
									{
										if (BaseTurnDir.compareTo("1") == 0)
											database.child("Room1").child("TurnDir").setValue(-1);
										else database.child("Room1").child("TurnDir").setValue(1);
									}

									if (HandColor.compareTo(BaseColor) == 0)
										database.child("Room1").child("Color").setValue(0);
								}

								if (HandType.compareTo("+") == 0) database.child("Room1").child("MaxDraw").setValue(5);

								database.child("Room1").child("Card").setValue(HandCards.get(CardOffset + Offset));
								HandCards.remove(CardOffset + Offset);
							}

							//TODO проверку если на столе нет карты
							if (CardOffset > 0) CardOffset--;

							DrawHand();
						}
						break;
				}
			}
			return false;
		}
	};
	//endregion

	@Override
	protected void onStop()
	{
		super.onStop();

		database.child("Room1").child("ConnectedPlayers").setValue(Integer.valueOf(BaseConnectedPlayers) - 1);
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
		HandCardsCount = (TextView) findViewById(R.id.HandCardsCount);
		WildChoice = (ConstraintLayout) findViewById(R.id.WildChoice);
		SubmitRadio = (Button) findViewById(R.id.SubmitRadio);
		RadioRed = (RadioButton) findViewById(R.id.RadioRed);
		RadioYellow = (RadioButton) findViewById(R.id.RadioYellow);
		RadioGreen = (RadioButton) findViewById(R.id.RadioGreen);
		RadioBlue = (RadioButton) findViewById(R.id.RadioBlue);
		ColorView = (TextView) findViewById(R.id.ColorView);
		GiveTurn = (Button) findViewById(R.id.GiveTurn);
		//endregion

		//Получим начальные значения и делаем свои штуки
		database.child("Room1").addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				for (DataSnapshot child : dataSnapshot.getChildren())
				{
					switch (child.getKey())
					{
						case "Card":
							BaseCard = child.getValue().toString();
							break;
						case "Color":
							BaseColor = child.getValue().toString();
							break;
						case "ConnectedPlayers":
							BaseConnectedPlayers = child.getValue().toString();
							break;
						case "CurrentPlayer":
							BaseCurrentPlayer = child.getValue().toString();
							break;
						case "MaxDraw":
							BaseMaxDraw = child.getValue().toString();
							break;
						case "NewCard":
							BaseNewCard = child.getValue().toString();
							break;
						case "TurnDir":
							BaseTurnDir = child.getValue().toString();
							break;
					}

					DrawHand();
				}

				Player = Integer.valueOf(BaseConnectedPlayers) + 1;
				database.child("Room1").child("ConnectedPlayers").setValue(Player);

				if (Player - 1 == 0)
				{
					//Создаем карты на сервере
					GenerateCards();

					//Готовим базу к началу игры
					String card = Cards.get(rnd.nextInt(Cards.size()));
					database.child("Room1").child("NewCard").setValue(card);
					Cards.remove(card);
					card = Cards.get(rnd.nextInt(Cards.size()));
					database.child("Room1").child("Card").setValue(card);
					Cards.remove(card);
					database.child("Room1").child("Color").setValue(0);
					database.child("Room1").child("CurrentPlayer").setValue(1);
					database.child("Room1").child("MaxDraw").setValue(1);
					database.child("Room1").child("TurnDir").setValue(1);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});

		//Обновляем значения каждый раз при изминении и делаем свои штуки
		database.child("Room1").addChildEventListener(new ChildEventListener()
		{
			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s)
			{
				switch (dataSnapshot.getKey())
				{
					case "Card":
						BaseCard = dataSnapshot.getValue().toString();
						break;
					case "Color":
						BaseColor = dataSnapshot.getValue().toString();
						if (BaseColor.compareTo("0") != 0) ColorView.setVisibility(View.VISIBLE);
						else ColorView.setVisibility(View.INVISIBLE);
						ColorView.setText(BaseColor);
						break;
					case "ConnectedPlayers":
						BaseConnectedPlayers = dataSnapshot.getValue().toString();
						break;
					case "CurrentPlayer":
						BaseCurrentPlayer = dataSnapshot.getValue().toString();
						break;
					case "MaxDraw":
						BaseMaxDraw = dataSnapshot.getValue().toString();
						break;
					case "NewCard":
						BaseNewCard = dataSnapshot.getValue().toString();
						break;
					case "TurnDir":
						BaseTurnDir = dataSnapshot.getValue().toString();
						break;

					default:
						Toast.makeText(MainActivity.this, "database.child(\"Room1\").addChildEventListener сломался", Toast.LENGTH_SHORT).show();
				}

				if (Player - 1 == 0)
				{
					if (BaseNewCard.compareTo("0") == 0)
					{
						String card = Cards.get(rnd.nextInt(Cards.size()));
						database.child("Room1").child("NewCard").setValue(card);
						Cards.remove(card);
					}

					if (Integer.valueOf(BaseCurrentPlayer) > Integer.valueOf(BaseConnectedPlayers))
						database.child("Room1").child("CurrentPlayer").setValue(1);
					else
					{
						if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
							PlayerTurn.setText("Ваш ход");
						else PlayerTurn.setText("Ход игрока " + BaseCurrentPlayer);
					}

					if (Integer.valueOf(BaseCurrentPlayer) < 1)
						database.child("Room1").child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
				}

				if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
					PlayerTurn.setText("Ваш ход");
				else PlayerTurn.setText("Ход игрока " + BaseCurrentPlayer);

				DrawHand();
			}

			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) { }

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) { }

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});

		//region Listeners
		Deck.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
				{
					if (Integer.valueOf(BaseMaxDraw) >= 1)
					{
						HandCards.add(BaseNewCard);
						if (HandCards.size() > CardOffset + 5) CardOffset++;
						DrawHand();
						database.child("Room1").child("NewCard").setValue(0);
						if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
							database.child("Room1").child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
						else
							database.child("Room1").child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
					}
				}
			}
		});

		SubmitRadio.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				WildChoice.setVisibility(View.INVISIBLE);

				if (RadioRed.isChecked())
				{
					database.child("Room1").child("Color").setValue("RED");
					ColorView.setText("Заказан красный");
				}
				if (RadioYellow.isChecked())
				{
					database.child("Room1").child("Color").setValue("YELLOW");
					ColorView.setText("Заказан желтый");
				}
				if (RadioGreen.isChecked())
				{
					database.child("Room1").child("Color").setValue("GREEN");
					ColorView.setText("Заказан зеленый");
				}
				if (RadioBlue.isChecked())
				{
					database.child("Room1").child("Color").setValue("BLUE");
					ColorView.setText("Заказан синий");
				}

				database.child("Room1").child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
			}
		});

		GiveTurn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child("Room1").child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
				GiveTurn.setVisibility(View.INVISIBLE);
			}
		});

		LeftCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset + 1 <= HandCards.size() - 5) CardOffset++;
				DrawHand();
			}
		});

		RightCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset - 1 >= 0) CardOffset--;
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