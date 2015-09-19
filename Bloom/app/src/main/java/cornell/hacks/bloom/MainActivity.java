package cornell.hacks.bloom;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

//Big Red Hacks 2015
//Authors: Jimmy Stoyell, Yuqi Zhao, Han Li, Jesse Lupica
import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_activity_viewpager)
    ViewPager viewPager;

    public ArrayList<String> interests = new ArrayList<String>();
    public ArrayList<Integer> interestRatings = new ArrayList<Integer>();
    public EditText interestsEdit;
    public LinearLayout interestsLayout;
    public RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentManager fm = getSupportFragmentManager();
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fm);
        // Here you would declare which page to visit on creation
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        //This is a hackathon, isn't it?
        //Checks to see if this is the first time the user opens the app by checking the existence
        //of a file - if it doesn't exist, it's the first time, so create the file
        File f = new File("firstTimeMarker.txt");
        if(!f.exists()) {
            viewPager.setCurrentItem(0);
            Toast.makeText(this,
                    "Let's set up your profile - what are some of your interests?", Toast.LENGTH_LONG).show();
            try {
                f.createNewFile();
                FileWriter fw = new FileWriter("firstTimeMarker.txt",false);
                BufferedWriter bw=	new BufferedWriter(fw);
                bw.write(UUID.randomUUID().toString());
                bw.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }   

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0)
                return ProfileFragment.newInstance();
            else
                return CircleFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public void addToInterests(View v) {
        interests.add(interestsEdit.getText().toString());
        interestRatings.add((int)ratingBar.getRating());
        ratingBar.setRating(3);
        interestsEdit.setText("");

        TextView t = new TextView(this);
        t.setText(interests.get(interests.size() - 1) + ": " + interestRatings.get(interestRatings.size() - 1));
        interestsLayout.addView(t);
    }

    //TODO: Push profile information to the server
    public void pushProfileToServer()
    {
        String id = "";
        try {
            FileReader fr = new FileReader(new File("firstTimeMarker.txt"));
            BufferedReader br = new BufferedReader(fr);
            id = br.readLine();
            br.close();
        }
        catch(IOException e) {
            System.out.println(e);
        }

        BloomUserProfile prof = new BloomUserProfile(id, interests, interestRatings);
    }

    public static class BloomUserProfile {

        public String identifier;
        public ArrayList<String> interests;
        public ArrayList<Integer> interestRatings;

        public BloomUserProfile(String identifier, ArrayList<String> interests, ArrayList<Integer> interestRatings) {
            this.identifier = identifier;
            this.interests = interests;
            this.interestRatings = interestRatings;
        }
    }
}
