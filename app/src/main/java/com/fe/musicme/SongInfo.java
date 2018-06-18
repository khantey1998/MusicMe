package com.fe.musicme;

public class SongInfo {

    private String title, singer, songUrl;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        singer = singer;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        songUrl = songUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SongInfo(int id,String title, String singer) {
        this.id = id;
        this.title = title;
        this.singer = singer;
    }
}
