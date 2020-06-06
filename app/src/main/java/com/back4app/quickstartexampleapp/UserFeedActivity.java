package com.back4app.quickstartexampleapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    LinearLayout layout;
    Intent intent;
    String user_feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        layout = findViewById(R.id.verticalLayout);
        intent = getIntent();
        user_feed = intent.getStringExtra("user_feed");

        setTitle(user_feed + "'s Pictures");

        /* Queries for the selected user's pictures. The pictures are post in descending order from
           when they were posted.
         */
        ParseQuery<ParseObject> query_feed = new ParseQuery<>("Images");
        query_feed.whereEqualTo("username", user_feed);
        query_feed.addDescendingOrder("createdAt");

        /* Finds all the pictures that was uploaded by the user, converts it to a Bitmap, which is then
           set by the imageview(s) created in the linear (vertical) layout.
         */
        query_feed.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if ((objects.size() > 0) && (e == null)) {
                    for (ParseObject item : objects) {
                        ParseFile file = (ParseFile)(item.get("image"));
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if ((data != null) && (e == null)) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView image = new ImageView(getApplicationContext());
                                    image.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    image.setImageBitmap(bitmap);
                                    layout.addView(image);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
