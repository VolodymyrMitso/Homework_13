package mitso.v.homework_13.recycler_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mitso.v.homework_13.R;

public class TrackViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView_Artist;
    public TextView mTextView_Song;
    public TextView mTextView_Duration;

    public RelativeLayout mRelativeLayout_PersonCard;

    public TrackViewHolder(View item) {
        super(item);
        mTextView_Artist = (TextView) item.findViewById(R.id.tv_Artist_TC);
        mTextView_Song = (TextView) item.findViewById(R.id.tv_Song_TC);
        mTextView_Duration = (TextView) item.findViewById(R.id.tv_Duration_TC);

        mRelativeLayout_PersonCard = (RelativeLayout) item.findViewById(R.id.rl_TrackCard_TC);
    }
}
