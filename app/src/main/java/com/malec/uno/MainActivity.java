package com.malec.uno;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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
	RadioButton RadioR, RadioY, RadioG, RadioB;
	Button SubmitRadio, GiveTurn, Quit, CloseRoom;
	//endregion

	//region Игровые переменные
	String BaseCard, BaseColor, BaseConnectedPlayers, BaseCurrentPlayer, BaseMaxDraw, BaseNewCard, BaseTurnDir;
	Integer BaseTurns = 0;
	public static Integer Player = 0;
	//endregion

	//region Другие переменные
	List<String> HandCards = new ArrayList(), Cards = new ArrayList();
	Integer CardOffset = 0, StartPosX, StartPosY, Prekol = 0;
	Random rnd = new Random();
	boolean firstTime = false, DeckTouch = true;

	Float AnimX0, AnimX1, AnimY1;
	String SaveCard = "";

	public static Integer Reconnect = 0;

	DatabaseReference database = FirebaseDatabase.getInstance().getReference();
	//endregion

	int GetCardImage(String name)
	{
		switch (name)
		{
			case "R 0":
				return R.drawable.r0;
			case "R 1":
				return R.drawable.r1;
			case "R 2":
				return R.drawable.r2;
			case "R 3":
				return R.drawable.r3;
			case "R 4":
				return R.drawable.r4;
			case "R 5":
				return R.drawable.r5;
			case "R 6":
				return R.drawable.r6;
			case "R 7":
				return R.drawable.r7;
			case "R 8":
				return R.drawable.r8;
			case "R 9":
				return R.drawable.r9;
			case "R $":
				return R.drawable.r10;
			case "R @":
				return R.drawable.r11;
			case "R ^":
				return R.drawable.r12;

			case "Y 0":
				return R.drawable.y0;
			case "Y 1":
				return R.drawable.y1;
			case "Y 2":
				return R.drawable.y2;
			case "Y 3":
				return R.drawable.y3;
			case "Y 4":
				return R.drawable.y4;
			case "Y 5":
				return R.drawable.y5;
			case "Y 6":
				return R.drawable.y6;
			case "Y 7":
				return R.drawable.y7;
			case "Y 8":
				return R.drawable.y8;
			case "Y 9":
				return R.drawable.y9;
			case "Y $":
				return R.drawable.y10;
			case "Y @":
				return R.drawable.y11;
			case "Y ^":
				return R.drawable.y12;

			case "G 0":
				return R.drawable.g0;
			case "G 1":
				return R.drawable.g1;
			case "G 2":
				return R.drawable.g2;
			case "G 3":
				return R.drawable.g3;
			case "G 4":
				return R.drawable.g4;
			case "G 5":
				return R.drawable.g5;
			case "G 6":
				return R.drawable.g6;
			case "G 7":
				return R.drawable.g7;
			case "G 8":
				return R.drawable.g8;
			case "G 9":
				return R.drawable.g9;
			case "G $":
				return R.drawable.g10;
			case "G @":
				return R.drawable.g11;
			case "G ^":
				return R.drawable.g12;

			case "B 0":
				return R.drawable.b0;
			case "B 1":
				return R.drawable.b1;
			case "B 2":
				return R.drawable.b2;
			case "B 3":
				return R.drawable.b3;
			case "B 4":
				return R.drawable.b4;
			case "B 5":
				return R.drawable.b5;
			case "B 6":
				return R.drawable.b6;
			case "B 7":
				return R.drawable.b7;
			case "B 8":
				return R.drawable.b8;
			case "B 9":
				return R.drawable.b9;
			case "B $":
				return R.drawable.b10;
			case "B @":
				return R.drawable.b11;
			case "B ^":
				return R.drawable.b12;

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
		for (int i = 0; i < HandCards.size() - 1; i++)
			HandCards.remove("");

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

		try
		{
			CurrentCard.setImageResource(GetCardImage(BaseCard));
		} catch (Exception e) { CurrentCard.setImageResource(GetCardImage("EmptyCard")); }

		try
		{
			HandCardsCount.setText(getString(R.string.HandSize) + " " + HandCards.size());
		} catch (Exception e) { HandCardsCount.setText(getString(R.string.HandSize) + " 0"); }
	}

	void GenerateCards()
	{
		Cards.add("Y 0");
		Cards.add("R 0");
		Cards.add("B 0");
		Cards.add("G 0");

		for (int i = 1; i < 10; i++)
			Cards.add("Y " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("Y " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("R " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("R " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("B " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("B " + i);

		for (int i = 1; i < 10; i++)
			Cards.add("G " + i);
		for (int i = 1; i < 10; i++)
			Cards.add("G " + i);

		Cards.add("Y @");
		Cards.add("Y @");
		Cards.add("R @");
		Cards.add("R @");
		Cards.add("B @");
		Cards.add("B @");
		Cards.add("G @");
		Cards.add("G @");

		Cards.add("Y ^");
		Cards.add("Y ^");
		Cards.add("R ^");
		Cards.add("R ^");
		Cards.add("B ^");
		Cards.add("B ^");
		Cards.add("G ^");
		Cards.add("G ^");

		Cards.add("Y $");
		Cards.add("Y $");
		Cards.add("R $");
		Cards.add("R $");
		Cards.add("B $");
		Cards.add("B $");
		Cards.add("G $");
		Cards.add("G $");

		for (int i = 0; i < 4; i++)
			Cards.add("BLACK #");
		for (int i = 0; i < 4; i++)
			Cards.add("BLACK +");
	}

	Animation.AnimationListener CardReceiveAAnimation = new Animation.AnimationListener()
	{
		@Override
		public void onAnimationStart(Animation animation) { }

		@Override
		public void onAnimationEnd(Animation animation)
		{
			AnimCard.setVisibility(View.GONE);
			CardReceive();
		}

		@Override
		public void onAnimationRepeat(Animation animation) { }
	};

	Animation.AnimationListener CardSendAnimation = new Animation.AnimationListener()
	{
		@Override
		public void onAnimationStart(Animation animation)
		{
			if (CardOffset > 0)
				CardOffset--;
			DrawHand();
		}

		@Override
		public void onAnimationEnd(Animation animation)
		{
			AnimCard.setVisibility(View.GONE);

			database.child(MenuActivity.ClickRoomName).child("Card").setValue(SaveCard);
		}

		@Override
		public void onAnimationRepeat(Animation animation) { }
	};

	void SendCardsArrary()
	{
		String array = "";
		for (int i = 0; i < HandCards.size(); i++)
			array+=HandCards.get(i)+';';
		database.child(MenuActivity.ClickRoomName).child("Player-" + Player.toString()).child("Cards").setValue(array);
	}

	void CardReceive()
	{
		HandCards.add(BaseNewCard);

		if (CardOffset + 5 < HandCards.size())
			CardOffset = HandCards.size() - 5;
		DrawHand();

		database.child(MenuActivity.ClickRoomName).child("NewCard").setValue(0);
		SendCardsArrary();

		if ((BaseNewCard.split(" ")[0].compareTo(BaseCard.split(" ")[0]) == 0 || BaseNewCard.split(" ")[1].compareTo(BaseCard.split(" ")[1]) == 0) && Integer.valueOf(BaseMaxDraw) <= 1)
			GiveTurn.setVisibility(View.VISIBLE);
		else
		{
			if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
				database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
			else
				database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
		}
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
						Log.i("Coords", x + ":" + y);
						break;
					case MotionEvent.ACTION_UP:
						DisplayMetrics displaymetrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
						int screenHeight = displaymetrics.heightPixels;
						int screenWidth = displaymetrics.widthPixels;

						final ImageView Card = (ImageView) view;

						//Свайп влево
						if (StartPosX - x >= screenWidth / 2)
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
							if (Card.getId() == LeftCard1.getId() || Card.getId() == LeftCard0.getId())
							{
								if (CardOffset + 6 <= HandCards.size())
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
											database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(3);

										//Реверс хода
										if (HandType.compareTo("^") == 0)
										{
											if (BaseTurnDir.compareTo("1") == 0)
											{
												database.child(MenuActivity.ClickRoomName).child("TurnDir").setValue(-1);
												BaseTurnDir = "-1";
											} else
											{
												database.child(MenuActivity.ClickRoomName).child("TurnDir").setValue(1);
												BaseTurnDir = "1";
											}
										}

										//Передаем ход
										if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == Integer.valueOf(BaseConnectedPlayers) + 1)
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(1);
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == Integer.valueOf(BaseConnectedPlayers) + 2)
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(2);
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == 0)
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
										else if (Player + SkipTurn * Integer.valueOf(BaseTurnDir) == -1)
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers) - 1);
										else
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Player + SkipTurn * Integer.valueOf(BaseTurnDir));

										if (HandColor.compareTo(BaseColor) == 0)
											database.child(MenuActivity.ClickRoomName).child("Color").setValue(0);
									}

									//+4
									if (HandType.compareTo("+") == 0)
										database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(5);

									//Анимация
									if (MenuActivity.Animation)
									{
										Drawable drawable = Card.getDrawable();
										AnimCard.setImageDrawable(drawable);
										final Integer finalOffset = Offset;
										SaveCard = HandCards.get(CardOffset + finalOffset);
										HandCards.remove(SaveCard);
										AnimCard.setVisibility(View.VISIBLE);

										final TranslateAnimation DropCard = new TranslateAnimation(-AnimCard.getX() + Card.getX(), CurrentCard.getX() - AnimCard.getX(), 0, -AnimCard.getY() + CurrentCard.getY());
										DropCard.setDuration(800);
										DropCard.setAnimationListener(CardSendAnimation);
										AnimCard.startAnimation(DropCard);
									} else
									{
										database.child(MenuActivity.ClickRoomName).child("Card").setValue(HandCards.get(CardOffset + Offset));
										HandCards.remove(HandCards.get(CardOffset + Offset));
									}

									if (CardOffset > 0)
										CardOffset--;
									DrawHand();

									//Определение победителя
									if (HandCards.isEmpty() && BaseMaxDraw.compareTo("1") == 0)
										database.child(MenuActivity.ClickRoomName).child("Winner").setValue("W " + Player);

									//Показываем у кого осталась одна карта
									if (HandCards.size() == 1 && BaseMaxDraw.compareTo("1") == 0)
										database.child(MenuActivity.ClickRoomName).child("Winner").setValue(Player);

									SendCardsArrary();

									if (GiveTurn.getVisibility() == View.VISIBLE)
										GiveTurn.setVisibility(View.INVISIBLE);
								} else
								{
									Log.e("Touchlistener", "Карта неподходит");
									if (HandType.compareTo(BoardType) == 0 && BaseMaxDraw.compareTo("2") == 0)
									{
										HandCards.remove(CardOffset + Offset);
										SendCardsArrary();
										database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(3);

										//Передаем ход
										if (Player + 1 * Integer.valueOf(BaseTurnDir) > Integer.valueOf(BaseConnectedPlayers))
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(1);
										else if (Player + 1 * Integer.valueOf(BaseTurnDir) < 1)
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
										else
											database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
									} else if (HandColor.compareTo("BLACK") == 0 && BaseMaxDraw.compareTo("4") == 0)
									{
										HandCards.remove(CardOffset + Offset);
										database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(5);

										WildChoice.setVisibility(View.VISIBLE);
									}

									//Определение победителя
									if (HandCards.isEmpty() && BaseMaxDraw.compareTo("1") == 0)
										database.child(MenuActivity.ClickRoomName).child("Winner").setValue("W " + Player);

									//Показываем у кого осталась одна карта
									if (HandCards.size() == 1 && BaseMaxDraw.compareTo("1") == 0)
										database.child(MenuActivity.ClickRoomName).child("Winner").setValue(Player);
									DrawHand();
								}
							}
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
						GiveTurn.setVisibility(View.INVISIBLE);
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								try
								{
									Thread.sleep(1200);
									DeckTouch = true;
								} catch (InterruptedException e) { e.printStackTrace(); }
							}
						}).start();

						//Анимация
						if (MenuActivity.Animation)
						{
							//Штуки для анимации
							AnimX0 = AnimCard.getX();
							AnimX1 = Deck.getX() - AnimX0;
							AnimY1 = Deck.getY() - AnimCard.getY();

							AnimCard.setImageResource(GetCardImage(BaseNewCard));
							TranslateAnimation DropCard = null;
							switch (HandCards.size())
							{
								case 0:
									DropCard = new TranslateAnimation(AnimX1, RightCard0.getX() - AnimX0, AnimY1, 0);
									break;
								case 1:
									DropCard = new TranslateAnimation(AnimX1, RightCard1.getX() - AnimX0, AnimY1, 0);
									break;
								case 2:
									DropCard = new TranslateAnimation(AnimX1, CenterCard.getX() - AnimX0, AnimY1, 0);
									break;
								case 3:
									DropCard = new TranslateAnimation(AnimX1, LeftCard1.getX() - AnimX0, AnimY1, 0);
									break;
								default:
									DropCard = new TranslateAnimation(AnimX1, LeftCard0.getX() - AnimX0, AnimY1, 0);
							}
							DropCard.setDuration(1100);
							AnimCard.setVisibility(View.VISIBLE);
							DropCard.setAnimationListener(CardReceiveAAnimation);
							AnimCard.startAnimation(DropCard);
						} else
							CardReceive();
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
		database.child(MenuActivity.ClickRoomName).child("NewCard").setValue(card);
		Cards.remove(card);
		card = Cards.get(rnd.nextInt(Cards.size()));
		//Если на стол положилась черная мы задаем случайный цвет
		if (card.split(" ")[0].compareTo("BLACK") == 0)
		{
			Integer r = rnd.nextInt(4);
			switch (r)
			{
				case 0:
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("R");
					BaseColor = "R";
					break;
				case 1:
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("B");
					BaseColor = "B";
					break;
				case 2:
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("G");
					BaseColor = "G";
					break;
				case 3:
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("Y");
					BaseColor = "Y";
					break;
			}
		} else
			database.child(MenuActivity.ClickRoomName).child("Color").setValue(0);
		database.child(MenuActivity.ClickRoomName).child("Card").setValue(card);
		Cards.remove(card);
		database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(1);
		database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(7);
		database.child(MenuActivity.ClickRoomName).child("TurnDir").setValue(1);
		database.child(MenuActivity.ClickRoomName).child("Winner").setValue(0);
	}

	@Override
	public void onBackPressed()
	{
		if (Prekol < 10)
		{
			if (Prekol % 2 == 0)
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
		this.setTitle(getString(R.string.app_name) + " - " + MenuActivity.ClickRoomName);

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
		RadioR = (RadioButton) findViewById(R.id.RadioR);
		RadioY = (RadioButton) findViewById(R.id.RadioY);
		RadioG = (RadioButton) findViewById(R.id.RadioG);
		RadioB = (RadioButton) findViewById(R.id.RadioB);
		ColorView = (TextView) findViewById(R.id.ColorView);
		GiveTurn = (Button) findViewById(R.id.GiveTurn);
		Quit = (Button) findViewById(R.id.Quit);
		CloseRoom = (Button) findViewById(R.id.CloseRoom);
		ConnectedPlayersText = (TextView) findViewById(R.id.ConnectedPlayersText);
		MaxDrawText = (TextView) findViewById(R.id.MaxDrawText);
		//endregion

		//Получим начальные значения и делаем свои штуки
		database.child(MenuActivity.ClickRoomName).addListenerForSingleValueEvent(new ValueEventListener()
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
									case "R":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
										break;
									case "Y":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
										break;
									case "G":
										ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
										break;
									case "B":
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
							BaseMaxDraw = child.getValue().toString();
							break;
						case "NewCard":
							BaseNewCard = child.getValue().toString();
							break;
						case "TurnDir":
							BaseTurnDir = child.getValue().toString();
							break;
					}

					if (child.getKey().compareTo("Player-"+Reconnect) == 0)
					{
						String [] Cards = child.child("Cards").getValue().toString().split(";");

						for (int i = 0; i < Cards.length; i++)
							if (Cards[i].compareTo("") != 0)
								HandCards.add(Cards[i]);
					}
				}

				firstTime = false;

				if (Reconnect == 0)
				{
					Player = Integer.valueOf(BaseConnectedPlayers) + 1;
					database.child(MenuActivity.ClickRoomName).child("ConnectedPlayers").setValue(Player);
				}
				else
				{
					Player = Reconnect;

					BaseCurrentPlayer = dataSnapshot.child("CurrentPlayer").getValue().toString();
					BaseMaxDraw = dataSnapshot.child("MaxDraw").getValue().toString();
					BaseCard = dataSnapshot.child("Card").getValue().toString();
					BaseConnectedPlayers = dataSnapshot.child("ConnectedPlayers").getValue().toString();
					BaseTurns = Integer.valueOf(dataSnapshot.child("Turns").getValue().toString());
					BaseTurnDir = dataSnapshot.child("TurnDir").getValue().toString();
					BaseColor = dataSnapshot.child("Color").getValue().toString();

					MaxDrawText.setText(BaseMaxDraw);

					Log.e("Pidor", Player.toString() + " " + BaseCurrentPlayer);

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
				}

				if (Player - 1 == 0)
				{
					ServerThing();

					CloseRoom.setVisibility(View.VISIBLE);

					database.child(MenuActivity.ClickRoomName).child("Turns").setValue(0);

					PlayerTurn.setText(getString(R.string.MyTurn));
				}

				//Запускаем Service для обработки ходов и отображения даже во время блокировки экрана
				startService(new Intent(MainActivity.this, TurnExplorer.class));

				DrawHand();

				if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);

					TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
					String IMEI = tm.getDeviceId();
					database.child(MenuActivity.ClickRoomName).child("Player-" + Player.toString()).child("IMEI").setValue(IMEI);
				}else {
					TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
					String IMEI = tm.getDeviceId();
					database.child(MenuActivity.ClickRoomName).child("Player-" + Player.toString()).child("IMEI").setValue(IMEI);
				}

			}

			@Override
			public void onCancelled(DatabaseError databaseError) { }
		});

		//Обновляем значения каждый раз при изминении и делаем свои штуки
		database.child(MenuActivity.ClickRoomName).addChildEventListener(new ChildEventListener()
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
							DrawHand();
							break;
						case "Color":
							BaseColor = dataSnapshot.getValue().toString();
							if (BaseColor.compareTo("0") != 0)
								ColorView.setVisibility(View.VISIBLE);
							else
								ColorView.setVisibility(View.INVISIBLE);
							switch (BaseColor)
							{
								case "R":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
									break;
								case "Y":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
									break;
								case "G":
									ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
									break;
								case "B":
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
									database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(7);
								database.child(MenuActivity.ClickRoomName).child("Turns").setValue(BaseTurns + 1);
							}

							if (Integer.valueOf(BaseCurrentPlayer) <= Integer.valueOf(BaseConnectedPlayers) && Integer.valueOf(BaseCurrentPlayer) >= 1)
								if (BaseCurrentPlayer.compareTo(Player.toString()) == 0)
								{
									PlayerTurn.setText(getString(R.string.MyTurn));
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

									new Thread(new Runnable()
									{
										@Override
										public void run()
										{
											try
											{
												if (Player - 1 == 0)
												{
													Thread.sleep(500);
													database.child(MenuActivity.ClickRoomName).removeValue();
													//Остановить службу
													stopService(new Intent(MainActivity.this, TurnExplorer.class));
												}
												finish();
											} catch (InterruptedException e) { e.printStackTrace(); }
										}
									}).start();
								} else
								{
									Toast.makeText(MainActivity.this, getString(R.string.PlayerPreLabelText) + " " + dataSnapshot.getValue().toString() + " " + getString(R.string.PlayerPreWinLabelText), Toast.LENGTH_LONG).show();
									database.child(MenuActivity.ClickRoomName).child("Winner").setValue(0);
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
							database.child(MenuActivity.ClickRoomName).child("NewCard").setValue(card);
							Cards.remove(card);
						}

						//Комната удаляется если игра не активна более 10 минут
						Calendar c = Calendar.getInstance();
						int mm = c.get(Calendar.MINUTE);
						database.child(MenuActivity.ClickRoomName).child("Date").setValue(mm);

						if (Integer.valueOf(BaseCurrentPlayer) > Integer.valueOf(BaseConnectedPlayers))
							database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(1);

						if (Integer.valueOf(BaseCurrentPlayer) < 1)
							database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Integer.valueOf(BaseConnectedPlayers));
					}
				} catch (Exception e)
				{
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
					//Остановить службу
					stopService(new Intent(MainActivity.this, TurnExplorer.class));
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

				if (RadioR.isChecked())
				{
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("R");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorRed));
				}
				if (RadioY.isChecked())
				{
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("Y");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorYellow));
				}
				if (RadioG.isChecked())
				{
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("G");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorGreen));
				}
				if (RadioB.isChecked())
				{
					database.child(MenuActivity.ClickRoomName).child("Color").setValue("B");
					ColorView.setText(getString(R.string.ColorOrder) + "\n" + getString(R.string.ColorBlue));
				}

				database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));
			}
		});

		CloseRoom.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child(MenuActivity.ClickRoomName).removeValue();
				Toast.makeText(MainActivity.this, getString(R.string.RoomLabelText) + " " + MenuActivity.ClickRoomName + " " + getString(R.string.RoomDelLabelText), Toast.LENGTH_LONG).show();
				//Остановить службу
				stopService(new Intent(MainActivity.this, TurnExplorer.class));
				finish();
			}
		});

		Quit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				database.child(MenuActivity.ClickRoomName).child("ConnectedPlayers").setValue(Integer.valueOf(BaseConnectedPlayers) - 1);
				//Остановить службу
				stopService(new Intent(MainActivity.this, TurnExplorer.class));
				finish();
			}
		});

		GiveTurn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (Integer.valueOf(BaseMaxDraw) - 1 > 0)
					database.child(MenuActivity.ClickRoomName).child("MaxDraw").setValue(Integer.valueOf(BaseMaxDraw) - 1);
				else
					database.child(MenuActivity.ClickRoomName).child("CurrentPlayer").setValue(Player + 1 * Integer.valueOf(BaseTurnDir));

				GiveTurn.setVisibility(View.INVISIBLE);
			}
		});

		LeftCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset + 1 <= HandCards.size() - 5)
				{
					CardOffset++;
					DrawHand();
				}
			}
		});

		RightCard0.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if (CardOffset - 1 >= 0)
				{
					CardOffset--;
					DrawHand();
				}
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
							{
								HandCards.add(input.getText().toString().split("-")[1]);
								DrawHand();
							}
							else
								Toast.makeText(MainActivity.this, "Чит имеет вид \"=ЧДПК3228-'Название карты'\"", Toast.LENGTH_LONG).show();

						} else
							database.child(MenuActivity.ClickRoomName).child("Msg").setValue(getString(R.string.PlayerLabelText) + " " + Player + ": " + input.getText().toString());
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