package com.malec.ino;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RoomDataAdapter extends RecyclerView.Adapter<RoomDataAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<MenuActivity.Room> Rooms;
    private Context context;

    RoomDataAdapter(Context context, List<MenuActivity.Room> rooms)
    {
        this.Rooms = rooms;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RoomDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.room_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomDataAdapter.ViewHolder holder, int position)
    {
        MenuActivity.Room room = Rooms.get(position);

        holder.RoomName.setText(room.Name);
        holder.ConnectedPlayers.setText(room.ConnectedPlayers.toString());

        if (room.Pass.compareTo("0") == 0)
            holder.PassImage.setVisibility(View.INVISIBLE);
        else
            holder.PassImage.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount()
    {
        return Rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final ImageView PassImage;
        final TextView RoomName, ConnectedPlayers;
        ViewHolder(final View view)
        {
            super(view);

            view.setOnClickListener(this);

            PassImage = (ImageView)view.findViewById(R.id.LockImage);
            RoomName = (TextView) view.findViewById(R.id.RoomName);
            ConnectedPlayers = (TextView) view.findViewById(R.id.ConnectedPlayers);
        }

        @Override
        public void onClick(View view)
        {
            MenuActivity.ClickRoomName = RoomName.getText().toString();
        }
    }
}
