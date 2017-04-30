package cuny.fooltech.quickcover;

import android.content.Intent;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CalendarActivityFragment extends Fragment {

    private static final String LOG_TAG = CalendarActivityFragment.class.getSimpleName();

    private ListView mListView;
    private ScheduleAdapter mAdapter;

    public CalendarActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        getActivity().setTitle("My Schedule");

        int startingHour[] = new int[4];

        startingHour[0]= 7;
        startingHour[1]= 6;
        startingHour[2]= 3;
        startingHour[3]= 3;

        int endHour[] = new int[4];
        endHour[0]= 6;
        endHour[1]= 10;
        endHour[2]= 11;
        endHour[3]= 6;

        Event event1 = new Event("John","Monday",startingHour[0],endHour[0]);
        Event event2 = new Event("John","Tuesday",  startingHour[1],endHour[1]);
        Event event3 = new Event("John","Wednesday",startingHour[2], endHour[2]);
        Event event4 = new Event("John","Thursday", startingHour[3],  endHour[3]);

        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);

        mListView = (ListView)rootView.findViewById(R.id.schedule_listview);
        mAdapter = new ScheduleAdapter(getActivity(),events);
        mListView.setAdapter(mAdapter);

        return rootView;

    }
}
