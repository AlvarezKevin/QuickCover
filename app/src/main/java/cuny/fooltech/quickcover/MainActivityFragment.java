package cuny.fooltech.quickcover;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private String mUsername;
    private String mTags;

    private EditText mNameEditText;
    private Button mEnterButton;
    private EditText mTagsEditText;
    private EditText mExistingEditText;
    private Button mSignInButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mNameEditText = (EditText) rootView.findViewById(R.id.name_edit_text);
        mEnterButton = (Button) rootView.findViewById(R.id.enter_button);
        mTagsEditText = (EditText) rootView.findViewById(R.id.tags_edit_text);
        mExistingEditText = (EditText) rootView.findViewById(R.id.sign_in_name_edit_text);
        mSignInButton = (Button) rootView.findViewById(R.id.sign_in_button);

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTags = mTagsEditText.getText().toString().trim();
                mUsername = mNameEditText.getText().toString().trim();

                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("USERNAME", mUsername);
                //startActivity(intent);
                PutNameServer putNameServer = (PutNameServer) new PutNameServer().execute();
                startActivity(intent);
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mExistingEditText.getText().toString().trim();
                ValidateName validateName = (ValidateName) new ValidateName().execute();
            }
        });

        return rootView;
    }

    public class ValidateName extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
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
                Log.v(LOG_TAG,Integer.toString(jsonArray.length()));
                String name = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject usersObject = jsonArray.getJSONObject(i);
                    name = usersObject.getString("name");
                    Log.v(LOG_TAG,name);
                    if (name.equals(username)) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                Log.v(LOG_TAG, "Error getting json");
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                Toast.makeText(getActivity(), "No existing user with that name", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                intent.putExtra("USERNAME", mUsername);
                startActivity(intent);
            }
        }
    }

    public class PutNameServer extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String userName = mUsername;
            String tag = mTags;

            //Put user into database
            putInfoServer(mUsername, tag);
            return null;
        }

        private void putInfoServer(String username, String tag) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL("http://quickcover.esy.es/add_user.php");

                Map<String,Object> params = new LinkedHashMap<>();
                params.put("name", username);
                params.put("position", tag);
                params.put("day",null);
                params.put("month", null);
                params.put("year_",null);
                params.put("start_time",null);
                params.put("end_time",null);
                params.put("need_cover",false);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
