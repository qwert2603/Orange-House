package com.example.alex.orangehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SongListFragment extends ListFragment {

    private PlayList mPlayList;
    private PlayList.OnPlayingModeChangedListener mOnPlayingModeChangedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        Intent intent = new Intent(getActivity(), OrangePlayerService.class);
        getActivity().startService(intent);

        mPlayList = PlayList.get(getActivity());
        mPlayList.setRandomMode(true);
        mPlayList.setRepeatMode(false);
        mOnPlayingModeChangedListener = new PlayList.OnPlayingModeChangedListener() {
            @Override
            public void onPlayingModeChanged(PlayList ap) {
                ((SongListAdapter) getListAdapter()).notifyDataSetChanged();
            }
        };
        mPlayList.addOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
        SongListAdapter adapter = new SongListAdapter();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        final ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            MenuItem menuItemMakeNext;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int checked_count = getListView().getCheckedItemCount();
                String title = getString(R.string.text_selected_count) + " " + checked_count;
                mode.setTitle(title);
                menuItemMakeNext.setVisible(checked_count == 1);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_song_details_fragment, menu);
                menuItemMakeNext = menu.findItem(R.id.menu_item_make_next);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.menu_item_make_next) {
                    int songNumber = -1;
                    for (int i = 0; i != listView.getAdapter().getCount(); ++i) {
                        if(listView.isItemChecked(i)) {
                            songNumber = i;
                            break;
                        }
                    }
                    mPlayList.setNextSongNumber(songNumber);
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {}
        });
        return view;
    }

    @Override
    public void onDestroy() {
        if (! mPlayList.isPlaying()) {
            mPlayList.stop();

            Intent intent = new Intent(getActivity(), OrangePlayerService.class);
            getActivity().stopService(intent);
        }
        super.onDestroy();
        mPlayList.removeOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListView().setSelection(mPlayList.getCurrentSongNumber());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(), SongDetailsPagerActivity.class);
        i.putExtra(SongDetailsPagerActivity.EXTRA_SONG_NUMBER, position);
        startActivity(i);
    }

    private class SongListAdapter extends ArrayAdapter<Song> {

        SongListAdapter() {
            super(getActivity(), 0, mPlayList.getSongs());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_song, parent, false);
            }

            TextView textView = (TextView) convertView.findViewById(R.id.item_song_text_view);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.item_song_image_view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SongDetailsFragment.onSongClick(mPlayList, position);
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
        if(!mPlayList.getRandomMode()) {
            MenuItem randomMenuItem = menu.findItem(R.id.menu_item_random);
            randomMenuItem.setTitle(R.string.text_random_off);
        }
        if(!mPlayList.getRepeatMode()) {
            MenuItem repeatMenuItem = menu.findItem(R.id._menu_item_repeat);
            repeatMenuItem.setTitle(R.string.text_repeat_off);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_next_song:
                mPlayList.startNextSong();
                getListView().setSelection(mPlayList.getCurrentSongNumber());
                return true;
            case R.id.menu_item_random:
                boolean newMode = !mPlayList.getRandomMode();
                mPlayList.setRandomMode(newMode);
                item.setTitle(newMode ? R.string.text_random_on : R.string.text_random_off);
                return true;
            case R.id._menu_item_repeat:
                boolean newMode2 = !mPlayList.getRepeatMode();
                mPlayList.setRepeatMode(newMode2);
                item.setTitle(newMode2 ? R.string.text_repeat_on : R.string.text_repeat_off);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}