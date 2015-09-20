package cornell.hacks.bloom;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QuestionAnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        final LinearLayout answersBox = (LinearLayout) findViewById(R.id.activity_question_answer_answersbox);


        int enlarge = 0;
        String question = MainActivity.questions.get(MainActivity.currIndex);
        ArrayList<String> myAnswers = MainActivity.answers.get(MainActivity.currIndex);
        System.out.println(myAnswers);
                ((TextView) findViewById(R.id.activity_question_answer_question))
                .setText(question);

        int pxMargin = (int) convertDpToPixel(4, this);
        LinearLayout horz = new LinearLayout(this);
        for (int i = 0; i < myAnswers.size(); i++) {
            ToggleButton ans = (ToggleButton) getLayoutInflater().inflate(R.layout.answer_button, horz, false);
            ans.setTextOn(myAnswers.get(i));
            ans.setTextOff(myAnswers.get(i));
            ans.setChecked(false);
            horz.addView(ans);
            if (horz.getChildCount() == 2) {
                int notEnlarge = enlarge == 0 ? 1 : 0;
                LinearLayout.LayoutParams loparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loparams.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);
                horz.getChildAt(notEnlarge).setLayoutParams(loparams);
                LinearLayout.LayoutParams loparams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                loparams1.setMargins(pxMargin, pxMargin, pxMargin, pxMargin);
                loparams1.weight = 1;
                horz.getChildAt(enlarge).setLayoutParams(loparams1);
                if (enlarge == 0)
                    enlarge = 1;
                else
                    enlarge = 0;
                answersBox.addView(horz);
                horz = new LinearLayout(this);
            }

            if (horz.getChildCount() == 1 && i == myAnswers.size() - 1) {
                answersBox.addView(horz);
            }
        }
        findViewById(R.id.activity_question_answer_continue).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View v){
                        //new UpdateQuestionTask().execute();
                        for(int i = 0; i< answersBox.getChildCount(); i++)
                        {
                            LinearLayout horz = (LinearLayout) answersBox.getChildAt(i);
                            for(int j = 0; j< horz.getChildCount(); j++)
                            {
                                ToggleButton but = (ToggleButton) horz.getChildAt(j);
                                if(but.isChecked())
                                {
                                    String ans = but.getText().toString();
                                    MainActivity.answerStatus.get(MainActivity.currIndex).add(ans);
                                }
                            }
                        }

                        if(MainActivity.currIndex == MainActivity.questions.size()-1)
                        {
                            finish();
                            MainActivity.viewPager.setCurrentItem(1);
                        }

                        else
                        {
                            MainActivity.currIndex++;

                            startActivity(new Intent(QuestionAnswerActivity.this, QuestionAnswerActivity.class));
                            finish();
                        }
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
    /*
    private class UpdateGPSData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... param) {
            try {
                //JSONObject me = new JSONObject();
                //me.put(param[0],)
                //HttpGet httppost = new HttpGet("http://10.128.13.169/index.php?User-Agent="+MainActivity.ident+
                //        "&profile"+);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    return data;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {

        }




    }*/
}


