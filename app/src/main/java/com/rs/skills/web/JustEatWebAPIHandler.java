package com.rs.skills.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.rs.skills.type.Skill;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by waeljammal on 03/09/2014.
 */
public class JustEatWebAPIHandler
{
    private List<Skill> _restauraunts;
    private Activity _targetActivity;

    public JustEatWebAPIHandler(Activity target)
    {
        _targetActivity = target;
    }

    public void FetchRestaurauntList(String postCode)
    {
        if(_restauraunts == null)
            _restauraunts = new ArrayList<Skill>();

        new RequestOperation().execute(String.format("http://api-interview.just-eat.com/restaurants?q=?", postCode));
    }

    private final class RequestOperation extends AsyncTask<String, Void, Void>
    {
        private final HttpClient _client = new DefaultHttpClient();
        private ProgressDialog _dialog = new ProgressDialog(_targetActivity);

        @Override
        protected void onPreExecute()
        {
            _dialog.setTitle("Searching For Restaurants");
            _dialog.show();
        }

        @Override
        protected Void doInBackground(String... urls)
        {
            _dialog.setMessage(urls[0]);
            return null;
        }
    }
}
