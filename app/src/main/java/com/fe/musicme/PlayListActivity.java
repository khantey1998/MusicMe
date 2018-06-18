package com.fe.musicme;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayListActivity extends AppCompatActivity implements MediaController.MediaPlayerControl{

    private ArrayList<SongInfo>_songs=new ArrayList<>();
    RecyclerView recyclerView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaplayer;
    MusicController musicController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        recyclerView=findViewById(R.id.recyclerview);
        seekBar=findViewById(R.id.seekbar);

        SongInfo song=new SongInfo(1,"Excuse Me Miss", "Shinee");
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
        getSongList();
        Collections.sort(_songs, new Comparator<SongInfo>(){
            @Override
            public int compare(SongInfo o1, SongInfo o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        
        setMusicController();
    }
    public void getSongList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                int thisId = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                _songs.add(new SongInfo(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public void setMusicController(){
        musicController = new MusicController(this);
        musicController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        musicController.setMediaPlayer(this);
        musicController.setAnchorView(findViewById(R.id.recyclerview));
        musicController.setEnabled(true);
    }

    private void playPrev() {
    }

    private void playNext() {
    }
}
