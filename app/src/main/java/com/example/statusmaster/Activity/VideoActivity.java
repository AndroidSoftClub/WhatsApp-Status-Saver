package com.example.statusmaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.statusmaster.R;
import com.example.statusmaster.Utils;

import java.io.File;
import java.io.IOException;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    Toolbar toolbar;
    Uri uri;
    Boolean aBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoviewAID);
        toolbar = findViewById(R.id.toolbarVideoID);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MediaController mediaController = new MediaController(this);

        uri= Uri.parse(getIntent().getExtras().getString("videouri"));
        aBoolean = getIntent().getExtras().getBoolean("boolvalue",false);



        if (uri != null) {

            videoView.setVideoURI(uri);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (aBoolean){
            MenuInflater menuInflater =getMenuInflater();
            menuInflater.inflate(R.menu.video_menu,menu);
        }
        else{
            MenuInflater menuInflater =getMenuInflater();
            menuInflater.inflate(R.menu.video_menu_save,menu);

        }

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sharevideosavemenuID:

                Utils.showToast(this,"Share");
                if (String.valueOf(uri).endsWith(".mp4")) {
                    Utils.shareVideo(this,uri);
                }
                else
                    Utils.shareImage(this,uri);

                break;

            case android.R.id.home:
                onBackPressed();

                break;
            case R.id.sharevideomenuID:
                Utils.shareImage(this,uri);
                Utils.showToast(this,"Share Video");
                break;
            case R.id.downloadedvideomanuID:
                try {
                    Utils.downloadMediaItem(this,new File(String.valueOf(uri)));
                    Utils.showToast(this,"File save in Gallery");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.playvideo:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onStart() {
        videoView.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        videoView.pause();
        super.onStop();
    }

}