package com.example.alex.orangehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongListFragment extends ListFragment {

    private PlayList mPlayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        mPlayList = PlayList.get(getActivity());
        mPlayList.setRandomMode(true);
        mPlayList.addOnSongCompletionListener(new PlayList.OnSongCompletionListener() {
            @Override
            public void onSongCompletion(PlayList ap) {
                updateListView();
            }
        });
        SongListAdapter adapter = new SongListAdapter();
        setListAdapter(adapter);
    }

    private void updateListView() {
        ((SongListAdapter)getListAdapter()).notifyDataSetChanged();
        getListView().setSelection(mPlayList.getCurrentSongNumber());
    }

    @Override
    public void onResume() {
        super.onResume();
       updateListView();
    }

    @Override
    public void onDestroy() {
        mPlayList.stop();
        super.onDestroy();
    }

    private class SongListAdapter extends ArrayAdapter<Song> {

        SongListAdapter() {
            super(getActivity(), 0, mPlayList.getSongs());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, null);
                convertView.setFocusable(false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.item_song_text_view);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SongDetailsPagerActivity.class);
                    i.putExtra(SongDetailsPagerActivity.EXTRA_SONG_NUMBER, position);
                    startActivity(i);
                }
            });

            ImageView imageView = (ImageView) convertView.findViewById(R.id.item_song_image_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongDetailsFragment.onSongImageViewClick(mPlayList, position);
                    ((SongListAdapter) getListAdapter()).notifyDataSetChanged();
                }
            });

            SongDetailsFragment.makeSongPlayingDesign(imageView, textView, mPlayList, position);

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
                mPlayList.startNextSong();
                updateListView();
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