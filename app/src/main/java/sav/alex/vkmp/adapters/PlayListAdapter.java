package sav.alex.vkmp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sav.alex.vkmp.R;
import sav.alex.vkmp.classes.Audio;

/**
 * Created by 123 on 30.01.2016.
 */
public class PlayListAdapter extends ArrayAdapter<Audio> {
    private final Context context;
    private final ArrayList <Audio> values;

    public PlayListAdapter(Context context, ArrayList <Audio> values) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.title);
        TextView artist = (TextView) rowView.findViewById(R.id.artist);
        title.setText(values.get(position).getTitle());
        artist.setText(values.get(position).getArtist());
        return rowView;
    }
}
