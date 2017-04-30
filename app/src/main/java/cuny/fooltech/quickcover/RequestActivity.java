package cuny.fooltech.quickcover;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {

    private static final String LOG_TAG = RequestActivity.class.getSimpleName();

    private TextView mDayTV;
    private TextView mHoursTV;
    private TextView mPayTV;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mDayTV = (TextView)findViewById(R.id.day_request);
        mHoursTV = (TextView)findViewById(R.id.hours_request);
        mPayTV = (TextView)findViewById(R.id.rate_request);

        final Event event = (Event) getIntent().getSerializableExtra("EVENT");
        int day = event.getDay();
        int month = event.getMonth();
        String hours = event.getStartTime() + " - " + event.getEndTime();
        String pay = "$11.00";

        switch (month) {
            case 1:
                mDayTV.setText("January " + day);
                break;
            case 2:
                mDayTV.setText("February " + day);
                break;
            case 3:
                mDayTV.setText("March " + day);
                break;
            case 4:
                mDayTV.setText("April " + day);
                break;
            case 5:
                mDayTV.setText("May " + day);
                break;
            case 6:
                mDayTV.setText("June " + day);
                break;
            case 7:
                mDayTV.setText("July " + day);
                break;
            case 8:
                mDayTV.setText("August " + day);
                break;
            case 9:
                mDayTV.setText("September " + day);
                break;
            case 10:
                mDayTV.setText("October " + day);
                break;
            case 11:
                mDayTV.setText("November " + day);
                break;
            case 12:
                mDayTV.setText("December " + day);
                break;
        }
        mHoursTV.setText("Hours: " + hours);
        mPayTV.setText(pay);


        mButton = (Button)findViewById(R.id.request_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CannotMakeIt cannotMakeIt = (CannotMakeIt) new CannotMakeIt().execute(event);

            }
        });
    }

    public class CannotMakeIt extends AsyncTask<Event,Void,Void> {
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
                URL url = new URL("http://quickcover.esy.es/update_user.php");

                Map<String,Object> params = new LinkedHashMap<>();
                params.put("pid",event.getPid());
                params.put("name", event.getName());
                params.put("position", event.getPosition());
                params.put("day",event.getDay());
                params.put("month", event.getMonth());
                params.put("year_",event.getYear());
                params.put("start_time",event.getStartTime());
                params.put("end_time",event.getEndTime());
                params.put("need_cover",1);

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(RequestActivity.this,"Request sent!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
