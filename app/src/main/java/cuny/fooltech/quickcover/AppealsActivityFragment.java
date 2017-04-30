package cuny.fooltech.quickcover;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class AppealsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Event>> {

    private static final String LOG_TAG = AppealsActivityFragment.class.getSimpleName();
    private String mUsername;

    public AppealsActivityFragment() {
    }

    private ListView mListView;
    private ScheduleAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appeals, container, false);
        mUsername = getActivity().getIntent().getStringExtra("USERNAME");

        mListView = (ListView) rootView.findViewById(R.id.appeals_listview);
        mAdapter = new ScheduleAdapter(getActivity(), new ArrayList<Event>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event event = (Event) parent.getItemAtPosition(position);
                AcceptAppeal acceptAppeal = (AcceptAppeal) new AcceptAppeal().execute(event);
                AcceptAppeal2 acceptAppeal2 = (AcceptAppeal2) new AcceptAppeal2().execute(event);
            }
        });

        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return rootView;
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

                    if (!usersObject.getString("name").equals(mUsername) && usersObject.getInt("need_cover") == 1) {

                        int id = usersObject.getInt("pid");
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

                        Event event = new Event(id, name, position, day, month, year, startTime, endTime, cover);
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

    public class AcceptAppeal extends AsyncTask<Event, Void, Void> {
        @Override
        protected Void doInBackground(Event... params) {
            Event event = params[0];

            //Put user into database
            putInfoServer(event);
            return null;
        }

        private void putInfoServer(Event event) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL("http://quickcover.esy.es/update_field.php");

                Map<String, Object> params = new LinkedHashMap<>();
                params.put("db_name", "users");
                params.put("db_field_ref", "pid");
                params.put("val_ref", event.getPid());
                params.put("db_field_rep", "name");
                params.put("val_rep", mUsername);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                urlConnection.getOutputStream().write(postDataBytes);

                //For logging to make sure
                inputStream = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                String jsonStr = builder.toString();
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public class AcceptAppeal2 extends AsyncTask<Event, Void, Void> {
        @Override
        protected Void doInBackground(Event... params) {
            Event event = params[0];

            //Put user into database
            putInfoServer(event);
            return null;
        }

        private void putInfoServer(Event event) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL("http://quickcover.esy.es/update_field.php");

                Map<String, Object> params = new LinkedHashMap<>();
                params.put("db_name", "users");
                params.put("db_field_ref", "pid");
                params.put("val_ref", event.getPid());
                params.put("db_field_rep", "need_cover");
                params.put("val_rep", 0);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                urlConnection.getOutputStream().write(postDataBytes);

                //For logging to make sure
                inputStream = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
                String jsonStr = builder.toString();
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getActivity(), "This is now your shift!", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }
}

