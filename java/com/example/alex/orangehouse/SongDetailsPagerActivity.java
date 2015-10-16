package com.example.alex.orangehouse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class SongDetailsPagerActivity extends AppCompatActivity {
    public static final String EXTRA_SONG_NUMBER = "com.example.alex.orangehouse.SongDetailsPagerActivity.EXTRA_SONG_NUMBER";

    private ViewPager mViewPager;
    private PlayList mPlayList;
    private PlayList.OnPlayingModeChangedListener mOnPlayingModeChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayList = PlayList.get(this);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.view_pager_song_details);
        setContentView(mViewPager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return SongDetailsFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return mPlayList.getSongCount();
            }
        });
        int songNumber = getIntent().getIntExtra(EXTRA_SONG_NUMBER, 0);
        updateTitle();
        updateSubtitle();
        mOnPlayingModeChangedListener = new PlayList.OnPlayingModeChangedListener() {
            @Override
            public void onPlayingModeChanged(PlayList ap) {
                updateTitle();
                updateSubtitle();
            }
        };
        mPlayList.addOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
        mViewPager.setCurrentItem(songNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayList.removeOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
    }

    private void updateTitle() {
        String title;
        int cur_song = mPlayList.getCurrentSongNumber();
        if(cur_song != -1) {
            title = getString(R.string.text_now);
            title += ": " + getString(mPlayList.getCurrentSong().getTitleId());
        }
        else {
            title = getString(R.string.text_nothing_is_playing);
        }
        setTitle(title);
    }

    // поместить в подзаголовок название следующей песни
    public void updateSubtitle() {
        String subTitle  = getString(R.string.text_next);
        subTitle += ": " + getString(mPlayList.getNextSong().getTitleId());
        getSupportActionBar().setSubtitle(subTitle);
    }

}