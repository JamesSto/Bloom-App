package cornell.hacks.bloom;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


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
    public RadioGroup radioGroup;
    public boolean questionsDone;
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
        questionsDone = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);

        System.out.println("CREATING\n\n\n\n\n\n\n\n\n\n\n\n\n");
        pushProfileToServer();

        radioGroup = (RadioGroup) v.findViewById(R.id.fragment_profile_button_group);
        interestsLayout = (LinearLayout) v.findViewById(R.id.interestList);
//        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        final EditText interestsInput = (EditText) v.findViewById(R.id.interestsInput);
        final Button submitButton = (Button) v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!interestsEdit.getText().toString().trim().equals(""))
                    addToInterests();
                else {
                    Snackbar snackbar = Snackbar.make(v, "Must enter an interest",
                            Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.WHITE);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(Color.RED);
//                    TextView textView= (TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
//                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
//                    interestsEdit.setInputType(0);
//                    InputMethodManager imm = (InputMethodManager)
//                            ProfileFragment.this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(interestsEdit.getWindowToken(), 0);
                }

            }
            public void addToInterests() {
                interests.add(interestsEdit.getText().toString());
                String text = ((RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId()))
                        .getText().toString().replace("fragment_profile_", "");
                int textInt = 0;
                switch(text)
                {
                    case "Eh":textInt = 1;
                        break;
                    case "Ok":textInt = 2;
                        break;
                    case "Fine":textInt = 3;
                        break;
                    case "OMG":textInt = 4;
                        break;
                    case "ASDF":textInt = 5;
                        break;


                }
                interestRatings.add(textInt);
//                ratingBar.setRating(3);
                interestsEdit.setText("");

                final TextView t = (TextView) getLayoutInflater(null).inflate(R.layout.list_item, null);
                final int currInt = interests.size() -1;
                t.setText(interests.get(interests.size() - 1) + ": " + interestRatings.get(interestRatings.size() - 1));
                t.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(ProfileFragment.this.getActivity())
                                .setMessage("Delete " + t.getText().toString() + "?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        interestsLayout.removeView(t);
                                        interests.remove(currInt);
                                        interestRatings.remove(currInt);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        return false;
                    }
                });
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

        //Done Button
        final Button doneButton = (Button) v.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (questionsDone == true) {
                    MainActivity.viewPager.setCurrentItem(1);
                }
                else{
                    //Transition to new fragment
                    //questionsDone = true;
                    Intent intent = new Intent(ProfileFragment.this.getActivity(), QuestionAnswerActivity.class);
                    startActivity(intent);
                }
                pushProfileToServer();
            }
        });


        AutoCompleteTextView auto = (AutoCompleteTextView) interestsInput;

        auto.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.interests)));


        return v;
    }
    //TODO: Push profile information to the server
    public void pushProfileToServer()
    {
        HashMap<String, Integer> interestMap = new HashMap<>();
        int n = 0;
        for(String i : interests)
        {
            interestMap.put(i, interestRatings.get(n));
            n++;
        }

        String id = MainActivity.ident;

        JSONObject json = new JSONObject();
        try {
            json.put("interests", interestMap);
            json.put("identifier", id);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        System.out.println("SENDING INFO TO SERVER");
        System.out.println(json.toString());
        (new sendQData()).execute(json.toString());
    }

    private class sendQData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {
            try {
                System.out.println("URL CONNECTING GOING\n\n\n\n\n\n");
                String serverUrl = "http://10.128.13.169/";
                URL myURL = new URL(serverUrl);
                URLConnection c = myURL.openConnection();
                c.setRequestProperty("Profile", param[0]);
                System.out.println("Connecting");
                c.connect();
                BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String line = r.readLine();
                while(line != null)
                {
                    System.out.println("LINE OF RESPONSE TEXT: " + line);
                    line = r.readLine();
                }
                } catch (IOException e) {
                    System.out.println("THERE WAS AN OBNOXIOUS ERROR");
                    System.err.println(e);
                }
            return null;
        }

        protected void onPostExecute(String result) {

        }



    }

}
