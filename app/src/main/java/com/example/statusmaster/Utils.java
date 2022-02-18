package com.example.statusmaster;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.statusmaster.Activity.MainActivity;
import com.example.statusmaster.Data.FileClass;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.example.statusmaster.Activity.ImageActivity.TAG;

public class Utils {

    public static String MyPREFERENCES = "PREFS";
    public static final String FILE_PROVIDER_AUTHORITY = "com.statusmaster.fileprovider";
    public static String DIRECTORY_TO_SAVE_MEDIA_NOW = "/Status Master/";
    public static String WHATSHAP_FILES= "/WhatsApp/Media/.Statuses";

    public static void showIntrestialG(Activity activity, InterstitialAd interstitialAd) {
        AudienceNetworkAds.initialize(activity);
        interstitialAd = new InterstitialAd(activity, activity.getResources().
                getString(R.string.interestial_addID));

        // Create listeners for the Interstitial Ad
        InterstitialAd finalInterstitialAd = interstitialAd;
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                finalInterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    public static void showBannerG(Activity activity,AdView adView,LinearLayout adContainer){
        AudienceNetworkAds.initialize(activity);

        if (!isTablet(activity))
            adView = new AdView(activity, activity.getResources().getString(R.string.banner_addID), AdSize.BANNER_HEIGHT_50);
        else
            adView = new AdView(activity, activity.getResources().getString(R.string.banner_addID), AdSize.BANNER_HEIGHT_90);

        adContainer.addView(adView);
        adView.loadAd();
    }

    public static void ratethisApp(Activity activity){
        Intent link=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
        activity.startActivity(link);
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static void shareImage(Activity activity, Uri uri){
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("image/*");
        shareintent.putExtra(Intent.EXTRA_STREAM, buildFileProviderUri(activity,uri));
        activity.startActivity(Intent.createChooser(shareintent ,"Share Via"));

    }

    public static void shareVideo(Activity activity,Uri uri){
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("video/mp4");
        shareintent.putExtra(Intent.EXTRA_STREAM,
                buildFileProviderUri(activity,uri));
        activity.startActivity(Intent.createChooser(shareintent ,"Share Video"));

    }

    public static Uri buildFileProviderUri(Activity activity, Uri pathname) {
        return FileProvider.getUriForFile(activity,
                FILE_PROVIDER_AUTHORITY,
                new File(pathname.getPath()));

    }


    public static void showToast(Activity activity,String nameToast){
        Toast.makeText(activity, nameToast, Toast.LENGTH_SHORT).show();
    }


    public static void downloadMediaItem(Activity context,File sourceFile) throws IOException {
        copyFile(sourceFile,
                new File(Environment.getExternalStorageDirectory().toString() +
                        DIRECTORY_TO_SAVE_MEDIA_NOW + sourceFile.getName()));
        Toast.makeText(context, "File Store in Gallery", Toast.LENGTH_SHORT).show();

    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public static boolean getAppIntro(MainActivity mainActivity) {
        SharedPreferences preferences;
        preferences = mainActivity.getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean("AppIntro", true);

    }

    public static List<FileClass> getvideoList(){
        List<FileClass> fileClasses = new ArrayList<>();
        fileClasses.clear();
        File fileeac = new File(Environment.getExternalStorageDirectory() + File.separator + WHATSHAP_FILES);
        File[] files = fileeac.listFiles();

        if (files != null){
            for (File file : files){
                if (file.getAbsolutePath().endsWith(".mp4"))
                    fileClasses.add(new FileClass(file.getAbsolutePath()));
            }
        }
        return fileClasses;
    }

    public static List<FileClass> getSaveList(){
        List<FileClass> fileClasses = new ArrayList<>();
        fileClasses.clear();

        File fileEach = new File(Environment.getExternalStorageDirectory() + File.separator + DIRECTORY_TO_SAVE_MEDIA_NOW);
        File[] files = fileEach.listFiles();

        if (files != null){
            for (File file : files)
                fileClasses.add(new FileClass(file.getAbsolutePath()));
        }

        return fileClasses;
    }


    public static List<FileClass> getImageList(){
        List<FileClass> fileClasses = new ArrayList<>();
        fileClasses.clear();

        File fileEach = new File(Environment.getExternalStorageDirectory() + File.separator + WHATSHAP_FILES);
        File[] files = fileEach.listFiles();

        if (files != null){
            for (File file : files) {
                if (file.getAbsolutePath().endsWith(".jpg") || file.getAbsolutePath().endsWith(".png"))
                    fileClasses.add(new FileClass(file.getAbsolutePath()));

            }
        }
        return fileClasses;
    }

    public static void openRateAppDialog(final Activity activity) {

            new AlertDialog.Builder(activity)
                    .setTitle("Rate Us")
                    .setMessage("If You Like Status Master Amazing App So Rate This Amazing Application")
                    .setIcon(R.drawable.rate_src)
                    .setPositiveButton("Rate Us", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utils.ratethisApp(activity);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("No",null)
                    .setNeutralButton("Later",null)
                    .create().show();

    }

    public static boolean isOnline(Activity activity) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager)
                activity.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null &&
                activeNetworkInfo.isConnectedOrConnecting();
    }


}
