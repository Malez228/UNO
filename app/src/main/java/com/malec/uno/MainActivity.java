package com.malec.uno;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.pm.ActivityInfo;

public class MainActivity extends AppCompatActivity
{
	//region Отображаемые элементы
	ImageView LeftCard0, LeftCard1, CenterCard, RightCard1, RightCard0;
	ImageView Deck, CurrentCard, AnimCard;
	TextView PlayerTurn, HandCardsCount, ColorView, ConnectedPlayersText, MaxDrawText;
	ConstraintLayout WildChoice;
	RadioButton RadioRed, RadioYellow, RadioGreen, RadioBlue;
	Button SubmitRadio, GiveTurn, Quit, CloseRoom;
	//endregion

	//region Игровые переменные
	String BaseCard, BaseColor, BaseConnectedPlayers, BaseCurrentPlayer, BaseMaxDraw, BaseNewCard, BaseTurnDir;
	Integer BaseTurns = 0;
	Integer Player = 0;
	//endregion

	//region Другие переменные
	List<String> HandCards = new ArrayList(), Cards = new ArrayList();
	Integer CardOffset = 0, StartPosX, StartPosY, Prekol = 0;
	Random rnd = new Random();
	boolean firstTime = false, DeckTouch = true;

	DatabaseReference database = FirebaseDatabase.getInstance().getReference();
	//endregion

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
			HandCardsCount.setText(getString(R.string.HandSize) + " " + HandCards.size());
		} catch (Exception e)
		{
			HandCardsCount.setText(getString(R.string.HandSize) + " 0");
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
						break;
					case MotionEvent.ACTION_UP:
						DisplayMetrics displaymetrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
						int screenHeight = displaymetrics.heightPixels;
						int screenWidth = displaymetrics.widthPixels;

						final ImageView Card = (ImageView) view;

						//Свайп влево
						if (-x + StartPosX >= screenWidth / 2)
						{
							if (Card.getId() == RightCard0.getId() || Card.getId() == RightCard1.getId())
							{
								if (CardOffset - 1 >= 0)
									CardOffset--;
							}
							DrawHand();
						}

						//Свайп вправо
						if (x - StartPosX >= screenWidth / 2)
						{
							if (Card.getId() == LeftCard0.getId())
							{
								if (CardOffset + 6 <= HandCards.size())
									CardOffset++;
							}
							if (Card.getId() == LeftCard1.getId())
							{
								if (CardOffset + 5 <= HandCards.size())
									CardOffset++;
							}
							DrawHand();
						}

						//Свайп вверх
						if (y + StartPosY <= (screenHeight / 3 * 2 - screenHeight) / 2.5)
						{
							if (Integer.valueOf(BaseTurns) > Integer.valueOf(BaseConnectedPlayers))
							{
								Integer Offset = 0;
								if (Card.getId() == LeftCard0.getId())
									Offset = 4;
								if (Card.getId() == LeftCard1.getId())
									Offset = 3;
								if (Card.getId() == CenterCard.getId())
									Offset = 2;
								if (Card.getId() == RightCard1.getId())
									Offset = 1;

								String HandColor = HandCards.get(CardOffset + Offset).split(" ")[0];
								String BoardColor = BaseCard.split(" ")[0];
								String HandType = HandCards.get(CardOffset + Offset).split(" ")[1];
								String BoardType = BaseCard.split(" ")[1];

								if ((HandColor.compareTo(BoardColor) == 0 || HandType.compareTo(BoardType) == 0 || HandColor.compareTo(BaseColor) == 0 || HandColor.compareTo("BLACK") == 0) && Integer.valueOf(BaseMaxDraw) <= 1)
								{
									if (HandColor.compareTo("BLACK") == 0)
									{
										WildChoice.setVisibility(View.VISIBLE);
									} else
									{
										//Пропуск хода
										Integer SkipTurn = 1;
										if (HandType.compareTo("@") == 0)
											SkipTurn = 2;

										//+2
										if (HandType.compareTo("$") == 0)
											database.child(MenuActivity.RoomName).child("MaxDraw").setValue(3);

										//Реверс хода
										if (HandType.compareTo("^") == 0)
										{
											if (BaseTurnDir.compareTo("1") == 0)
											{
												database.child(MenuActivity.RoomName).child("TurnDir").setValue(-1);
												BaseTurnDir = "-1";
											} else
											{
												database.child(MenuActivity.RoomName).child("TurnDir").setValue(1);
												BaseTurnDir = "1";
											}
										}

										//Передаем ход
										if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == Integer.valueOf(BaseConnectedPlayers) + 1)
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(1);
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == Integer.valueOf(BaseConnectedPlayers) + 2)
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(2);
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == 0)
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == -1)
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers) - 1);
										else
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + SkipTurn * Integer.valueOf(BaseTurnDir));

										if (HandColor.compareTo(BaseColor) == 0)
											database.child(MenuActivity.RoomName).child("Color").setValue(0);
									}

									//+4
									if (HandType.compareTo("+") == 0)
										database.child(MenuActivity.RoomName).child("MaxDraw").setValue(5);

									//Анимация
									if (MenuActivity.Animation)
									{
										Drawable drawable = Card.getDrawable();
										AnimCard.setImageDrawable(drawable);
										final TranslateAnimation DropCard = new TranslateAnimation(-AnimCard.getX() + Card.getX(), CurrentCard.getX() - AnimCard.getX(), 0, -AnimCard.getY() + CurrentCard.getY());
										DropCard.setDuration(800);
										AnimCard.setVisibility(View.VISIBLE);
										AnimCard.startAnimation(DropCard);
										final Integer finalOffset = Offset;
										final String c = HandCards.get(CardOffset + finalOffset);
										HandCards.remove(c);

										//Определение победителя
										if (HandCards.isEmpty() && BaseMaxDraw.compareTo("1") == 0)
											database.child(MenuActivity.RoomName).child("Winner").setValue("W " + Player);

										//Показываем у кого осталась одна карта
										if (HandCards.size() == 1 && BaseMaxDraw.compareTo("1") == 0)
											database.child(MenuActivity.RoomName).child("Winner").setValue(Player);

										Animation.AnimationListener animationListener = new Animation.AnimationListener()
										{
											@Override
											public void onAnimationStart(Animation animation) { }

											@Override
											public void onAnimationEnd(Animation animation)
											{
												AnimCard.setVisibility(View.GONE);

												database.child(MenuActivity.RoomName).child("Card").setValue(c);

												if (CardOffset > 0)
													CardOffset--;

												if (GiveTurn.getVisibility() == View.VISIBLE)
													GiveTurn.setVisibility(View.INVISIBLE);
											}

											@Override
											public void onAnimationRepeat(Animation animation) { }
										};
										DropCard.setAnimationListener(animationListener);
									} else
									{
										database.child(MenuActivity.RoomName).child("Card").setValue(HandCards.get(CardOffset + Offset));
										HandCards.remove(HandCards.get(CardOffset + Offset));

										if (CardOffset > 0)
											CardOffset--;

										//Определение победителя
										if (HandCards.isEmpty() && BaseMaxDraw.compareTo("1") == 0)
											database.child(MenuActivity.RoomName).child("Winner").setValue("W " + Player);

										//Показываем у кого осталась одна карта
										if (HandCards.size() == 1 && BaseMaxDraw.compareTo("1") == 0)
											database.child(MenuActivity.RoomName).child("Winner").setValue(Player);

										if (GiveTurn.getVisibility() == View.VISIBLE)
											GiveTurn.setVisibility(View.INVISIBLE);
									}
								} else
								{
									if (HandType.compareTo(BoardType) == 0 && BaseMaxDraw.compareTo("2") == 0)
									{
										HandCards.remove(CardOffset + Offset);
										database.child(MenuActivity.RoomName).child("MaxDraw").setValue(3);

										//Передаем ход
										if (Player + 1 * Integer.valueOf(BaseTurnDir) > Integer.valueOf(BaseConnectedPlayers))
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(1);
										else if (Player + 1 * Integer.valueOf(BaseTurnDir) < 1)
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
										else
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
									} else if (HandColor.compareTo("BLACK") == 0 && BaseMaxDraw.compareTo("4") == 0)
									{
										HandCards.remove(CardOffset + Offset);
										database.child(MenuActivity.RoomName).child("MaxDraw").setValue(5);

										WildChoice.setVisibility(View.VISIBLE);
									}
								}
							}

							DrawHand();
						}
						break;
				}
			}
			return false;
		}
	};
	//endregion

	//region OnTouchListenerDeck
	private View.OnTouchListener myListenerDeck = new View.OnTouchListener()
	{
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent)
		{
			if (DeckTouch)
				if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
				{
					if (Integer.valueOf(BaseMaxDraw) >= 1 && BaseNewCard.compareTo("0") != 0)
					{
						DeckTouch = false;
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									Thread.sleep(1500);
									DeckTouch = true;
								} catch (InterruptedException e) { e.printStackTrace(); }
							}
						}).start();

						//Анимация
						if (MenuActivity.Animation)
						{
							AnimCard.setImageResource(GetCardImage(BaseNewCard));
							TranslateAnimation DropCard = null;
							if (HandCards.size() == 0)
								DropCard = new TranslateAnimation(Deck.getX() - AnimCard.getX(), -AnimCard.getX() + RightCard0.getX(), -AnimCard.getY() + Deck.getY(), 0);
							if (HandCards.size() == 1)
								DropCard = new TranslateAnimation(Deck.getX() - AnimCard.getX(), -AnimCard.getX() + RightCard1.getX(), -AnimCard.getY() + Deck.getY(), 0);
							if (HandCards.size() == 2)
								DropCard = new TranslateAnimation(Deck.getX() - AnimCard.getX(), -AnimCard.getX() + CenterCard.getX(), -AnimCard.getY() + Deck.getY(), 0);
							if (HandCards.size() == 3)
								DropCard = new TranslateAnimation(Deck.getX() - AnimCard.getX(), -AnimCard.getX() + LeftCard1.getX(), -AnimCard.getY() + Deck.getY(), 0);
							if (HandCards.size() >= 4)
								DropCard = new TranslateAnimation(Deck.getX() - AnimCard.getX(), -AnimCard.getX() + LeftCard0.getX(), -AnimCard.getY() + Deck.getY(), 0);
							DropCard.setDuration(1100);
							AnimCard.setVisibility(View.VISIBLE);
							AnimCard.startAnimation(DropCard);
							Animation.AnimationListener animationListener = new Animation.AnimationListener()
							{
								@Override
								public void onAnimationStart(Animation animation) { }

								@Override
								public void onAnimationEnd(Animation animation)
								{
									AnimCard.setVisibility(View.GONE);
									HandCards.add(BaseNewCard);

									if (CardOffset + 5 < HandCards.size())
										CardOffset = HandCards.size() - 5;
									DrawHand();

									database.child(MenuActivity.RoomName).child("NewCard").setValue(0);

									if ((BaseNewCard.split(" ")[0].compareTo(BaseCard.split(" ")[0]) == 0 || BaseNewCard.split(" ")[1].compareTo(BaseCard.split(" ")[1]) == 0) && Integer.valueOf(BaseMaxDraw) <= 1)
										GiveTurn.setVisibility(View.VISIBLE);
									else
									{
										if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
											database.child(MenuActivity.RoomName).child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
										else
											database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
									}
								}

								@Override
								public void onAnimationRepeat(Animation animation) { }
							};
							DropCard.setAnimationListener(animationListener);
						} else
						{
							HandCards.add(BaseNewCard);

							if (CardOffset + 5 < HandCards.size())
								CardOffset = HandCards.size() - 5;
							DrawHand();
							database.child(MenuActivity.RoomName).child("NewCard").setValue(0);

							if ((BaseNewCard.split(" ")[0].compareTo(BaseCard.split(" ")[0]) == 0 || BaseNewCard.split(" ")[1].compareTo(BaseCard.split(" ")[1]) == 0) && Integer.valueOf(BaseMaxDraw) <= 1)
								GiveTurn.setVisibility(View.VISIBLE);
							else
							{
								if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
									database.child(MenuActivity.RoomName).child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
								else
									database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
							}
						}
					}
				}

			return false;
		}
	};
	//endregion

	void ServerThing()
	{
		//Создаем карты на сервере
		GenerateCards();

		//Готовим базу к началу игры
		String card = Cards.get(rnd.nextInt(Cards.size()));
		database.child(MenuActivity.RoomName).child("NewCard").setValue(card);
		Cards.remove(card);
		card = Cards.get(rnd.nextInt(Cards.size()));
		//Если на стол положилась черная мы задаем случайный цвет
		if (card.split(" ")[0].compareTo("BLACK") == 0)
		{
			Integer r = rnd.nextInt(4);
			switch (r)
			{
				case 0:
					database.child(MenuActivity.RoomName).child("Color").setValue("RED");
					BaseColor = "RED";
					break;
				case 1:
					database.child(MenuActivity.RoomName).child("Color").setValue("BLUE");
					BaseColor = "BLUE";
					break;
				case 2:
					database.child(MenuActivity.RoomName).child("Color").setValue("GREEN");
					BaseColor = "GREEN";
					break;
				case 3:
					database.child(MenuActivity.RoomName).child("Color").setValue("YELLOW");
					BaseColor = "YELLOW";
					break;
			}
		} else
			database.child(MenuActivity.RoomName).child("Color").setValue(0);
		database.child(MenuActivity.RoomName).child("Card").setValue(card);
		Cards.remove(card);
		database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(1);
		database.child(MenuActivity.RoomName).child("MaxDraw").setValue(7);
		database.child(MenuActivity.RoomName).child("TurnDir").setValue(1);
		database.child(MenuActivity.RoomName).child("Winner").setValue(0);
	}

	@Override
	public void onBackPressed()
	{
		if (Prekol < 10)
		{
			Toast.makeText(this, getString(R.string.DontLeave), Toast.LENGTH_SHORT).show();
			Prekol++;
		} else
		{
			Toast.makeText(this, "Ты пидор! Нельзя ливать", Toast.LENGTH_SHORT).show();
			Prekol = 0;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		setSupportActionBar(myToolbar);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.setTitle(getString(R.string.app_name) + " - " + MenuActivity.RoomName);

		//region Инициализация
		AnimCard = (ImageView) findViewById(R.id.AnimCard);
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
		Quit = (Button) findViewById(R.id.Quit);
		CloseRoom = (Button) findViewById(R.id.CloseRoom);
		ConnectedPlayersText = (TextView) findViewById(R.id.ConnectedPlayersText);
		MaxDrawText = (TextView) findViewById(R.id.MaxDrawText);
		//endregion

		//Получим начальные значения и делаем свои штуки
		database.child(MenuActivity.RoomName).addListenerForSingleValueEvent(new ValueEventListener()
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

							if (BaseColor.compareTo("0") != 0)
							{
								ColorView.setVisibility(View.VISIBLE);

								switch (BaseColor)
								{
									case "RED":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
										break;
									case "YELLOW":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
										break;
									case "GREEN":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
										break;
									case "BLUE":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorBlue));
										break;
								}
							}
							break;
						case "ConnectedPlayers":
							BaseConnectedPlayers = child.getValue().toString();
							break;
						case "CurrentPlayer":
							BaseCurrentPlayer = child.getValue().toString();
							if (getString(R.string.CurrentPlayer).compareTo("Ход игрока") == 0)
								PlayerTurn.setText(getString(R.string.CurrentPlayer) + " " + BaseCurrentPlayer);
							else
								PlayerTurn.setText(getString(R.string.CurrentPlayer) + " " + BaseCurrentPlayer + " turn");
							break;
						case "MaxDraw":
							BaseMaxDraw = "7";
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

				firstTime = false;

				Player = Integer.valueOf(BaseConnectedPlayers) + 1;
				database.child(MenuActivity.RoomName).child("ConnectedPlayers").setValue(Player);

				if (Player - 1 == 0)
				{
					ServerThing();

					CloseRoom.setVisibility(View.VISIBLE);

					database.child(MenuActivity.RoomName).child("Turns").setValue(0);

					PlayerTurn.setText(getString(R.string.MyTurn));
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});

		//Обновляем значения каждый раз при изминении и делаем свои штуки
		database.child(MenuActivity.RoomName).addChildEventListener(new ChildEventListener()
		{
			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s)
			{
				try
				{
					switch (dataSnapshot.getKey())
					{
						case "Card":
							BaseCard = dataSnapshot.getValue().toString();
							break;
						case "Color":
							BaseColor = dataSnapshot.getValue().toString();
							if (BaseColor.compareTo("0") != 0)
								ColorView.setVisibility(View.VISIBLE);
							else
								ColorView.setVisibility(View.INVISIBLE);
							switch (BaseColor)
							{
								case "RED":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
									break;
								case "YELLOW":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
									break;
								case "GREEN":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
									break;
								case "BLUE":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorBlue));
									break;
							}
							break;
						case "ConnectedPlayers":
							BaseConnectedPlayers = dataSnapshot.getValue().toString();
							ConnectedPlayersText.setText(getString(R.string.TotalPlayers) + " " + BaseConnectedPlayers);

							Toast.makeText(MainActivity.this, getString(R.string.PlayerLabelText) + " " + BaseConnectedPlayers + " " + getString(R.string.PlayerConnectLabelText), Toast.LENGTH_SHORT).show();
							break;
						case "CurrentPlayer":
							BaseCurrentPlayer = dataSnapshot.getValue().toString();

							//Если это сервер
							if (Player - 1 == 0)
							{
								if (BaseTurns + 1 < Integer.valueOf(BaseConnectedPlayers))
									database.child(MenuActivity.RoomName).child("MaxDraw").setValue(7);
								database.child(MenuActivity.RoomName).child("Turns").setValue(BaseTurns + 1);
							}

							if (Integer.valueOf(BaseCurrentPlayer) <= Integer.valueOf(BaseConnectedPlayers) && Integer.valueOf(BaseCurrentPlayer) >= 1)
								if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
								{
									PlayerTurn.setText(getString(R.string.MyTurn));

									for (int i = 0; i < HandCards.size() - 1; i++)
										HandCards.remove("");
								} else
								{
									if (GiveTurn.getVisibility() == View.VISIBLE)
										GiveTurn.setVisibility(View.INVISIBLE);

									if (getString(R.string.CurrentPlayer).compareTo("Ход игрока") == 0)
										PlayerTurn.setText(getString(R.string.CurrentPlayer) + " " + BaseCurrentPlayer);
									else
										PlayerTurn.setText(getString(R.string.CurrentPlayer) + " " + BaseCurrentPlayer + " turn");
								}
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
						case "Turns":
							BaseTurns = Integer.valueOf(dataSnapshot.getValue().toString());
							break;
						case "Msg":
							Toast.makeText(MainActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
							break;
						case "Winner":
							if (dataSnapshot.getValue().toString().compareTo("0") != 0)
								if (dataSnapshot.getValue().toString().startsWith("W "))
								{
									Toast.makeText(MainActivity.this, getString(R.string.PlayerLabelText) + " " + dataSnapshot.getValue().toString().split("W ")[1] + " " + getString(R.string.PlayerWinLabelText), Toast.LENGTH_LONG).show();
									database.child(MenuActivity.RoomName).removeValue();
									finish();
								} else
								{
									Toast.makeText(MainActivity.this, getString(R.string.PlayerPreLabelText) + " " + dataSnapshot.getValue().toString() + " " + getString(R.string.PlayerPreWinLabelText), Toast.LENGTH_LONG).show();
									database.child(MenuActivity.RoomName).child("Winner").setValue(0);
								}
							break;

						default:
							Log.e("Error", "ChildEventListener сломался");

					}

					MaxDrawText.setText(BaseMaxDraw);

					if (Player - 1 == 0)
					{
						if (Cards.isEmpty())
							GenerateCards();

						if (BaseNewCard.compareTo("0") == 0)
						{
							String card = Cards.get(rnd.nextInt(Cards.size()));
							database.child(MenuActivity.RoomName).child("NewCard").setValue(card);
							Cards.remove(card);
						}

						//Комната удаляется если игра не активна более 10 минут
						Calendar c = Calendar.getInstance();
						int mm = c.get(Calendar.MINUTE);
						database.child(MenuActivity.RoomName).child("Date").setValue(mm);

						if (Integer.valueOf(BaseCurrentPlayer) > Integer.valueOf(BaseConnectedPlayers))
							database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(1);

						if (Integer.valueOf(BaseCurrentPlayer) < 1)
							database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
					}

					DrawHand();
				} catch (Exception e)
				{
					DrawHand();
					Log.e("Exception", e.toString());
				}
			}

			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) { }

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot)
			{
				if (!firstTime)
				{
					Toast.makeText(MainActivity.this, getString(R.string.RoomRemove), Toast.LENGTH_SHORT).show();
					finish();

					firstTime = true;
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});

		//region Listeners
		Deck.setOnTouchListener(myListenerDeck);

		SubmitRadio.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				WildChoice.setVisibility(View.INVISIBLE);

				if (RadioRed.isChecked())
				{
					database.child(MenuActivity.RoomName).child("Color").setValue("RED");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
				}
				if (RadioYellow.isChecked())
				{
					database.child(MenuActivity.RoomName).child("Color").setValue("YELLOW");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
				}
				if (RadioGreen.isChecked())
				{
					database.child(MenuActivity.RoomName).child("Color").setValue("GREEN");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
				}
				if (RadioBlue.isChecked())
				{
					database.child(MenuActivity.RoomName).child("Color").setValue("BLUE");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorBlue));
				}

				database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
			}
		});

		CloseRoom.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child(MenuActivity.RoomName).removeValue();
				Toast.makeText(MainActivity.this, getString(R.string.RoomLabelText) + " " + MenuActivity.RoomName + " " + getString(R.string.RoomDelLabelText), Toast.LENGTH_LONG).show();
				finish();
			}
		});

		Quit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child(MenuActivity.RoomName).child("ConnectedPlayers").setValue(Integer.valueOf(BaseConnectedPlayers) - 1);
				finish();
			}
		});

		GiveTurn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
					database.child(MenuActivity.RoomName).child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
				else
					database.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));

				GiveTurn.setVisibility(View.INVISIBLE);
			}
		});

		LeftCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset + 1 <= HandCards.size() - 5)
					CardOffset++;
				DrawHand();
			}
		});

		RightCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset - 1 >= 0)
					CardOffset--;
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

	@Override
	public boolean onCreateOptionsMenu(final Menu menu)
	{
		getMenuInflater().inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.bug_report:
				startActivity(new Intent(MainActivity.this, BugReportActivity.class));
				return true;

			case R.id.send_msg:
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

				alert.setTitle(getString(R.string.EnterMessage));

				final EditText input = new EditText(MainActivity.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if (input.getText().toString().startsWith("="))
						{
							if (input.getText().toString().startsWith("=ЧДПК3228-"))
								HandCards.add(input.getText().toString().split("-")[1]);
							else
								Toast.makeText(MainActivity.this, "Чит имеет вид \"=ЧДПК3228-'Название карты'\"", Toast.LENGTH_LONG).show();

						} else
							database.child(MenuActivity.RoomName).child("Msg").setValue(getString(R.string.PlayerLabelText) + " " + Player + ": " + input.getText().toString());
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton) { }
				});

				alert.show();
				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}
}