package cuny.fooltech.quickcover;

import android.content.Intent;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
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

        startingHour[0]= 8;
        startingHour[1]= 10;
        startingHour[2]= 8;
        startingHour[3]= 10;

        int endHour[] = new int[4];
        endHour[0]= 17;
        endHour[1]= 20;
        endHour[2]= 17;
        endHour[3]= 20;

        int totalHours[] = new int[4];

        for(int count=0; count< endHour.length;count++){
            totalHours[count]= (12- startingHour[count])+ (endHour[count] -12);
            Log.v(LOG_TAG,Integer.toString(totalHours[count]));
        }




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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RequestActivity.class);
                intent.putExtra("EVENT", (Serializable) parent.getItemAtPosition(position));
                getActivity().startActivity(intent);
            }
        });

        return rootView;

    }
}
