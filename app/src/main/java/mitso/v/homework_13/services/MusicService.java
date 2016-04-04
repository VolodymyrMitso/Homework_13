package mitso.v.homework_13.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import java.util.ArrayList;

import mitso.v.homework_13.Constants.Constants;
import mitso.v.homework_13.MainActivity;
import mitso.v.homework_13.R;
import mitso.v.homework_13.models.Track;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {

    private MediaPlayer mMediaPlayer;

    private ArrayList<Track> mTrackList;
    private String mArtist;
    private String mSong;
    private int mTrackPosition;

    private IBinder mMusicBind = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mTrackPosition = -1;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        if (!isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void setTrackList(ArrayList<Track> trackList) {
        mTrackList = trackList;
    }

    public void setTrack(int trackPosition) {
        mTrackPosition = trackPosition;
    }

    public void playTrack() {
        mMediaPlayer.reset();

        Track track = mTrackList.get(mTrackPosition);
        long id = track.getId();
        mArtist = track.getArtist();
        mSong = track.getSong();

        Uri uri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

        try {
            mMediaPlayer.setDataSource(getApplicationContext(), uri);
        } catch(Exception e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

            playNextTrack();

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        sendBroadcast(new Intent(Constants.BROADCAST_BUNDLE));
        startForeground(789, getNotification());
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(mSong)
                .setContentText(mArtist)
                .setTicker(mArtist + " - " + mSong)
                .setAutoCancel(true)
                .setOngoing(true);
        return builder.build();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public String getArtist() {
        return mArtist;
    }

    public String getSong() {
        return mSong;
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void seekTo(int position){
        mMediaPlayer.seekTo(position);
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void playPreviousTrack(){
        mTrackPosition--;
        if(mTrackPosition < 0)
            mTrackPosition = mTrackList.size() - 1;
        playTrack();
    }

    public void playNextTrack(){
        mTrackPosition++;
        if(mTrackPosition >= mTrackList.size())
            mTrackPosition = 0;
        playTrack();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}
