package com.malec.ino;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class CardDataAdapter extends RecyclerView.Adapter<CardDataAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<Integer> Cards;
    private Context context;

    Integer SentCard, SkipTurn;

    DatabaseReference dataBase = FirebaseDatabase.getInstance().getReference();

    CardDataAdapter(Context context, List<Integer> cards)
    {
        this.Cards = cards;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CardDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new CardDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Integer card = Cards.get(position);

        holder.PassImage.setTag(Cards.get(position)+"");
        holder.PassImage.setImageDrawable(context.getResources().getDrawable(GameActivity.GetCardResource(card)));
    }

    @Override
    public int getItemCount()
    {
        return Cards.size();
    }

    Boolean CheckShape(Integer PlayerCard, Integer BoardCard)
    {
        if (PlayerCard == BoardCard)
            return true;

        if (PlayerCard - 39 == BoardCard || PlayerCard - 26 == BoardCard || PlayerCard - 13 == BoardCard)
            return true;

        if (BoardCard - 39 == PlayerCard || BoardCard - 26 == PlayerCard || BoardCard - 13 == PlayerCard)
            return true;

        return false;
    }

    Boolean CheckColor(Integer PlayerCard, Integer BoardCard)
    {
        if (PlayerCard == BoardCard)
            return true;

        if (PlayerCard / 13 >= 4)
            return true;

        if (PlayerCard / 13 == BoardCard / 13)
            return true;

        return false;
    }

    void GiveTurn(int TurnDir, int SkipTurn)
    {
        int NewPlayerIndex = GameActivity.player.ID + SkipTurn * TurnDir;
        if (GameActivity.board.ConnectedPlayers == 1)
        {
            NewPlayerIndex = 1;
        } else
        {
            if (NewPlayerIndex - GameActivity.board.ConnectedPlayers == 1)
                NewPlayerIndex = 1;
            else if (NewPlayerIndex - GameActivity.board.ConnectedPlayers == 2)
                NewPlayerIndex = 2;
            else if (NewPlayerIndex == 0)
                NewPlayerIndex = GameActivity.board.ConnectedPlayers;
            else if (NewPlayerIndex == -1)
                NewPlayerIndex = GameActivity.board.ConnectedPlayers - 1;
        }

        dataBase.child(MenuActivity.RoomName).child("CurrentPlayer").setValue(NewPlayerIndex);
        dataBase.child(MenuActivity.RoomName).child("Turns").setValue(GameActivity.board.Turns + 1);
        GameActivity.recyclerView[0].getAdapter().notifyDataSetChanged();
    }

    Animation.AnimationListener CardSendAnimation = new Animation.AnimationListener()
    {
        @Override
        public void onAnimationStart(Animation animation) { GameActivity.EndTurn.setVisibility(View.INVISIBLE); }

        @Override
        public void onAnimationEnd(Animation animation)
        {
            GameActivity.AnimationCard.setVisibility(View.GONE);

            if (SentCard / 13 == GameActivity.board.Color)
            {
                GameActivity.board.Color = -1;
                dataBase.child(MenuActivity.RoomName).child("Color").setValue(GameActivity.board.Color);
            }

            dataBase.child(MenuActivity.RoomName).child("Card").setValue(SentCard);
            if (SentCard > 51)
            {
                GameActivity.ColorPicker.setVisibility(View.VISIBLE);
            } else
                GiveTurn(GameActivity.board.TurnDir, SkipTurn);

            GameActivity.board.UpdateBoard();
            GameActivity.recyclerView[0].getAdapter().notifyDataSetChanged();

            if (GameActivity.player.HandCards.isEmpty())
                dataBase.child(MenuActivity.RoomName).child("Winner").setValue("☺" + MenuActivity.UserName);

            if (GameActivity.player.HandCards.size() == 1)
                dataBase.child(MenuActivity.RoomName).child("Winner").setValue("☻" + MenuActivity.UserName);

            dataBase.child(MenuActivity.RoomName).child("Players").child(GameActivity.player.Key).child("Cards").setValue(GameActivity.SyncCards());
        }

        @Override
        public void onAnimationRepeat(Animation animation) { }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final ImageView PassImage;
        ViewHolder(final View view)
        {
            super(view);

            view.setOnClickListener(this);

            PassImage = (ImageView)view.findViewById(R.id.CardImage);
            PassImage.setTag("0");
        }

        @Override
        public void onClick(View view)
        {
            if (GameActivity.board.CurrentPlayer == GameActivity.player.ID)
            {
                SentCard = Integer.valueOf(((ConstraintLayout) view).getChildAt(0).getTag().toString());

                if (CheckColor(SentCard, GameActivity.board.Card) || CheckShape(SentCard, GameActivity.board.Card) || (int) (SentCard / 13) == GameActivity.board.Color)
                {
                    boolean norm = false;
                    SkipTurn = 1;
                    //+2
                    if ((SentCard == 10 || SentCard == 23 || SentCard == 36 || SentCard == 49) && GameActivity.board.MaxDraw <= 2)
                    {
                        dataBase.child(MenuActivity.RoomName).child("MaxDraw").setValue(3);
                        norm = true;
                    } else
                        //Pass
                        if ((SentCard == 11 || SentCard == 24 || SentCard == 37 || SentCard == 50) && GameActivity.board.MaxDraw == 1)
                        {
                            SkipTurn = 2;
                            norm = true;
                        } else
                            //Reverse
                            if ((SentCard == 12 || SentCard == 25 || SentCard == 38 || SentCard == 51) && GameActivity.board.MaxDraw == 1)
                            {
                                if (GameActivity.board.TurnDir == 1)
                                    GameActivity.board.TurnDir = -1;
                                else
                                    GameActivity.board.TurnDir = 1;
                                dataBase.child(MenuActivity.RoomName).child("TurnDir").setValue(GameActivity.board.TurnDir);
                                norm = true;
                            } else
                                //+4
                                if ((SentCard == 54 || SentCard == 55) && GameActivity.board.MaxDraw <= 4)
                                {
                                    dataBase.child(MenuActivity.RoomName).child("MaxDraw").setValue(5);
                                    norm = true;
                                } else if (GameActivity.board.MaxDraw == 1)
                                    norm = true;

                    //Анимация
                    if (norm)
                    {
                        GameActivity.AnimationCard.setImageDrawable(context.getResources().getDrawable(GameActivity.GetCardResource(SentCard)));
                        GameActivity.AnimationCard.setVisibility(View.VISIBLE);
                        GameActivity.player.HandCards.remove(GameActivity.player.HandCards.indexOf(SentCard));
                        GameActivity.recyclerView[0].getAdapter().notifyDataSetChanged();

                        final TranslateAnimation DropCard = new TranslateAnimation(GameActivity.recyclerView[0].getX() - GameActivity.AnimationCard.getX(), GameActivity.CurrentCard.getX() - GameActivity.AnimationCard.getX(), 0, GameActivity.CurrentCard.getY() - GameActivity.AnimationCard.getY());
                        DropCard.setDuration(800);
                        DropCard.setAnimationListener(CardSendAnimation);
                        GameActivity.AnimationCard.startAnimation(DropCard);
                    }
                }
            }
        }
    }
}
