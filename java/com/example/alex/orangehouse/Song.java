package com.example.alex.orangehouse;

public class Song {

    private int mSongId;
    private int mTitleId;
    private int mLyricsId;

    public Song(int songId, int titleId, int lyricsId) {
        mSongId = songId;
        mTitleId = titleId;
        mLyricsId = lyricsId;
    }

    public int getSongId() {
        return mSongId;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public int getLyricsId() {
        return mLyricsId;
    }

}
