package com.fe.musicme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends BaseAdapter {

    private ArrayList<SongInfo> songs;
    private LayoutInflater songInf;
    public SongAdapter(Context c, ArrayList<SongInfo> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.row_song, parent, false);
        TextView songView = songLay.findViewById(R.id.textViewTitle);
        TextView artistView = songLay.findViewById(R.id.textViewSinger);
        SongInfo currSong = songs.get(position);
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getSinger());
        songLay.setTag(position);
        return songLay;
    }

//    ArrayList<SongInfo> _songs;
//    Context context;
//
//    OnItemClickListener onItemClickListener;
//
//    SongAdapter(Context context,ArrayList<SongInfo> _songs){
//        this.context=context;
//        this._songs=_songs;
//    }
//
//    public interface OnItemClickListener{
//        void onItemClick(View view);
//    }
//    public void setOnItemClickListener(OnItemClickListener onitemClickListener){
//        this.onItemClickListener=onitemClickListener;
//    }
//    @Override
//    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View myView= LayoutInflater.from(context).inflate(R.layout.row_song, viewGroup,false);
//
//        return new SongHolder(myView);
//    }
//
//    @Override
//    public void onBindViewHolder(final SongHolder holder, final int position) {
//        final SongInfo t= _songs.get(position);
//        holder.Title.setText(t.getTitle());
//        holder.Singer.setText(t.getSinger());
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return _songs.size();
//    }
//
//    public class SongHolder extends RecyclerView.ViewHolder {
//        TextView Title, Singer;
//        public SongHolder(View itemView) {
//            super(itemView);
//            Title = itemView.findViewById(R.id.textViewTitle);
//            Singer = itemView.findViewById(R.id.textViewSinger);
//        }
//    }
}
