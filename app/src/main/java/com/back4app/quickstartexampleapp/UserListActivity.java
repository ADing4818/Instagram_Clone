package com.back4app.quickstartexampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity
{
    ListView my_listView;
    List<String> user_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        my_listView = findViewById(R.id.listView);
        user_list = new ArrayList<>();

        ParseQuery<ParseUser> parse_user = ParseUser.getQuery();
        //parse_user.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        parse_user.addAscendingOrder("username");

        try {
            List<ParseUser> parse_user_list = parse_user.find();
            for (ParseUser user : parse_user_list) user_list.add(user.getUsername());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, user_list);
        my_listView.setAdapter(adapter);
    }
}
