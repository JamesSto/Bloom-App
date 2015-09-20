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

import android.app.Activity;
import android.widget.Toast;

import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Big Red Hacks 2015
 * @author Jimmy Stoyell
 * @author Mark Zhao
 * @author Han Li
 * @author Jesse Lupica
 */

public class MainActivity extends AppCompatActivity {

    public static ViewPager viewPager;

    public static int currIndex = 0;

    public static String ident;
    public static ArrayList<String> questions = new ArrayList<String>();
    public static ArrayList<ArrayList<String>> answerStatus = new ArrayList<>();
    public static ArrayList<Boolean> answered = new ArrayList<Boolean>();
    public static ArrayList<ArrayList<String>> answers = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        questions.add("What is your dream house?");
        questions.add("Would you ever want to try skydiving?");
        questions.add("How political are you?");
        answered.add(false);
        answered.add(false);
        ArrayList<String> q1Answers = new ArrayList<String>();
        q1Answers.add("Beach House");
        q1Answers.add("Trailer Home");
        q1Answers.add("Park Bench");
        answers.add(q1Answers);

        ArrayList<String> q2Answers = new ArrayList<String>();
        q2Answers.add("Yes");
        q2Answers.add("No");
        answers.add(q2Answers);
        ArrayList<String> q3Answers = new ArrayList<String>();
        q2Answers.add("1");
        q2Answers.add("2");
        q2Answers.add("3");
        q2Answers.add("4");
        q2Answers.add("5");
        answers.add(q3Answers);


        for(int i = 0; i< questions.size(); i++)
        {
            answerStatus.add(new ArrayList<String>());
        }
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.main_activity_viewpager);

        //This is a hackathon, isn't it?
        //Checks to see if this is the first time the user opens the app by checking the existence
        //of a file - if it doesn't exist, it's the first time, so create the file
        File f = new File("firstTimeMarker.txt");
        if(!f.exists()) {
            viewPager.setCurrentItem(0);
            Toast.makeText(this,
                    "Let's set up your profile - what are some of your interests?", Toast.LENGTH_LONG).show();
            try {
                ident = UUID.randomUUID().toString();
                f.createNewFile();
                FileWriter fw = new FileWriter("firstTimeMarker.txt",false);
                BufferedWriter bw=	new BufferedWriter(fw);
                bw.write(ident);
                bw.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
        else {
            try {
                FileReader fr = new FileReader(new File("firstTimeMarker.txt"));
                BufferedReader br = new BufferedReader(fr);
                ident = br.readLine();
                br.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        ScreenSlidePagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(fm);
        // Here you would declare which page to visit on creation
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);


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
