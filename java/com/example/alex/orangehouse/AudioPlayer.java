package com.example.alex.orangehouse;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mPlayer;
    private OnSongCompletionListener mCompletionListener;
    private Context mContext;
    private static AudioPlayer sAudioPlayer;

    private AudioPlayer(Context context) {
        mContext = context;
    }

    public static AudioPlayer get(Context context) {
        if(sAudioPlayer == null)
            sAudioPlayer = new AudioPlayer(context.getApplicationContext());
        return sAudioPlayer;
    }

    public void play(Song song) {
        if(mPlayer == null) {
            int songId = song.getSongId();
            mPlayer = MediaPlayer.create(mContext, songId);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                    if(mCompletionListener != null) {
                        mCompletionListener.onSongCompletion(AudioPlayer.this);
                    }
                }
            });
        }
        mPlayer.start();
    }

    public void pause() {
        if(mPlayer != null)
            mPlayer.pause();
    }

    public void resume() {
        if(mPlayer != null)
            mPlayer.start();
    }

    public void stop() {
        if(mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public interface OnSongCompletionListener {
        void onSongCompletion(AudioPlayer ap);
    }

    public void setOnSongCompletionListener(OnSongCompletionListener l) {
        mCompletionListener = l;
    }

    public void removeOnSongCompletionListener() {
        mCompletionListener = null;
    }

}
