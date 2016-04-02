package mitso.v.homework_13.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mitso.v.homework_13.R;
import mitso.v.homework_13.interfaces.EventHandler;
import mitso.v.homework_13.models.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackViewHolder> {

    private ArrayList<Track> mTrackList;
    private EventHandler mEventHandler;

    public TrackAdapter( ArrayList<Track> tracks) {
        this.mTrackList = tracks;
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, final int position) {
        Track currentTrack = mTrackList.get(position);
        holder.mTextView_Artist.setText(currentTrack.getArtist());
        holder.mTextView_Song.setText(currentTrack.getSong());
        holder.mTextView_Duration.setText(getTimeString(currentTrack.getDuration()));
        holder.mRelativeLayout_PersonCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventHandler.chooseTrack(position);
            }
        });
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_card, parent, false);
        return new TrackViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.mEventHandler = eventHandler;
    }

    public void releaseEventHandler() {
        mEventHandler = null;
    }


    private String getTimeString(long millis) {
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}
