package com.fe.musicme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    ArrayList<SongInfo> _songs;
    Context context;

    OnitemClickListener onitemClickListener;

    SongAdapter(Context context,ArrayList<SongInfo> _songs){
        this.context=context;
        this._songs=_songs;
    }

    public interface OnitemClickListener{
        void onitemClick(Button btn,View view, SongInfo obj, int i);
    }
    public void setOnitemClickListener(OnitemClickListener onitemClickListener){
        this.onitemClickListener=onitemClickListener;
    }
    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View myView= LayoutInflater.from(context).inflate(R.layout.row_song, viewGroup,false);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(final SongHolder holder, final int position) {
        final SongInfo t= _songs.get(position);
        holder.Title.setText(t.Title);
        holder.Singer.setText(t.Singer);
        holder.buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onitemClickListener !=null) {
                    onitemClickListener.onitemClick(holder.buttonAction, view,t, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return _songs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView Title,Singer;
        Button buttonAction;
        public SongHolder(View itemView) {
            super(itemView);
            Title=(TextView) itemView.findViewById(R.id.textViewTitle);
            Singer=(TextView) itemView.findViewById(R.id.textViewSinger);
            buttonAction=(Button) itemView.findViewById(R.id.button);

        }
    }
}