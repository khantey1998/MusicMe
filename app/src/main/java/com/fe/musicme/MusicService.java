package com.fe.musicme;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Tey on 6/10/2018.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private int position;
    private ArrayList<SongInfo> listSongs;
    private final IBinder musicBind = new MusicBinder();
    private String songTitle="";
    private static final int NOTIFY_ID=1;
    private boolean shuffle=false;
    private Random rand;


    @Override
    public void onCreate() {
        super.onCreate();
        position = 0;
        mediaPlayer = new MediaPlayer();
        initMediaPlayer();
        rand = new Random();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mediaPlayer.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Intent notIntent = new Intent(this, PlayListActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void initMediaPlayer(){
        mediaPlayer.setWakeMode(getApplicationContext(),PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setListSongs(ArrayList<SongInfo> listSongs) {
        this.listSongs = listSongs;
    }

    public class MusicBinder extends Binder{
        MusicService getSevice(){
            return MusicService.this;
        }
    }

    public void playSong(){
        mediaPlayer.reset();
        SongInfo song = listSongs.get(position);
        songTitle = song.getTitle();
        long currSong = song.getId();
        String path = song.getSongUrl();
        Uri mySongUri=ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
        try {

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (Exception e) {
            Log.d("MUSIC SERVICE", "Error setting data source", e);
        }
        mediaPlayer.start();
    }


    public void setSong(int songIndex){
        position = songIndex;
    }

    public int getPosn(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDur(){
        return mediaPlayer.getDuration();
    }

    public boolean isPng(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void seek(int posn){
        mediaPlayer.seekTo(posn);
    }

    public void go(){
        mediaPlayer.start();
    }

    public void playPrev(){
        position--;
        if(position<0) position=listSongs.size()-1;
        playSong();
    }

    public void playNext(){
        if(shuffle){
            int newSong = position;
            while(newSong == position){
                newSong = rand.nextInt(listSongs.size());
            }
            position = newSong;
        }
        else {
            position++;
            if(position>listSongs.size()) position=0;
        }
        playSong();
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }
}
