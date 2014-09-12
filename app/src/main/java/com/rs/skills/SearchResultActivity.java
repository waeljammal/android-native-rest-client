package com.rs.skills;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;


public class SearchResultActivity extends Activity {

    private ExpandableListView _restaurantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        setTitle("Search Results");

        _restaurantListView = (ExpandableListView)findViewById(R.id.listRestaurant);
    }
}