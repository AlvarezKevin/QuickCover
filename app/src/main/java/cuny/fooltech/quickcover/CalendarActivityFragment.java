package cuny.fooltech.quickcover;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class CalendarActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Event>> {

    private static final String LOG_TAG = CalendarActivityFragment.class.getSimpleName();

    private ListView mListView;
    private ScheduleAdapter mAdapter;
    private String mUsername;

    public CalendarActivityFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        getActivity().setTitle("My Schedule");
        mUsername = getActivity().getIntent().getStringExtra("USERNAME");

        mListView = (ListView) rootView.findViewById(R.id.schedule_listview);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RequestActivity.class);
                intent.putExtra("EVENT", (Serializable) parent.getItemAtPosition(position));
                getActivity().startActivity(intent);
            }
        });

        mListView.setEmptyView(rootView.findViewById(R.id.empty_view));

        mAdapter = new ScheduleAdapter(getActivity(), new ArrayList<Event>());
        mListView.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(0, null, this).forceLoad();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<ArrayList<Event>> onCreateLoader(int id, Bundle args) {
        return new ScheduleLoader(getActivity(), mUsername);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Event>> loader, ArrayList<Event> data) {
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Event>> loader) {
        mAdapter.clear();
    }

    public static class ScheduleLoader extends AsyncTaskLoader<ArrayList<Event>> {

        String mUsername;

        public ScheduleLoader(Context context, String username) {
            super(context);
            mUsername = username;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Override
        public ArrayList<Event> loadInBackground() {

            ArrayList<Event> events = new ArrayList<Event>();

            String username = mUsername;
            String jsonStr = null;
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL("http://quickcover.esy.es/get_all_users.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //For logging to make sure
                inputStream = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                reader.readLine();
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                jsonStr = builder.toString();
                Log.v(LOG_TAG, jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                Log.v(LOG_TAG, Integer.toString(jsonArray.length()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject usersObject = jsonArray.getJSONObject(i);

                    if (usersObject.getString("name").equals(mUsername)) {

                        String name = usersObject.getString("name");
                        String position = usersObject.getString("position");
                        int day = usersObject.getInt("day");
                        int month = usersObject.getInt("month");
                        int year = usersObject.getInt("year_");
                        int startTime = usersObject.getInt("start_time");
                        int endTime = usersObject.getInt("end_time");
                        int needCover = usersObject.getInt("need_cover");
                        boolean cover = false;
                        if (needCover == 1) {
                            cover = true;
                        }
                        Log.v(LOG_TAG, name + " " + position);

                        Event event = new Event(name, position, day, month, year, startTime, endTime, cover);
                        events.add(event);
                    }

                }
            } catch (JSONException e) {
                Log.v(LOG_TAG, "Error getting json");
            }
            Log.v(LOG_TAG, Integer.toString(events.size()));
            return events;
        }
    }
}
