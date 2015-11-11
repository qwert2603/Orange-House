package com.example.alex.orangehouse;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class OrangePlayerService extends Service {

    private PlayList mPlayList;
    private PlayList.OnPlayingModeChangedListener mOnPlayingModeChangedListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayList = PlayList.get(this);

        makeNotification();

        mOnPlayingModeChangedListener = new PlayList.OnPlayingModeChangedListener() {
            @Override
            public void onPlayingModeChanged(PlayList ap) {
                makeNotification();
            }
        };
        mPlayList.addOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
    }

    @Override
    public void onDestroy() {
        mPlayList.removeOnPlayingModeChangedListener(mOnPlayingModeChangedListener);
        super.onDestroy();
    }

    private void makeNotification() {
        String contentText;
        int cur_song = mPlayList.getCurrentSongNumber();
        if(cur_song != -1) {
            contentText = getString(R.string.text_now);
            contentText += ": " + getString(mPlayList.getCurrentSong().getTitleId());
        }
        else {
            contentText = getString(R.string.text_nothing_is_playing);
        }
        contentText += '\n' + getString(R.string.text_next);
        contentText += ": " + getString(mPlayList.getNextSong().getTitleId());

        Notification notification = new Notification.Builder(this)
                .setTicker(getString(R.string.app_name))
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentText)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, SongListActivity.class), 0))
                .build();

        startForeground(10, notification);
    }
}
