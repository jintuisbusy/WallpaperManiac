package com.jackapps.wallpaper;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An example full-screen activity that shows the selected image in fullscreen mode
 */
public class FullscreenImageActivity extends AppCompatActivity {

    private ImageView fullImage;
    ProgressDialog progress;
    String HDimage;
    ImageView select;
    ImageView back;
    Bitmap mBitmap;
   // ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.fullscreen);
        Bundle i = getIntent().getExtras();
        String image_id = i.getString("imageurl");
        String url = "https://pixabay.com/api/?key=4587443-6a22020cf80f05f3b181d7fae&response_group=high_resolution&id=" + image_id + "&pretty=true";
        fullImage = (ImageView) findViewById(R.id.fullscreen_content);
        fetchFullResolutionImage fetch = new fetchFullResolutionImage();
        fetch.execute(url);
       // pb = (ProgressBar)findViewById(R.id.prBar);
        select = (ImageView) findViewById(R.id.apply);
        back = (ImageView) findViewById(R.id.dont_apply);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WallpaperManager w = WallpaperManager.getInstance(getApplicationContext());
                try {
                    Toast.makeText(FullscreenImageActivity.this,"Setting wallpaper...",Toast.LENGTH_SHORT).show();
                    w.setBitmap(mBitmap);
                    Toast.makeText(FullscreenImageActivity.this,"Wallpaper set",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                }
                FullscreenImageActivity.super.onBackPressed();    }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenImageActivity.super.onBackPressed();

            }
        });

        //fullImage.setImageBitmap(mBitmap);

    }

    class fetchFullResolutionImage extends AsyncTask<String, Void, JSONObject> {

        fetchFullResolutionImage() {
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(FullscreenImageActivity.this);
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();

        }

        @Override
        protected void onPostExecute(JSONObject imageURL) {
            getHdImage(imageURL);
            try {
                Glide
                        .with(FullscreenImageActivity.this)
                        .load(HDimage)
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.loading)
                        .into(new SimpleTarget<Bitmap>() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                                final int displayWidth = metrics.widthPixels;
                                final int displayHeight = metrics.heightPixels;

                                // obtain the original Bitmap's dimensions
                                final int originalWidth = resource.getWidth();
                                final int originalHeight = resource.getHeight();

                                // Obtain the horizontal and vertical scale factors
                                final float horizontalScaleFactor = (float) originalWidth / (float) displayWidth;
                                final float verticalScaleFactor = (float) originalHeight / (float) displayHeight;

                                // Get the biggest scale factor to use in order to maintain original image's aspect ratio
                                final float scaleFactor = Math.max(verticalScaleFactor, horizontalScaleFactor);
                                final int finalWidth = (int) (originalWidth / scaleFactor);
                                final int finalHeight = (int) (originalHeight / scaleFactor);

                                // Create the final bitmap
                                final Bitmap wallpaperBmp = Bitmap.createScaledBitmap(
                                        resource, finalWidth,finalHeight , true);

                                // Recycle the original bitmap
                                resource.recycle();


                                fullImage.setImageBitmap(wallpaperBmp); // Possibly runOnUiThread()
                                mBitmap = wallpaperBmp;
                                progress.dismiss();
                            }
                        });
            }catch (Exception e){
                //do nothing
            }
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

        private void getHdImage(JSONObject object) {

            try {
                JSONArray list = object.getJSONArray("hits");

                for (int i = 0; i < 1; i++) {

                    JSONObject data = list.getJSONObject(i);
                    HDimage = data.getString("fullHDURL");

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
