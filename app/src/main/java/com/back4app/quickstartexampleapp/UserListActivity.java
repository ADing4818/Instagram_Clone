package com.back4app.quickstartexampleapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity
{
    ListView my_listView;
    List<String> user_list;
    ArrayAdapter<String> adapter;
    Intent intent;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        my_listView = findViewById(R.id.listView);
        user_list = new ArrayList<>();

        ParseQuery<ParseUser> parse_user = ParseUser.getQuery();
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

        intent = getIntent();
        username = intent.getStringExtra("username");
    }

    /* Creates Menu tab */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Starts an activity to the photo gallery where the user can choose a photo to upload */
    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    /* Callback function when user accepts or declines permission from the device to access the camera and photos */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == 1) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getPhoto();
        }
    }

    /* Function that is called when the user clicks an item of the menu tab */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            else getPhoto();
        }
        return super.onOptionsItemSelected(item);
    }

    /* Function that is called when the user chooses image from gallery. Picture will be uploaded to parse server */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            try {
                Bitmap res = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);

                /* Compressing resulting bitmap to an output_stream of PNG format, then the output stream is converted to a byte array */
                ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
                res.compress(Bitmap.CompressFormat.PNG, 100, output_stream);
                byte[] byte_array = output_stream.toByteArray();

                /* Creating new ParseFile from the byte array and naming it "image.png" */
                ParseFile file = new ParseFile("image.png", byte_array);

                /* Creating new class in the parse server for uploaded images */
                ParseObject image_class = new ParseObject("Images");
                image_class.put("image", file);
                image_class.put("username", username);
                image_class.saveInBackground();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
