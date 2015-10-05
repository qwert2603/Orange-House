package com.example.alex.orangehouse;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private MediaPlayer mPlayer;
    private OnSongCompletionListener mListener;

    public void play(Context context, Song song) {
        if(mPlayer == null) {
            int songId = song.getSongId();
            mPlayer = MediaPlayer.create(context, songId);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();
                    if(mListener != null) {
                        mListener.onSongCompletion(AudioPlayer.this);
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
        mListener = l;
    }

    public void removeOnSongCompletionListener() {
        mListener = null;
    }

}
