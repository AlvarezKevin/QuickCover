package cuny.fooltech.quickcover;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private String mUsername;

    private EditText mNameEditText;
    private Button mEnterButton;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mNameEditText = (EditText) rootView.findViewById(R.id.name_edit_text);
        mEnterButton = (Button) rootView.findViewById(R.id.enter_button);

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mEnterButton.setClickable(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEnterButton.setClickable(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mNameEditText.getText().toString().trim();
                Intent intent = new Intent(getActivity(),CalendarActivity.class);
                intent.putExtra("USERNAME", mUsername);
            }
        });
        return rootView;
    }
}
