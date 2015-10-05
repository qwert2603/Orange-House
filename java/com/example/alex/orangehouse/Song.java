package com.example.alex.orangehouse;

public class Song {

    private int mSongId;
    private int mTitleId;

    public Song(int songId, int titleId) {
        mSongId = songId;
        mTitleId = titleId;
    }

    public int getSongId() {
        return mSongId;
    }

    public int getTitleId() {
        return mTitleId;
    }

}
