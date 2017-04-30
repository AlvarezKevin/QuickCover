package cuny.fooltech.quickcover;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        mDayTV = (TextView)findViewById(R.id.day_request);
        mHoursTV = (TextView)findViewById(R.id.hours_request);
        mPayTV = (TextView)findViewById(R.id.rate_request);

        Event event = (Event) getIntent().getSerializableExtra("EVENT");
        String day = event.getDayOfWeek();
        String hours = event.getStartTime() + ":00 - " + event.getEndTime() + ":00";
        String pay = "$11.00";

        mDayTV.setText(day);
        mHoursTV.setText(hours);
        mPayTV.setText(pay);


    }

}
