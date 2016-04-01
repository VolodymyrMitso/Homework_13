package mitso.v.homework_13.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mitso.v.homework_13.R;
import mitso.v.homework_13.models.Track;

public class TrackAdapter extends RecyclerView.Adapter<TrackViewHolder> {

    private ArrayList<Track> tracks;

    public TrackAdapter(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        Track currentTrack = tracks.get(position);
        holder.mTextView_Artist.setText(currentTrack.getArtist());
        holder.mTextView_Song.setText(currentTrack.getSong());
        holder.mTextView_Duration.setText(currentTrack.getDuration());
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_card, parent, false);
        return new TrackViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
