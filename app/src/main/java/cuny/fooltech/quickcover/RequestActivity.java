package cuny.fooltech.quickcover;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class RequestActivity extends AppCompatActivity {

    private TextView mDayTV;
    private TextView mHoursTV;
    private TextView mPayTV;
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

        Event event = (Event) getIntent().getSerializableExtra("EVENT");
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
    }

}
