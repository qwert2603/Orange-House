package com.example.alex.orangehouse;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SongListFragment extends ListFragment {

    private AudioPlayer mPlayer;
    private PlayList mPlayList;
    private boolean mFirstPlaying;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPlayList = PlayList.get();
        mPlayList.setRandomMode(true);
        mFirstPlaying = true;
        SongListAdapter adapter = new SongListAdapter();
        setListAdapter(adapter);
        mPlayer = AudioPlayer.get(getActivity());
        mPlayer.setOnSongCompletionListener(new AudioPlayer.OnSongCompletionListener() {
            @Override
            public void onSongCompletion(AudioPlayer ap) {
                startNextSong();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().setSelection(mPlayList.getCurrentSongNumber());
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ImageView imageViewNewSong = (ImageView) v.findViewById(R.id.item_song_image_view);
        TextView textViewNewSong = (TextView) v.findViewById(R.id.item_song_text_view);
        if (mFirstPlaying) {
            mFirstPlaying = false;
            startNewSong(position);
            imageViewNewSong.setImageResource(R.drawable.pause);
            textViewNewSong.setTypeface(null, Typeface.BOLD);
        }
        else if (mPlayList.getCurrentSongNumber() == position) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                imageViewNewSong.setImageResource(R.drawable.play);
            } else {
                mPlayer.resume();
                imageViewNewSong.setImageResource(R.drawable.pause);
            }
        }
        else {
            View viewPrevSong = l.getChildAt(mPlayList.getCurrentSongNumber());
            if(viewPrevSong != null) {
                if (mPlayer.isPlaying()) {
                    ImageView imageViewPrevSong = (ImageView) viewPrevSong.findViewById(R.id.item_song_image_view);
                    imageViewPrevSong.setImageResource(R.drawable.play);
                }
                TextView textViewPrevSong = (TextView) viewPrevSong.findViewById(R.id.item_song_text_view);
                textViewPrevSong.setTypeface(null, Typeface.NORMAL);
            }
            mPlayer.stop();
            startNewSong(position);
            imageViewNewSong.setImageResource(R.drawable.pause);
            textViewNewSong.setTypeface(null, Typeface.BOLD);
        }
        ((SongListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private void startNewSong(int songNumber) {
        Song newSong = mPlayList.setCurrentSongNumber(songNumber);
        mPlayer.play(newSong);
    }

    private void startNextSong() {
        int prevSongNumber = mPlayList.getCurrentSongNumber();
        View viewPrevSong = getListView().getChildAt(prevSongNumber);
        if (viewPrevSong != null) {
            ImageView imageViewPrevSong = (ImageView) viewPrevSong.findViewById(R.id.item_song_image_view);
            TextView textViewPrevSong = (TextView) viewPrevSong.findViewById(R.id.item_song_text_view);
            imageViewPrevSong.setImageResource(R.drawable.play);
            textViewPrevSong.setTypeface(null, Typeface.NORMAL);
        }

        mPlayer.stop();
        Song nextSong = mPlayList.getNextSong();
        mPlayer.play(nextSong);

        int newSongNumber = mPlayList.getCurrentSongNumber();
        View viewNewSong = getListView().getChildAt(newSongNumber);
        if (viewNewSong != null) {
            ImageView imageViewNewSong = (ImageView) viewNewSong.findViewById(R.id.item_song_image_view);
            TextView textViewNewSong = (TextView) viewNewSong.findViewById(R.id.item_song_text_view);
            imageViewNewSong.setImageResource(R.drawable.pause);
            textViewNewSong.setTypeface(null, Typeface.BOLD);
        }

        ((SongListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class SongListAdapter extends ArrayAdapter<Song> {

        SongListAdapter() {
            super(getActivity(), 0, mPlayList.getSongs());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);

            Song song = getItem(position);
            TextView textView = (TextView) convertView.findViewById(R.id.item_song_text_view);
            textView.setText(song.getTitleId());

            if (!mFirstPlaying) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.item_song_image_view);
                if ((position == mPlayList.getCurrentSongNumber())) {
                    textView.setTypeface(null, Typeface.BOLD);
                    if (mPlayer.isPlaying()) {
                        imageView.setImageResource(R.drawable.pause);
                    }
                }
                else {
                    textView.setTypeface(null, Typeface.NORMAL);
                    imageView.setImageResource(R.drawable.play);
                }
            }

            return convertView;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_song_list_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_next_song:
                startNextSong();
                return true;
            case R.id.menu_item_random:
                boolean newMode = !mPlayList.getRandomMode();
                mPlayList.setRandomMode(newMode);
                item.setTitle(newMode ? R.string.text_random_on : R.string.text_random_off);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}