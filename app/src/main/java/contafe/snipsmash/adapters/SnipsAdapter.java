package contafe.snipsmash.adapters;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import contafe.snipsmash.R;
import contafe.snipsmash.models.SnipObject;

/**
 * Created by Natig on 11/8/15.
 */

public class SnipsAdapter extends RecyclerView.Adapter<SnipsAdapter.ViewHolder> {
    private List<SnipObject> snips;
    public MediaPlayer mp = new MediaPlayer();
    public ImageButton currentPlaying;

    public SnipsAdapter(List<SnipObject> snips) {
        this.snips = snips;
    }

    // Create new views
    @Override
    public SnipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_snip, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
            if(currentPlaying != null){
                currentPlaying.setImageResource(R.drawable.play);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;
        viewHolder.categoryName.setText(snips.get(position).getName());
        viewHolder.chkSelected.setChecked(snips.get(position).isSelected());
        viewHolder.chkSelected.setTag(snips.get(position));

        viewHolder.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                // double clicking the same button should simply stop playing
                if(currentPlaying != viewHolder.playPauseButton) {
                    try {
                        mp = new MediaPlayer();
                        mp.setDataSource(getSnips().get(pos).getUrlAAC());
                        mp.prepare();
                        mp.start();
                        viewHolder.playPauseButton.setImageResource(R.drawable.pause);
                        currentPlaying = viewHolder.playPauseButton;
                    } catch (IOException e) {
                        Toast.makeText(v.getContext(), "ERROR_WHILE_PLAYING:" + e, Toast.LENGTH_LONG);
                    }
                }
            }
        });

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SnipObject snip = (SnipObject) cb.getTag();

                snip.setSelected(cb.isChecked());
                snips.get(pos).setSelected(cb.isChecked());

//                Toast.makeText(
//                        v.getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return snips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public CheckBox chkSelected;
        public ImageButton playPauseButton;
        public SnipObject snip;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            categoryName = (TextView) itemLayoutView.findViewById(R.id.snipName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.checkSelected);
            playPauseButton = (ImageButton) itemLayoutView.findViewById(R.id.playImageButton);
            playPauseButton.setImageResource(R.drawable.play);

        }

    }

    // method to access in activity after updating selection
    public List<SnipObject> getSnips() {
        return snips;
    }
}
