package com.example.statusmaster.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.statusmaster.BuildConfig;
import com.example.statusmaster.Fragment.ImageFragment;
import com.example.statusmaster.Fragment.SaveFragment;
import com.example.statusmaster.Fragment.VideoFragment;
import com.example.statusmaster.R;
import com.example.statusmaster.RV.PagerAdapter;
import com.example.statusmaster.Utils;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar toolbar;
    boolean ischek = false;
    LinearLayout linearLayout;

    private AdView adView;


    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForAppUpdate();

        if (Utils.getAppIntro(this))
            startActivity(new Intent(this, IntroActivity.class));


        if (Utils.isOnline(this))
            Utils.showBannerG(this,adView,findViewById(R.id.mainbannerID));

        toolbar = findViewById(R.id.toolIDF);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.exit));


        linearLayout = findViewById(R.id.mainbannerID);
        viewPager = findViewById(R.id.viewPagerID);
        tabLayout = findViewById(R.id.tablayoutID);

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerAdapter.addAttachPager(new ImageFragment(),"🎪Images");
        pagerAdapter.addAttachPager(new VideoFragment(),"⌚Videos");
        pagerAdapter.addAttachPager(new SaveFragment(),"🎀Save");


        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0,true);
        tabLayout.setupWithViewPager(viewPager);


    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.whatsAppmenuID:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                    PackageManager pm = getPackageManager();

                    try {
                        // Raise exception if whatsapp doesn't exist
                        PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);

                        Intent waIntent = new Intent(Intent.ACTION_SEND);
                        waIntent.setType("text/plain");
                        waIntent.setPackage("com.whatsapp");
                        waIntent.putExtra(Intent.EXTRA_TEXT, "YOUR TEXT");
                        startActivity(waIntent);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(MainActivity.this, "Please install whatsapp app", Toast.LENGTH_SHORT)
                                .show();
                    }
                }else {
                    Toast.makeText(this, "This Feature is not Available to Android 11+ ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.helpmanuID:
                Utils.showToast(this,"Help Us");
                startActivity(new Intent(this,IntroActivity.class));

                break;
            case R.id.starmeniID:
                Utils.openRateAppDialog(this);
                break;
        }

        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ischek=false;
            }
        },3000);

        viewPager.setCurrentItem(0);

        if (ischek){
            super.onBackPressed();
        }else {
            ischek=true;
            Utils.showToast(this,"Click Again to Exit");
        }

    }
    private void checkForAppUpdate() {

        // Creates instance of the manager.
    appUpdateManager = AppUpdateManagerFactory.create(this);

    // Returns an intent object that you use to check for an update.
    Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

    // Create a listener to track request state updates.
    installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            // Show module progress, log state, or install the update.
            if (installState.installStatus() == InstallStatus.DOWNLOADED)
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdateAndUnregister();
        }
    };

    // Checks that the platform will allow the specified type of update.
    appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            // Request the update.
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                // Before starting an update, register a listener for updates.
                appUpdateManager.registerListener(installStateUpdatedListener);
                // Start an update.
                startAppUpdateFlexible(appUpdateInfo);
            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
                // Start an update.
                startAppUpdateImmediate(appUpdateInfo);
            }
        }
    });
}

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MainActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    @SuppressLint("ResourceType")
    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(getApplicationContext(), new View(this),"Hellow There", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Restart", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(Color.BLACK));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupSnackbarForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        });

    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
