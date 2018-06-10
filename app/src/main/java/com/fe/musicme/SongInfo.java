package com.fe.musicme;

public class SongInfo {

    private String Title, Singer, SongUrl;
    private int id;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SongInfo(String title, String singer, String songUrl) {
        Title = title;
        Singer = singer;
        SongUrl = songUrl;
    }
}
