package com.fe.musicme;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PlayListActivity extends AppCompatActivity implements MediaPlayerControl{

    private ArrayList<SongInfo>_songs=new ArrayList<>();
    ListView listView;
    SeekBar seekBar;
    SongAdapter songAdapter;
    MediaPlayer mediaplayer;
    MusicController musicController;
    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private boolean paused=false, playbackPaused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        listView=findViewById(R.id.list_view);
        seekBar=findViewById(R.id.seekbar);
        Collections.sort(_songs, new Comparator<SongInfo>(){
            @Override
            public int compare(SongInfo o1, SongInfo o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        songAdapter=new SongAdapter(this,_songs);
        listView.setAdapter(songAdapter);

        mediaplayer=new MediaPlayer();
        getSongList();

        
        setMusicController();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            musicSrv = binder.getSevice();
            musicSrv.setListSongs(_songs);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(paused){
            setMusicController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        musicController.hide();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        musicController.setAnchorView(findViewById(R.id.list_view));
        musicController.setEnabled(true);
    }

    public void getSongList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);

            do {
                int thisId = idColumn;
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisUrl = musicUri.toString();
                _songs.add(new SongInfo(thisId, thisTitle, thisArtist, thisUrl));
            }
            while (musicCursor.moveToNext());
        }
    }

    private void playPrev() {
        musicSrv.playPrev();
        if(playbackPaused){
            setMusicController();
            playbackPaused=false;
        }
        musicController.show(0);
    }

    private void playNext() {
        musicSrv.playNext();
        if(playbackPaused){
            setMusicController();
            playbackPaused=false;
        }
        musicController.show(0);
    }

    public void songPicked(View view){

        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        if(playbackPaused){
            setMusicController();
            playbackPaused=false;
        }
        musicController.show(0);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null&&musicBound&&musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null&&musicBound&&musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null&&musicBound&&musicSrv.isPng())
            return musicSrv.isPng();
        else return false;

    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

}
