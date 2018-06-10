package com.fe.musicme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;

import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {
    private ArrayList<SongInfo>_songs=new ArrayList<SongInfo>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerview);
        seekBar=(SeekBar) findViewById(R.id.seekbar);
        songAdapter=new SongAdapter(this,_songs);
    }
}
