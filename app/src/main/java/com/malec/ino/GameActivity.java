package com.malec.ino;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class GameActivity extends AppCompatActivity
{
    Random rnd = new Random();
    DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();

    public static ImageView Deck, CurrentCard, AnimationCard;
    TextView ConnectedPlayersText, CurrentPlayerText, MaxDrawText, HandCardsCountText, ColorText;
    ConstraintLayout BoardLayout;
    public static LinearLayout ColorPicker;
    RadioButton RadioR, RadioY, RadioG, RadioB;
    RadioGroup ColorGroup;
    Button SubmitRadio, EndTurn;

    public static final RecyclerView[] recyclerView = new RecyclerView[1];

    static Drawable[] Cards = new Drawable[57];
    List<Integer> CardsArray = new ArrayList<>();

    public static Board board;
    public static Player player;

    static Integer CardOffset = 0;

    Boolean SERVER = false;
    public static Boolean Reconnect = false;
    int BackPressCounter = 2;

    public static MenuActivity.Room ThisRoom;

    //TODO заменить выбор цвета на квадраты с цветами

    void GenerateCards()
    {
        for (int i = 0; i < Cards.length - 1; i++)
        {
            CardsArray.add(i);
        }
    }

    public static int LoadCard(int i)
    {
        switch (i)
        {
            case 0: return R.drawable.r0;
            case 1: return R.drawable.r1;
            case 2: return R.drawable.r2;
            case 3: return R.drawable.r3;
            case 4: return R.drawable.r4;
            case 5: return R.drawable.r5;
            case 6: return R.drawable.r6;
            case 7: return R.drawable.r7;
            case 8: return R.drawable.r8;
            case 9: return R.drawable.r9;
            case 10: return R.drawable.r10;
            case 11: return R.drawable.r11;
            case 12: return R.drawable.r12;

            case 13: return R.drawable.y0;
            case 14: return R.drawable.y1;
            case 15: return R.drawable.y2;
            case 16: return R.drawable.y3;
            case 17: return R.drawable.y4;
            case 18: return R.drawable.y5;
            case 19: return R.drawable.y6;
            case 20: return R.drawable.y7;
            case 21: return R.drawable.y8;
            case 22: return R.drawable.y9;
            case 23: return R.drawable.y10;
            case 24: return R.drawable.y11;
            case 25: return R.drawable.y12;

            case 26: return R.drawable.g0;
            case 27: return R.drawable.g1;
            case 28: return R.drawable.g2;
            case 29: return R.drawable.g3;
            case 30: return R.drawable.g4;
            case 31: return R.drawable.g5;
            case 32: return R.drawable.g6;
            case 33: return R.drawable.g7;
            case 34: return R.drawable.g8;
            case 35: return R.drawable.g9;
            case 36: return R.drawable.g10;
            case 37: return R.drawable.g11;
            case 38: return R.drawable.g12;

            case 39: return R.drawable.b0;
            case 40: return R.drawable.b1;
            case 41: return R.drawable.b2;
            case 42: return R.drawable.b3;
            case 43: return R.drawable.b4;
            case 44: return R.drawable.b5;
            case 45: return R.drawable.b6;
            case 46: return R.drawable.b7;
            case 47: return R.drawable.b8;
            case 48: return R.drawable.b9;
            case 49: return R.drawable.b10;
            case 50: return R.drawable.b11;
            case 51: return R.drawable.b12;

            case 52: return R.drawable.w0;
            case 53: return R.drawable.w0;
            case 54: return R.drawable.w1;
            case 55: return R.drawable.w1;

            default: return R.drawable.empty_card;
        }
    }

    void SetImage(View v, Integer i)
    {
        try
        {
            ((ImageView) v).setImageDrawable(getResources().getDrawable(LoadCard(i)));
        } catch (Exception e)
        {
            ((ImageView) v).setImageDrawable(getResources().getDrawable(LoadCard(56)));
        }
        v.setTag(i.toString());
    }

    void GiveTurn(int TurnDir, int SkipTurn)
    {
        int NewPlayerIndex = player.ID + SkipTurn * TurnDir;
        if (board.ConnectedPlayers == 1)
        {
            NewPlayerIndex = 1;
        } else
        {
            if (NewPlayerIndex - board.ConnectedPlayers == 1)
                NewPlayerIndex = 1;
            else if (NewPlayerIndex - board.ConnectedPlayers == 2)
                NewPlayerIndex = 2;
            else if (NewPlayerIndex == 0)
                NewPlayerIndex = board.ConnectedPlayers;
            else if (NewPlayerIndex == -1)
                NewPlayerIndex = board.ConnectedPlayers - 1;
        }

        dataBase.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(NewPlayerIndex);
        dataBase.child(MenuActivity.RoomName).child("Turns").setValue(board.Turns + 1);
        recyclerView[0].getAdapter().notifyDataSetChanged();
    }

    public class Board
    {
        public Integer Card, ConnectedPlayers, CurrentPlayer, MaxDraw, NewCard, TurnDir, Turns, Color;

        Board()
        {
            this.Card = rnd.nextInt(Cards.length);
            this.ConnectedPlayers = 1;
            this.CurrentPlayer = 1;
            this.MaxDraw = 7;
            this.NewCard = rnd.nextInt(Cards.length);
            this.TurnDir = 1;
            this.Turns = 0;
            this.Color = -1;
        }

        public void UpdateBoard()
        {
            SetImage(CurrentCard, this.Card);
            ConnectedPlayersText.setText(getString(R.string.ConnectedPlayersText) + " " + this.ConnectedPlayers.toString());

            if (this.CurrentPlayer == player.ID)
                CurrentPlayerText.setText(getString(R.string.MyTurn));
            else
            {
                String ConPlName = "";
                for (Player pl : ThisRoom.Players)
                {
                    if (pl.ID == board.CurrentPlayer)
                    {
                        ConPlName = pl.Name;
                        break;
                    }
                }

                if (getString(R.string.CurrentPlayerText).compareTo("Ход игрока") == 0)
                    CurrentPlayerText.setText(getString(R.string.CurrentPlayerText) + " " + ConPlName);
                else
                    CurrentPlayerText.setText(getString(R.string.CurrentPlayerText) + " " + ConPlName + " turn");
            }

            MaxDrawText.setText(this.MaxDraw.toString());

            HandCardsCountText.setText(getString(R.string.HandCardsCountText) + " " + player.HandCards.size());

            switch (this.Color)
            {
                case 0:
                    ColorText.setText(getString(R.string.OrderedText) + "\n" + getString(R.string.RedText));
                    break;
                case 1:
                    ColorText.setText(getString(R.string.OrderedText) + "\n" + getString(R.string.YellowText));
                    break;
                case 2:
                    ColorText.setText(getString(R.string.OrderedText) + "\n" + getString(R.string.GreenText));
                    break;
                case 3:
                    ColorText.setText(getString(R.string.OrderedText) + "\n" + getString(R.string.BlueText));
                    break;
                default:
                    ColorText.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    public static class Player
    {
        public Integer ID;
        public List<Integer> HandCards;
        public String Name, Key;

        public Player InitPlayer()
        {
            this.Name = "-1";
            this.ID = -1;
            this.HandCards = new ArrayList<>();
            this.Key = "-1";

            return this;
        }

        public Player()
        {
            this.ID = 1;
            this.HandCards = new ArrayList<>();
            this.Name = "SERVER";
            this.Key = "-1";
        }
    }

    private boolean DeckTouch = true;

    //region DeckTouchListener
    private View.OnTouchListener DeckTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent)
        {
            if (board.CurrentPlayer == player.ID && board.MaxDraw >= 1 && board.NewCard != -1 && DeckTouch)
            {
                DeckTouch = false;
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(1100);
                            DeckTouch = true;
                        } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }).start();

                //Анимация
                if (true)
                {
                    //Штуки для анимации
                    Float AnimX0, AnimX1, AnimY1;
                    AnimX0 = AnimationCard.getX();
                    AnimX1 = Deck.getX() - AnimX0;
                    AnimY1 = Deck.getY() - AnimationCard.getY();

                    SetImage(AnimationCard, board.NewCard);
                    TranslateAnimation DropCard = null;
                    RecyclerView CardsRecycler = (RecyclerView)findViewById(R.id.CardsRecycler);
                    DropCard = new TranslateAnimation(AnimX1, CardsRecycler.getX() - AnimX0, AnimY1, 0);
                    DropCard.setDuration(1100);
                    AnimationCard.setVisibility(View.VISIBLE);
                    DropCard.setAnimationListener(CardReceiveAnimation);
                    AnimationCard.startAnimation(DropCard);
                }
            }

            return false;
        }
    };
    //endregion

    Animation.AnimationListener CardReceiveAnimation = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation) { }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            AnimationCard.setVisibility(View.GONE);

            player.HandCards.add(board.NewCard);

            CardOffset = player.HandCards.size() - 5;
            if (CardOffset < 0)
                CardOffset = 0;

            if (SERVER)
            {
                if (CardsArray.size() <= 0)
                    GenerateCards();
                int newcard = rnd.nextInt(CardsArray.size());
                dataBase.child(MenuActivity.RoomName).child("NewCard").setValue(CardsArray.get(newcard));
                CardsArray.remove(newcard);
            } else
                dataBase.child(MenuActivity.RoomName).child("NewCard").setValue(-1);

            board.MaxDraw--;
            if (board.MaxDraw <= 0)
            {
                if (board.Turns < board.ConnectedPlayers)
                    board.MaxDraw = 7;
                else
                    board.MaxDraw = 1;
                GiveTurn(board.TurnDir, 1);
            }
            dataBase.child(MenuActivity.RoomName).child("MaxDraw").setValue(board.MaxDraw);

            recyclerView[0].getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    };

    @Override
    public void onBackPressed()
    {
        if (BackPressCounter < 10)
        {
            if (BackPressCounter % 2 == 0)
                Toast.makeText(this, getString(R.string.DontLeave), Toast.LENGTH_SHORT).show();
            BackPressCounter++;
        } else
        {
            Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/VnGSsL"));
            startActivity(browseIntent);
            BackPressCounter = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.ToolBar);
        setSupportActionBar(myToolbar);
        this.setTitle(getString(R.string.app_name) + " - " + MenuActivity.RoomName);

        player = new Player();
        board = new Board();
        player.Name = MenuActivity.UserName;

        final CardDataAdapter adapter = new CardDataAdapter(this, player.HandCards);
        recyclerView[0] = (RecyclerView) findViewById(R.id.CardsRecycler);
        recyclerView[0].setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerView[0].setAdapter(adapter);

        Deck = (ImageView) findViewById(R.id.Deck);
        CurrentCard = (ImageView) findViewById(R.id.CurrentCard);
        AnimationCard = (ImageView) findViewById(R.id.AnimationCard);
        ConnectedPlayersText = (TextView) findViewById(R.id.ConnectedPlayersText);
        CurrentPlayerText = (TextView) findViewById(R.id.CurrentPlayerText);
        MaxDrawText = (TextView) findViewById(R.id.MaxDrawText);
        ColorText = (TextView) findViewById(R.id.ColorText);
        HandCardsCountText = (TextView) findViewById(R.id.HandCardsCountText);
        BoardLayout = (ConstraintLayout) findViewById(R.id.BoardLayout);
        ColorPicker = (LinearLayout) findViewById(R.id.ColorPicker);
        RadioR = (RadioButton) findViewById(R.id.RedColorPick);
        RadioY = (RadioButton) findViewById(R.id.YellowColorPick);
        RadioG = (RadioButton) findViewById(R.id.GreenColorPick);
        RadioB = (RadioButton) findViewById(R.id.BlueColorPick);
        ColorGroup = (RadioGroup) findViewById(R.id.ColorGroup);
        SubmitRadio = (Button) findViewById(R.id.ColorPick);

        Deck.setOnTouchListener(DeckTouchListener);

        dataBase.child(MenuActivity.RoomName).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                board.Card = Integer.valueOf(dataSnapshot.child("Card").getValue().toString());
                board.ConnectedPlayers = Integer.valueOf(dataSnapshot.child("ConnectedPlayers").getValue().toString());
                board.CurrentPlayer = Integer.valueOf(dataSnapshot.child("CurrentPlayer").getValue().toString());
                board.MaxDraw = Integer.valueOf(dataSnapshot.child("MaxDraw").getValue().toString());
                board.NewCard = Integer.valueOf(dataSnapshot.child("NewCard").getValue().toString());
                board.TurnDir = Integer.valueOf(dataSnapshot.child("TurnDir").getValue().toString());
                board.Turns = Integer.valueOf(dataSnapshot.child("Turns").getValue().toString());
                board.Color = Integer.valueOf(dataSnapshot.child("Color").getValue().toString());

                player.ID = board.ConnectedPlayers;

                if (player.ID == 1)
                {
                    SERVER = true;
                    GenerateCards();
                }

                MaxDrawText.setVisibility(View.VISIBLE);
                ConnectedPlayersText.setVisibility(View.VISIBLE);
                HandCardsCountText.setVisibility(View.VISIBLE);
                CurrentPlayerText.setVisibility(View.VISIBLE);

                board.UpdateBoard();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        dataBase.child(MenuActivity.RoomName).child("Players").addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                GameActivity.Player player = new GameActivity.Player();
                player = player.InitPlayer();
                player.Key = dataSnapshot.getKey().toString();
                player.Name = dataSnapshot.child("Name").getValue().toString();

                ThisRoom.Players.add(player);
                ThisRoom.PlayersKeys.add(player.Key);

                if (ThisRoom.Players.isEmpty())
                    ThisRoom.ConnectedPlayers = 0;
                else
                    ThisRoom.ConnectedPlayers = ThisRoom.Players.size();

                Toast.makeText(GameActivity.this, getString(R.string.PlayerLabelText) + " " + player.Name + " " + getString(R.string.PlayerConnectLabelText), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        dataBase.child(MenuActivity.RoomName).addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                switch (dataSnapshot.getKey())
                {
                    case "Card":
                        board.Card = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "ConnectedPlayers":
                        board.ConnectedPlayers = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "CurrentPlayer":
                        board.CurrentPlayer = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "MaxDraw":
                        board.MaxDraw = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "NewCard":
                        board.NewCard = Integer.valueOf(dataSnapshot.getValue().toString());

                        if (SERVER && board.NewCard == -1)
                        {
                            if (CardsArray.size() <= 0)
                                GenerateCards();
                            int newcard = rnd.nextInt(CardsArray.size());
                            dataBase.child(MenuActivity.RoomName).child("NewCard").setValue(CardsArray.get(newcard));
                            CardsArray.remove(newcard);
                        }
                        break;
                    case "TurnDir":
                        board.TurnDir = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "Turns":
                        board.Turns = Integer.valueOf(dataSnapshot.getValue().toString());
                        break;
                    case "Color":
                        board.Color = Integer.valueOf(dataSnapshot.getValue().toString());
                        ColorText.setVisibility(View.VISIBLE);
                        break;
                    case "Winner":
                        Toast.makeText(GameActivity.this, dataSnapshot.getValue().toString() + " победил!", Toast.LENGTH_SHORT).show();
                        //TODO переигровка?
                        finish();
                        dataBase.child(MenuActivity.RoomName).removeValue();
                        break;
                }

                board.UpdateBoard();
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
        SubmitRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (RadioR.isChecked())
                    dataBase.child(MenuActivity.RoomName).child("Color").setValue(0);
                if (RadioY.isChecked())
                    dataBase.child(MenuActivity.RoomName).child("Color").setValue(1);
                if (RadioG.isChecked())
                    dataBase.child(MenuActivity.RoomName).child("Color").setValue(2);
                if (RadioB.isChecked())
                    dataBase.child(MenuActivity.RoomName).child("Color").setValue(3);
                ColorGroup.clearCheck();
                ColorPicker.setVisibility(View.INVISIBLE);
                ColorText.setVisibility(View.VISIBLE);
                GiveTurn(board.TurnDir, 1);
            }
        });
        //endregion
    }
}
