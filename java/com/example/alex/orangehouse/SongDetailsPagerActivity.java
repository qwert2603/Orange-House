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
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateTitle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        int songNumber = getIntent().getIntExtra(EXTRA_SONG_NUMBER, 0);
        updateTitle(songNumber);
        mViewPager.setCurrentItem(songNumber);
    }

    private void updateTitle(int songNumber) {
        setTitle(mPlayList.getSongByNumber(songNumber).getTitleId());
    }

}
