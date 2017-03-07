package com.jackapps.wallpaper;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NetworkStatus networkStatus;

    List<Wallpaper> listWalpapers = new ArrayList<>();
    WallpaperAdapter wallpaperArrayAdapter;
    ListView viewWallpapers;
    EditText editText;
    ImageView src;
    String txt;
    String url;
    String totalImages;
    String noOfImages;
    int seekNo = 0;
    EditText tImages;
    private ProgressDialog progress;
    InputMethodManager imm;
    WallpaperManager wm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //custom app bar declaration


            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.custom_appbar);
            View view = getSupportActionBar().getCustomView();
            getSupportActionBar().show();

            //View declarations
            src = (ImageView) view.findViewById(R.id.search);
            editText = (EditText) view.findViewById(R.id.edit);
            viewWallpapers = (ListView) findViewById(R.id.LView);
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            //Wallpaper adapter

            wallpaperArrayAdapter = new WallpaperAdapter(this, listWalpapers);
            viewWallpapers.setAdapter(wallpaperArrayAdapter);

            //Search click listener

            src.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tImages = (EditText)findViewById(R.id.nos);
                    totalImages = tImages.getText().toString();

                    if(totalImages.equalsIgnoreCase("")) {
                            noOfImages = "" + 5;
                    }else {
                        noOfImages = "" + totalImages;
                    }

                    //  Requesting wallpapers from the server
                    GetWallpaperTask getWallpaperTask = new GetWallpaperTask();
                    txt = editText.getText().toString().replace(" ", "+");
                    url = "https://pixabay.com/api/?key=YOUR_API_KEY&per_page=" + noOfImages + "&pretty=true&image_type=photo&q=" + txt + "&response_group=high_resolution";
                    getWallpaperTask.execute(url);
                    try {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (NullPointerException e) {
                    }
                    
                }
            });


            viewWallpapers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    Wallpaper photo = listWalpapers.get(position);
                    String myUrl =  photo.getHashId();
                    //t.setText(myUrl);
                    Intent i = new Intent(getApplicationContext(),FullscreenImageActivity.class);
                    i.putExtra("imageurl",myUrl);
                    startActivity(i);
                }
            });

        } catch (Exception e) {
        }

    }

    class GetWallpaperTask extends AsyncTask<String, Void, JSONObject> {

        GetWallpaperTask() {
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Connecting...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            viewWallpapers.setBackgroundColor(Color.BLACK);

        }

        @Override
        protected void onPostExecute(JSONObject wallpaper) {
            getWallper(wallpaper);
            progress.dismiss();
            wallpaperArrayAdapter.notifyDataSetChanged();
            viewWallpapers.smoothScrollToPosition(0);
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            HttpURLConnection connection = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {

                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = bufferedReader.readLine()) != null) {

                            builder.append(line);

                        }

                    } catch (Exception e) {
                    }

                    return new JSONObject(builder.toString());

                }


            } catch (Exception e) {
            }

            return null;
        }

        private void getWallper(JSONObject object) {

            listWalpapers.clear();

            try {
                JSONArray list = object.getJSONArray("hits");

                for (int i = 0; i < list.length(); i++) {

                    JSONObject data = list.getJSONObject(i);

                    listWalpapers.add(new Wallpaper
                            (
                                    data.getString("webformatURL"),
                                    data.getString("id_hash")
                            )
                    );

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

