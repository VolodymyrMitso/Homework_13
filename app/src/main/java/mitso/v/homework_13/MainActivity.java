package mitso.v.homework_13;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import mitso.v.homework_13.Constants.Constants;
import mitso.v.homework_13.interfaces.EventHandler;
import mitso.v.homework_13.models.Track;
import mitso.v.homework_13.recycler_view.SpacesItemDecoration;
import mitso.v.homework_13.recycler_view.TrackAdapter;
import mitso.v.homework_13.services.MusicService;

public class MainActivity extends AppCompatActivity implements EventHandler,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private RecyclerView mRecyclerView_TrackList;
    private TrackAdapter mTrackAdapter;
    private ArrayList<Track> mTrackList;
    private BroadcastReceiver mBroadcastReceiver;
    private MusicService mMusicService;
    private Intent mIntent;

    private TextView mTextView_Artist;
    private TextView mTextView_Song;
    private SeekBar mSeekBar_Position;
    private Button mButton_PreviousTrack;
    private Button mButton_PlayPause;
    private Button mButton_NextTrack;
    private TextView mTextView_CurrentPosition;
    private TextView mTextView_Duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initRecyclerView();
        initViews();
        initBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        try {
            if(mIntent == null) {
                mIntent = new Intent(this, MusicService.class);
                bindService(mIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                startService(mIntent);
            }
            registerReceiver(mBroadcastReceiver, new IntentFilter(Constants.BROADCAST_BUNDLE));
            mTrackAdapter.setEventHandler(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            if (!mMusicService.isPlaying()) {
                stopService(mIntent);
                mMusicService = null;
            }
            unbindService(serviceConnection);
            unregisterReceiver(mBroadcastReceiver);
            mTrackAdapter.releaseEventHandler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    private void initRecyclerView() {
        mRecyclerView_TrackList = (RecyclerView) findViewById(R.id.rv_TrackList_AM);
        mTrackList = getTrackList();
        Collections.sort(mTrackList);
        mTrackAdapter = new TrackAdapter(mTrackList);
        mRecyclerView_TrackList.setAdapter(mTrackAdapter);
        mRecyclerView_TrackList.setLayoutManager(new GridLayoutManager(this, 1));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.d_size_5dp);
        mRecyclerView_TrackList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    private void initViews() {
        mTextView_Artist = (TextView) findViewById(R.id.tv_Artist_AM);
        mTextView_Song = (TextView) findViewById(R.id.tv_Song_AM);
        mSeekBar_Position = (SeekBar) findViewById(R.id.sb_Position_AM);
        mButton_PreviousTrack = (Button) findViewById(R.id.btn_PreviousTrack_AM);
        mButton_PlayPause = (Button) findViewById(R.id.btn_PlayPause_AM);
        mButton_NextTrack = (Button) findViewById(R.id.btn_NextTrack_AM);
        mTextView_CurrentPosition = (TextView) findViewById(R.id.tv_CurrentPosition_AM);
        mTextView_Duration = (TextView) findViewById(R.id.tv_Duration_AM);

        if (mButton_PreviousTrack != null)
            mButton_PreviousTrack.setOnClickListener(this);
        if (mButton_PlayPause != null)
            mButton_PlayPause.setOnClickListener(this);
        if (mButton_NextTrack != null)
            mButton_NextTrack.setOnClickListener(this);
        if (mSeekBar_Position != null)
            mSeekBar_Position.setOnSeekBarChangeListener(this);
    }

    private void initBroadcastReceiver() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI();
            }
        };
    }

    private void updateUI() {
        mTextView_Artist.setText(mMusicService.getArtist());
        mTextView_Song.setText(mMusicService.getSong());
        mSeekBar_Position.setMax(mMusicService.getDuration());
        mSeekBar_Position.setProgress(0);
        mTextView_Duration.setText(getTimeString(mMusicService.getDuration()));

        final Handler handler = new Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int currentPosition = mMusicService.getCurrentPosition();
                    mSeekBar_Position.setProgress(currentPosition);
                    mTextView_CurrentPosition.setText(getTimeString(currentPosition));
                    handler.postDelayed(this, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getTimeString(long millis) {
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getService();
            mMusicService.setTrackList(mTrackList);

            if (mMusicService.isPlaying()) {
                mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_pause);
                updateUI();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ArrayList<Track> getTrackList() {
        ArrayList<Track> trackList = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String artist = cursor.getString(artistColumn);
                String song = cursor.getString(songColumn);
                int duration = cursor.getInt(durationColumn);

                trackList.add(new Track(id, artist, song, duration));
            }
            cursor.close();
        }
        return trackList;
    }

    @Override
    public void chooseTrack(int position) {
        mMusicService.setTrack(position);
        mMusicService.playTrack();
        mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_pause);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_PreviousTrack_AM:
                mMusicService.playPreviousTrack();
                mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_pause);
                break;
            case R.id.btn_PlayPause_AM:
                if (mMusicService.isPlaying()) {
                    mMusicService.pause();
                    mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_play);
                } else {
                    mMusicService.start();
                    mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_pause);
                }
                break;
            case R.id.btn_NextTrack_AM:
                mMusicService.playNextTrack();
                mButton_PlayPause.setBackgroundResource(R.drawable.sh_btn_pause);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mMusicService.seekTo(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}