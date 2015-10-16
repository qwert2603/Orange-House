package com.example.alex.orangehouse;

import android.content.Context;

import java.util.LinkedList;
import java.util.Random;

public class PlayList {

    private Song[] mSongs;
    private int mCurrentSongNumber = -1;
    private int mNextSongNumber;
    private boolean mRandomMode = false;
    private boolean mRepeatMode = false;
    private Random mRandom = new Random(System.currentTimeMillis());
    private AudioPlayer mPlayer;
    LinkedList<OnPlayingModeChangedListener> mOnPlayingModeChangedListeners = new LinkedList<>();

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
        calculateNextSongNumber();
        mPlayer = AudioPlayer.get(context);
        mPlayer.setOnSongCompletionListener(new AudioPlayer.OnSongCompletionListener() {
            @Override
            public void onSongCompletion(AudioPlayer ap) {
                startNextSong();
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

    public Song getSongByNumber(int number) {
        return (number >= 0 && number < getSongCount()) ? mSongs[number] : null;
    }

    public Song getCurrentSong() {
        return getSongByNumber(getCurrentSongNumber());
    }

    public Song getNextSong() {
        return getSongByNumber(getNextSongNumber());
    }

    public int getNextSongNumber() {
        return (mRepeatMode && mCurrentSongNumber != -1) ? mCurrentSongNumber : mNextSongNumber;
    }

    public int getNextSongNumberIgnoringRepeat() {
        return mNextSongNumber;
    }

    public void setNextSongNumber(int number) {
        if(number >= 0 && number < getSongCount()) {
            mNextSongNumber = number;
            notifyOnPlayingModeChangedListeners();
        }
    }

    private void calculateNextSongNumber() {
        mNextSongNumber = mCurrentSongNumber;
        if (mRandomMode) {
            while (mNextSongNumber == mCurrentSongNumber)
                mNextSongNumber = mRandom.nextInt(getSongCount());
        } else {
            mNextSongNumber = (mCurrentSongNumber + 1) % getSongCount();
        }
    }

    public boolean getRandomMode() {
        return mRandomMode;
    }

    public void setRandomMode(boolean randomMode) {
        mRandomMode = randomMode;
        calculateNextSongNumber();
        notifyOnPlayingModeChangedListeners();
    }

    public boolean getRepeatMode() {
        return mRepeatMode;
    }

    public void setRepeatMode(boolean repeatMode) {
        mRepeatMode = repeatMode;
        notifyOnPlayingModeChangedListeners();
    }

    public void startNextSong() {
        startNewSong(getNextSongNumber());
    }

    public void startNewSong(int songNumber) {
        mPlayer.stop();
        Song newSong = getSongByNumber(songNumber);
        mPlayer.play(newSong);

        int prevSongNumber = mCurrentSongNumber;
        mCurrentSongNumber = songNumber;
        if(!mRepeatMode || (mCurrentSongNumber != prevSongNumber)) {
            calculateNextSongNumber();
        }

        notifyOnPlayingModeChangedListeners();
    }

    public void pause() {
        mPlayer.pause();
        notifyOnPlayingModeChangedListeners();
    }

    public void resume() {
        mPlayer.resume();
        notifyOnPlayingModeChangedListeners();
    }

    public void stop() {
        mPlayer.stop();
        notifyOnPlayingModeChangedListeners();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    // начало/пауза/продолжение/остановка воспроизведения или начало новой песни.
    // а также изменение следующей песни.
    // а также изменение режима рандома.
    // а также изменение режима повтора.
    public interface OnPlayingModeChangedListener {
        void onPlayingModeChanged(PlayList ap);
    }

    public void addOnPlayingModeChangedListener(OnPlayingModeChangedListener l) {
        mOnPlayingModeChangedListeners.add(l);
    }

    public void removeOnPlayingModeChangedListener(OnPlayingModeChangedListener l) {
        mOnPlayingModeChangedListeners.remove(l);
    }

    private void notifyOnPlayingModeChangedListeners() {
        for(OnPlayingModeChangedListener l : mOnPlayingModeChangedListeners)
            l.onPlayingModeChanged(this);
    }

}