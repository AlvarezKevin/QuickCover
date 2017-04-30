package cuny.fooltech.quickcover;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by kevin on 4/29/2017.
 */

public class ScheduleAdapter extends ArrayAdapter<Event> {
    public ScheduleAdapter(@NonNull Context context, @NonNull List<Event> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        Event event = getItem(position);

        TextView timeTV = (TextView) convertView.findViewById(R.id.time_list);
        TextView dayTV = (TextView) convertView.findViewById(R.id.day_list);
        FloatingActionButton button = (FloatingActionButton) convertView.findViewById(R.id.button_list);


        timeTV.setText(event.getStartTime() + ":00 - " + event.getEndTime()  + ":00  ");
        dayTV.setText(event.getDayOfWeek().substring(0,2).toUpperCase());
        return convertView;
    }
}
