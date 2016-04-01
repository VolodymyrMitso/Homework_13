package mitso.v.homework_13;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import mitso.v.homework_13.models.Track;
import mitso.v.homework_13.recycler_view.TrackAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView_TrackList;
    TrackAdapter mTrackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mRecyclerView_TrackList = (RecyclerView) findViewById(R.id.rv_TrackList_AM);
        mTrackAdapter = new TrackAdapter(getTracks());
        mRecyclerView_TrackList.setAdapter(mTrackAdapter);
        mRecyclerView_TrackList.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Track> getTracks() {

        ArrayList<Track> tracks = new ArrayList<>();
        Track track1 = new Track("Eminem", "Real Slim Shady", "300");
        Track track2 = new Track("Eminem", "Mocking Bird", "250");
        Track track3 = new Track("Eminem", "White America", "225");
        Track track4 = new Track("Eminem", "Just Lose It", "200");
        Track track5 = new Track("Eminem", "Without Me", "275");
        Collections.addAll(tracks,
                track1, track2, track3, track4, track5);
        return tracks;
    }
}
