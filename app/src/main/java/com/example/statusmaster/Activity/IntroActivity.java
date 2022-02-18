package com.example.statusmaster.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.statusmaster.R;
import com.example.statusmaster.Utils;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Open WhatsApp", "Tap to open WhatsApp.",
                R.drawable.whatsapp_logo_white, Color.parseColor("#85C1E9")));
        addSlide(AppIntroFragment.newInstance("View Recent Status ", "View Recent Status and Open Easy Status Master.",
                R.drawable.viewimae_whit, Color.parseColor("#7DCEA0")));
        addSlide(AppIntroFragment.newInstance("Save WhatsApp Status", "Now, Save WatsApp Status!",
                R.drawable.ic_baseline_save_alt_24, Color.parseColor("#AF7AC5")));

        showSkipButton(true);
        showStatusBar(false);
        showPagerIndicator(true);
        showDoneButton(true);
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        SharedPreferences.Editor editor = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        SharedPreferences.Editor editor = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
    }


    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean("AppIntro", false);
        editor.commit();
        finish();
        super.onBackPressed();
    }
}
