package com.example.alex.orangehouse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SongDetailsFragment extends Fragment {
    private static final String currentSongNumberKey = "currentSongNumberKey";

    public static SongDetailsFragment newInstance(int number) {
        SongDetailsFragment result = new SongDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(currentSongNumberKey, number);
        result.setArguments(args);
        return result;
    }

    private PlayList mPlayList;
    private int mSongNumber;
    private Song mSong;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView textViewLyrics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPlayList = PlayList.get(getActivity());
        mPlayList.addOnSongCompletionListener(new PlayList.OnSongCompletionListener() {
            @Override
            public void onSongCompletion(PlayList ap) {
                makeSongPlayingDesign(imageView, titleTextView, mPlayList, mSongNumber);
            }
        });
        mSongNumber = getArguments().getInt(currentSongNumberKey);
        mSong = mPlayList.getSongByNumber(mSongNumber);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_details, container, false);

        titleTextView = (TextView) view.findViewById(R.id.fragment_song_details_title_text_view);

        imageView = (ImageView) view.findViewById(R.id.fragment_song_details_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSongImageViewClick(mPlayList, mSongNumber);
                makeSongPlayingDesign(imageView, titleTextView, mPlayList, mSongNumber);
            }
        });

        textViewLyrics = (TextView) view.findViewById(R.id.fragment_song_details_lyrics_text_view);
        textViewLyrics.setText(mSong.getLyricsId());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeSongPlayingDesign(imageView, titleTextView, mPlayList, mSongNumber);
    }

    // назначить imageView картинку (play/pause) и название песни в titleTextView и, если нужно, BOLD
    public static void makeSongPlayingDesign(ImageView imageView, TextView titleTextView, PlayList playList, int songNumber) {
        Song song = playList.getSongByNumber(songNumber);
        titleTextView.setText(song.getTitleId());

        imageView.setImageResource(R.drawable.play);
        titleTextView.setTypeface(null, Typeface.NORMAL);

        if (songNumber == playList.getCurrentSongNumber()) {
            titleTextView.setTypeface(null, Typeface.BOLD);
            if (playList.isPlaying()) {
                imageView.setImageResource(R.drawable.pause);
            }
        }
    }

    // обработать нажатие на imageView (включить/выключить песню)
    public static void onSongImageViewClick(PlayList playList, int songNumber) {
        if (playList.getCurrentSongNumber() == songNumber) {
            if (playList.isPlaying()) {
                playList.pause();
            } else {
                playList.resume();
            }
        }
        else {
            playList.startNewSong(songNumber);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}