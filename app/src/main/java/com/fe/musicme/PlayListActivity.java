package com.fe.musicme;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {
    private ArrayList<SongInfo>_songs=new ArrayList<>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        recyclerView=findViewById(R.id.recyclerview);
        seekBar=findViewById(R.id.seekbar);

        SongInfo song=new SongInfo("Excuse Me Miss", "Shinee","http://fullgaana.com/siteuploads/files/sfd4/1522/Sia%20-%20Cheap%20Thrills%20(feat.%20Sean%20Paul)-(FullGaana.com).mp3");
        _songs.add(song);

        songAdapter=new SongAdapter(this,_songs);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(songAdapter);

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button btn, View view, SongInfo obj, int i)
            {
                try {
                    if(btn.getText().toString().equals("stop")){
                        btn.setText("Play");
                        mediaplayer.stop();
                        mediaplayer.reset();
                        mediaplayer.release();
                        mediaplayer=null;
                    }
                    else {
                    mediaplayer = new MediaPlayer();
                    mediaplayer.setDataSource(obj.getSongUrl());
                    mediaplayer.prepareAsync();
                    mediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                             mediaPlayer.start();
                             btn.setText("Stop");
                        }
                    });}
                    }
                    catch (IOException e){}
            }
        });

        mediaplayer=new MediaPlayer();
    }
}
