package cornell.hacks.bloom;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public ArrayList<String> interests = new ArrayList<String>();
    public ArrayList<Integer> interestRatings = new ArrayList<Integer>();
    public EditText interestsEdit;
    public LinearLayout interestsLayout;
    public RatingBar ratingBar;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        interestsLayout = (LinearLayout) v.findViewById(R.id.interestList);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        final EditText interestsInput = (EditText) v.findViewById(R.id.interestsInput);
        final Button submitButton = (Button) v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!interestsEdit.getText().toString().trim().equals(""))
                    addToInterests(v);
                else {
                    Snackbar snackbar = Snackbar.make(v, "Must enter an interest",
                            Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
//                    TextView textView= (TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    interestsEdit.setInputType(0);
                    InputMethodManager imm = (InputMethodManager)
                            ProfileFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(interestsEdit.getWindowToken(), 0);
                }

            }
            public void addToInterests(View v) {
                interests.add(interestsEdit.getText().toString());
                interestRatings.add((int) ratingBar.getRating());
                ratingBar.setRating(3);
                interestsEdit.setText("");

                TextView t = (TextView) getLayoutInflater(null).inflate(R.layout.list_item, null);
                t.setText(interests.get(interests.size() - 1) + ": " + interestRatings.get(interestRatings.size() - 1));
                interestsLayout.addView(t);
            }
        });
        interestsEdit = interestsInput;

        interestsInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println(v.getText().toString());
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitButton.performClick();
                    return true;
                }
                return false;
            }
        });

        AutoCompleteTextView auto = (AutoCompleteTextView) interestsInput;

        auto.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.interests)));


        return v;
    }
    //TODO: Push profile information to the server
    public void pushProfileToServer()
    {

        JSONObject json = new JSONObject();
        try {
            json.put("UUID", MainActivity.ident);
            json.put("interests", interests);
            json.put("interestRatings", interestRatings);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        try {
            String serverUrl = "http://localhost";
            URL myURL = new URL(serverUrl);
            URLConnection c = myURL.openConnection();
            c.setRequestProperty("json", json.toString());
            //TODO: Add GPS location to intial setup
            c.connect();
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}
