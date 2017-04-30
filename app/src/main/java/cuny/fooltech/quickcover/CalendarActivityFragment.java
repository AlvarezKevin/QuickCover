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

        Event event1 = new Event("John","Monday","3:00","6:00");
        Event event2 = new Event("John","Tuesday","3:00","6:00");
        Event event3 = new Event("John","Wednesday","3:00","6:00");
        Event event4 = new Event("John","Thursday","3:00","6:00");

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
