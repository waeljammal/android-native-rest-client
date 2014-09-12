package com.rs.skills;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rs.skills.adapter.SkillAdapter;
import com.rs.skills.type.Skill;
import com.rs.skills.type.Skills;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Main extends ListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new RequestSkillsOperation().execute("http://10.0.1.50:8080/allSkills.json");
    }

    public void handleResponse(String json)
    {
        Skills skills = null;

        try {
            skills = new ObjectMapper().readValue(json, Skills.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(skills != null)
        {
            List<Skill> skillList = skills.getSkills();
            Skill[] skillsArray = skillList.toArray(new Skill[skillList.size()]);
            ListAdapter adapter = new SkillAdapter(this, skillsArray);

            // Bind to our new adapter.
            setListAdapter(adapter);
        }
    }

    /**
     * Requests the restaurant list asynchronously
     */
    private final class RequestSkillsOperation extends AsyncTask<String, Integer, String>
    {
        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;

        private final HttpClient _client = new DefaultHttpClient();
        private ProgressDialog _dialog = new ProgressDialog(Main.this);
        private int taskType = GET_TASK;
        private static final String TAG = "RequestRestaurantsOperation";
        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        // connection timeout, in milliseconds (waiting to connect)
        private static final int CONN_TIMEOUT = 3000;

        // socket timeout, in milliseconds (waiting for data)
        private static final int SOCKET_TIMEOUT = 5000;

        @Override
        protected void onPreExecute()
        {
            _dialog.setTitle("Searching For Restaurants");
            _dialog.show();
        }

        @Override
        protected String doInBackground(String... urls)
        {
            _dialog.setMessage("Please Wait...");

            String url = urls[0];
            String result = "";

            HttpResponse response = doResponse(url);

            if (response == null) {
                return result;
            } else {

                try {

                    result = inputStreamToString(response.getEntity().getContent());

                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(String response)
        {
            _dialog.dismiss();
            handleResponse(response);
        }

        private HttpResponse doResponse(String url)
        {
            // Use our connection and data timeouts as parameters for our
            // DefaultHttpClient
            HttpClient httpclient = new DefaultHttpClient(getHttpParams());

            HttpResponse response = null;

            try {
                switch (taskType) {

                    case POST_TASK:
                        HttpPost httppost = new HttpPost(url);
                        // Add parameters
                        httppost.setEntity(new UrlEncodedFormEntity(params));

                        response = httpclient.execute(httppost);
                        break;
                    case GET_TASK:
                        HttpGet httpget = new HttpGet(url);
                        response = httpclient.execute(httpget);
                        break;
                }
            } catch (Exception e) {

                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            return response;
        }

        private String inputStreamToString(InputStream is) {

            String line = "";
            StringBuilder total = new StringBuilder();

            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                // Read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }

            // Return full string
            return total.toString();
        }

        // Establish connection and socket (data retrieval) timeouts
        private HttpParams getHttpParams() {

            HttpParams htpp = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);

            return htpp;
        }
    }
}
