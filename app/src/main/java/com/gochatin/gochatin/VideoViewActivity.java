package com.gochatin.gochatin;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;

public class VideoViewActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    android.widget.VideoView videoview;

    // Insert your Video URL
    String VideoURL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

    private ImageButton Playbutton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        VideoURL= getIntent().getStringExtra("Url");
        Log.d("url",VideoURL);

// Find your VideoViewActivity in your video_main.xml layout
        videoview = (android.widget.VideoView) findViewById(R.id.VideoView);
        Playbutton = (ImageButton)findViewById(R.id.play_button);
        // Execute StreamVideo AsyncTask


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        Playbutton.setVisibility(View.GONE);
        //pDialog.show();
        progressBar.setVisibility(View.VISIBLE);
        VideoPlay();

       /* Playbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Playbutton.setVisibility(View.GONE);
                //pDialog.show();
                progressBar.setVisibility(View.VISIBLE);
                VideoPlay();
            }
        });*/

        // Show progressbar


    }



    private void VideoPlay() {
        try {

            MediaController mediacontroller = new MediaController(VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);
			/*videoview.getBufferPercentage();
			Toast.makeText(VideoViewActivity.this,String.valueOf(videoview.getBufferPercentage()),Toast.LENGTH_LONG).show();
*/

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                //pDialog.dismiss();

                progressBar.setVisibility(View.GONE);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int h = displaymetrics.heightPixels;
                int w = displaymetrics.widthPixels;
                // Start the MediaController
                videoview.setLayoutParams(new FrameLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT));
                videoview.start();
            }
        });
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;
        // Start the MediaController
        videoview.setLayoutParams(new FrameLayout.LayoutParams(w,h));

    }





}
