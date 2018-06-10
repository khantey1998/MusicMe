package com.fe.musicme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {
    private ArrayList<SongInfo>_songs=new ArrayList<>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        recyclerView=findViewById(R.id.recyclerview);
        seekBar=findViewById(R.id.seekbar);
        songAdapter=new SongAdapter(this,_songs);
    }
}
