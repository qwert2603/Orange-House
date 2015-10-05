package com.example.alex.orangehouse;

import java.util.Random;

public class PlayList {

    private Song[] mSongs = new Song[] {
            new Song(R.raw.arthaus, R.string.song_title_arthaus),
            new Song(R.raw.bez_ocif, R.string.song_title_bez_oc),
            new Song(R.raw.vse_v_por, R.string.song_title_vse_v_por),
            new Song(R.raw.vse_gorazdo, R.string.song_title_vse_gora),
            new Song(R.raw.kogda_tvoi, R.string.song_title_kogda_tvoy),
            new Song(R.raw.kontrast, R.string.song_title_kontrast),
            new Song(R.raw.lunniy, R.string.song_title_lunniy),
            new Song(R.raw.odna_zhivaya, R.string.song_title_odna_zhivaya),
            new Song(R.raw.pos_vzrivi, R.string.song_title_pod_vzrivi),
            new Song(R.raw.sigareti, R.string.song_title_sigareti_i),
            new Song(R.raw.sneg_iz, R.string.song_title_sneg_iz)
    };

    private int mCurrentSongNumber = 0;
    private boolean mRandomMode = false;
    private Random mRandom = new Random(System.currentTimeMillis());

    public Song[] getSongs() {
        return mSongs;
    }

    public Song getSongByNumber(int number) {
        return (number >= 0 && number < getSongCount()) ? mSongs[number] : null;
    }

    public Song getCurrentSong() {
        return mSongs[mCurrentSongNumber];
    }

    public Song getNextSong() {
        if(mRandomMode) {
            int i = mCurrentSongNumber;
            while(i == mCurrentSongNumber)
                i = mRandom.nextInt(getSongCount());
            mCurrentSongNumber = i;
        }
        else {
            mCurrentSongNumber = (mCurrentSongNumber + 1) / getSongCount();
        }
        return mSongs[mCurrentSongNumber];
    }

    public int getCurrentSongNumber() {
        return mCurrentSongNumber;
    }

    public int getSongCount() {
        return mSongs.length;
    }

    public void setRandomMode(boolean randomMode) {
        mRandomMode = randomMode;
    }

    public boolean getRandomMode() {
        return mRandomMode;
    }

    public Song setCurrentSongNumber(int number) {
        if(number >= 0 && number < getSongCount()) {
            mCurrentSongNumber = number;
        }
        return getCurrentSong();
    }

}
