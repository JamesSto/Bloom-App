package cornell.hacks.bloom;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QuestionAnswerActivity extends AppCompatActivity {
    HashMap<String, List<String>> qA = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        LinearLayout answersBox = (LinearLayout) findViewById(R.id.activity_question_answer_answersbox);
        //Test data
        List<String> answers = new ArrayList<>();
        answers.add("Han"); answers.add("Mark");
        answers.add("Jessie"); answers.add("James");
        answers.add("HI");
        qA.put("What is your name?", answers);

        Set<String> keyset = qA.keySet();
        Iterator<String> keys = keyset.iterator();
        int enlarge = 0;
        while(keys.hasNext())
        {
            String question = keys.next();
            List<String> myAnswers = qA.get(question);
            ((TextView) findViewById(R.id.activity_question_answer_question))
                    .setText(question);

            int pxMargin = (int)convertDpToPixel(4, this);
            LinearLayout horz = new LinearLayout(this);
            for(int i = 0; i< myAnswers.size(); i++)
            {
                ToggleButton ans = (ToggleButton) getLayoutInflater().inflate(R.layout.answer_button, horz, false);
                ans.setTextOn(myAnswers.get(i));
                ans.setTextOff(myAnswers.get(i));
                ans.setChecked(false);

                if(horz.getChildCount()==2)
                {
                    int notEnlarge = enlarge==0?1: 0;
                    LinearLayout.LayoutParams loparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    loparams.setMargins(pxMargin,pxMargin,pxMargin,pxMargin);
                    horz.getChildAt(notEnlarge).setLayoutParams(loparams);
                    LinearLayout.LayoutParams loparams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    loparams1.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);
                    loparams1.weight = 1;
                    horz.getChildAt(enlarge).setLayoutParams(loparams1);
                    if(enlarge == 0)
                        enlarge =1;
                    else
                        enlarge = 0;
                    answersBox.addView(horz);
                    horz = new LinearLayout(this);
                }
                horz.addView(ans);
                if(horz.getChildCount() == 1 && i==myAnswers.size()-1)
                {
                    answersBox.addView(horz);
                }
            }
        }

        findViewById(R.id.activity_question_answer_continue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question_answer, menu);
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
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
