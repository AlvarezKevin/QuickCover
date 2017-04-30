package cuny.fooltech.quickcover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 4/29/2017.
 */

public class ScheduleAdapter extends ArrayAdapter<Event> {

    ArrayList<Event> events = new ArrayList<>();

    public ScheduleAdapter(@NonNull Context context, @NonNull ArrayList<Event> objects) {
        super(context, 0, objects);
        events = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_list_item, parent, false);
        }
        Event event = events.get(position);

        String eventPosition = event.getPosition();
        int day = event.getDay();
        int month = event.getMonth();
        boolean needCover = event.isNeedCover();
        Log.v("ADAPTER", day + "+ + " + month);

        TextView timeTV = (TextView) convertView.findViewById(R.id.time_list);
        TextView dayTV = (TextView) convertView.findViewById(R.id.day_list);
        TextView positionTV = (TextView) convertView.findViewById(R.id.position_list);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.list_linear_layout);

        dayTV.setText(month + " - " + day);
        timeTV.setText(event.getStartTime() + " - " + event.getEndTime());
        positionTV.setText(eventPosition);

        if (needCover) {
            linearLayout.setBackgroundColor(getContext().getResources().getColor(R.color.red));
        }
        return convertView;
    }
}
