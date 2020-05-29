package com.back4app.quickstartexampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity
{
    EditText username;
    EditText password;
    HomeScreen credentials;

    String username_string;
    String password_string;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser.getCurrentUser();
        ParseUser.logOut();

        /* Save the current Installation to Back4App */
        ParseInstallation.getCurrentInstallation().saveInBackground();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        credentials = new HomeScreen(this);
    }

    public void getInfo()
    {
        username_string = username.getText().toString();
        password_string = password.getText().toString();
    }

    public void showToast(String comment)
    {
        Toast message = Toast.makeText(this, comment, Toast.LENGTH_LONG);
        message.setGravity(Gravity.BOTTOM, 0, 175);
        message.show();
    }
    
    public void login_button(View v)
    {
        getInfo();

        if (credentials.isEmpty(username_string, password_string)) showToast("Empty fields. Please try again.");
        else credentials.login(username_string, password_string);
    }

    public void signup_button(View v)
    {
        getInfo();

        if (credentials.isEmpty(username_string, password_string)) showToast("Empty fields. Please try again.");
        else credentials.insertToDatabase(username_string, password_string);
    }
}
