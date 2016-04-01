package mitso.v.homework_13.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mitso.v.homework_13.R;

public class TrackViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView_Artist;
    public TextView mTextView_Song;
    public TextView mTextView_Duration;

    public TrackViewHolder(View itemView) {
        super(itemView);
        mTextView_Artist = (TextView)itemView.findViewById(R.id.tv_Artist_TC);
        mTextView_Song = (TextView)itemView.findViewById(R.id.tv_Song_TC);
        mTextView_Duration = (TextView)itemView.findViewById(R.id.tv_Duration_TC);
    }
}
