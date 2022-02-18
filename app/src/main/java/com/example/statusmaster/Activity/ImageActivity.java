package com.example.statusmaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.statusmaster.R;
import com.example.statusmaster.Utils;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity{

    ZoomageView zoomageView;
    String image;
    Toolbar toolbar;
    Boolean aBoolean;
    public static final String TAG = "DM";
    LinearLayout linearLayout;
    private AdView adView;
    InterstitialAd interstitialAd;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        if (Utils.isOnline(this))
            Utils.showBannerG(this,adView,findViewById(R.id.imagebannerID));


        toolbar = findViewById(R.id.toolbarImageID);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.imagebannerID);
        zoomageView = findViewById(R.id.ivActivityID);

        image = getIntent().getExtras().getString("pathname");
        aBoolean = getIntent().getExtras().getBoolean("boolvalue",false);

        Glide.with(this).load(image).placeholder(Color.WHITE).into(zoomageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (aBoolean) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.image_manu, menu);
        }else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.image_menu_save, menu);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.sharevideosavemenuID:

                Utils.showToast(this,"Share");
                Utils.shareImage(this,Uri.parse(image));

                break;

            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sharemenuID:
                Utils.shareImage( this,Uri.parse(image));
                Utils.showToast(this,"Share Image");
                break;
            case R.id.downloadedmanuID:
                if (Utils.isOnline(this))
                    Utils.showIntrestialG(this,interstitialAd);

                try {
                    Utils.downloadMediaItem(this,new File(image));
                    Utils.showToast(this,"File save in Gallery");


                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null || adView != null) {
            interstitialAd.destroy();
            adView.destroy();
        }
        super.onDestroy();
    }

}
