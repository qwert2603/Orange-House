package com.example.alex.orangehouse;

import android.content.Context;
import java.util.LinkedList;
import java.util.Random;

public class PlayList {

    private Song[] mSongs;
    private int mCurrentSongNumber = -1;
    private boolean mRandomMode = false;
    private Random mRandom = new Random(System.currentTimeMillis());
    private AudioPlayer mPlayer;
    LinkedList<OnSongCompletionListener> mCompletionListeners = new LinkedList<OnSongCompletionListener>();

    private static PlayList sPlayList;

    private PlayList(Context context) {
        mSongs = new Song[]{
                new Song(R.raw.arthaus, R.string.song_title_arthaus, R.string.song_lyrics_arthaus),
                new Song(R.raw.bez_ocif, R.string.song_title_bez_oc, R.string.song_lyrics_bez_oc),
                new Song(R.raw.vse_v_por, R.string.song_title_vse_v_por, R.string.song_lyrics_vse_v_por),
                new Song(R.raw.vse_gorazdo, R.string.song_title_vse_gora, R.string.song_lyrics_vse_gora),
                new Song(R.raw.kogda_tvoi, R.string.song_title_kogda_tvoy, R.string.song_lyrics_kogda_tvoy),
                new Song(R.raw.kontrast, R.string.song_title_kontrast, R.string.song_lyrics_kontrast),
                new Song(R.raw.lunniy, R.string.song_title_lunniy, R.string.song_lyrics_lunniy),
                new Song(R.raw.odna_zhivaya, R.string.song_title_odna_zhivaya, R.string.song_lyrics_odna_zhivaya),
                new Song(R.raw.pos_vzrivi, R.string.song_title_pod_vzrivi, R.string.song_lyrics_pod_vzrivi),
                new Song(R.raw.sigareti, R.string.song_title_sigareti_i, R.string.song_lyrics_sigareti_i),
                new Song(R.raw.sneg_iz, R.string.song_title_sneg_iz, R.string.song_lyrics_sneg_iz)
        };
        mPlayer = AudioPlayer.get(context);
        mPlayer.setOnSongCompletionListener(new AudioPlayer.OnSongCompletionListener() {
            @Override
            public void onSongCompletion(AudioPlayer ap) {
                startNextSong();
                for(OnSongCompletionListener l : mCompletionListeners)
                    l.onSongCompletion(PlayList.this);
            }
        });
    }

    public static PlayList get(Context context) {
        if(sPlayList == null)
            sPlayList = new PlayList(context.getApplicationContext());
        return sPlayList;
    }

    public int getSongCount() {
        return mSongs.length;
    }

    public Song[] getSongs() {
        return mSongs;
    }

    public int getCurrentSongNumber() {
        return mCurrentSongNumber;
    }

    public Song getCurrentSong() {
        return getSongByNumber(mCurrentSongNumber);
    }

    public Song getSongByNumber(int number) {
        return (number >= 0 && number < getSongCount()) ? mSongs[number] : null;
    }

    public Song setNewSongNumber(int number) {
        if(number >= 0 && number < getSongCount()) {
            mCurrentSongNumber = number;
        }
        return getCurrentSong();
    }

    private int getNextSongNumber() {
        int res = mCurrentSongNumber;
        if(mRandomMode) {
            while (res == mCurrentSongNumber)
                res = mRandom.nextInt(getSongCount());
        }
        else {
            res = (mCurrentSongNumber + 1) % getSongCount();
        }
        return res;
    }

    public boolean getRandomMode() {
        return mRandomMode;
    }

    public void setRandomMode(boolean randomMode) {
        mRandomMode = randomMode;
    }

    public void startNextSong() {
        startNewSong(getNextSongNumber());
    }

    public void startNewSong(int songNumber) {
        stop();
        Song newSong = setNewSongNumber(songNumber);
        mPlayer.play(newSong);
    }

    public void pause() {
        mPlayer.pause();
    }

    public void resume() {
        mPlayer.resume();
    }

    public void stop() {
        mPlayer.stop();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public interface OnSongCompletionListener {
        void onSongCompletion(PlayList ap);
    }

    public void addOnSongCompletionListener(OnSongCompletionListener l) {
        mCompletionListeners.add(l);
    }

    public void removeOnSongCompletionListener(OnSongCompletionListener l) {
        mCompletionListeners.remove(l);
    }

}