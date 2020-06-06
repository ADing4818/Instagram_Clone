package com.back4app.quickstartexampleapp;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

class HomeScreen
{
    private MainActivity main_activity;

    HomeScreen(MainActivity main_activity) {
        this.main_activity = main_activity;
    }

    boolean isEmpty(String username, String password) {
        return ((username.equals("")) || (password.equals("")));
    }

    void insertToDatabase(String username, String password)
    {
        ParseUser user = new ParseUser();

        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    main_activity.showToast("Sign up complete!");
                    main_activity.startActivity(main_activity.intent);
                }
                else main_activity.showToast(e.getMessage());
            }
        });
    }

    void login(final String username, String password)
    {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    main_activity.showToast("Log in complete!");
                    main_activity.login_username = user.getUsername();

                    Log.i("object username", main_activity.login_username);

                    main_activity.intent.putExtra("username", main_activity.login_username);
                    main_activity.startActivity(main_activity.intent);
                }
                else {
                    main_activity.showToast(e.getMessage());
                }
            }
        });
    }
}