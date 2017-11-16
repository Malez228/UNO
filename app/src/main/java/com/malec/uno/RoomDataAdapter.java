package com.malec.uno;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RoomDataAdapter extends RecyclerView.Adapter<RoomDataAdapter.ViewHolder>
{
    private LayoutInflater inflater;
    private List<RoomClass> Rooms;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    RoomDataAdapter(Context context, List<RoomClass> rooms)
    {
        this.Rooms = rooms;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RoomDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.room_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomDataAdapter.ViewHolder holder, int position)
    {
        RoomClass room = Rooms.get(position);

        holder.PassImage.setImageResource(R.drawable.lock);
        holder.RoomName.setText(room.getName());
        holder.ConnectedPlayers.setText(room.getPlayers());

        if (room.getPass().compareTo("0") == 0)
            holder.PassImage.setVisibility(View.GONE);
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
