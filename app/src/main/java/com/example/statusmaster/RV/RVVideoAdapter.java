package com.example.statusmaster.RV;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.statusmaster.Activity.VideoActivity;
import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.R;
import com.example.statusmaster.Utils;
import com.facebook.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RVVideoAdapter extends RecyclerView.Adapter<RVVideoAdapter.VH> {
    Activity activity;
    public List<FileClass> fileClassList = new ArrayList<>();
    private boolean count = true;
    public InterstitialAd interesialAd;

    public RVVideoAdapter(Activity activity, List<FileClass> fileClasses) {
        this.activity = activity;
        this.fileClassList = fileClasses;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_video_context,null));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

            holder.itemView.setTag(position);
            FileClass fileClass = fileClassList.get(position);

            Glide.with(activity)
                    .load(fileClass.getPathname())
                    .placeholder(Color.WHITE)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return fileClassList == null ? 0 : fileClassList.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView,imageViewPlay;
        RelativeLayout floatingActionButton,floatingActionButtonDownloaded;
        public VH(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rvvideoID);
            imageViewPlay = itemView.findViewById(R.id.rvsaveMID);
            floatingActionButton = itemView.findViewById(R.id.rvvideoShareMID);
            floatingActionButtonDownloaded = itemView.findViewById(R.id.rvvideoDownloadMID);

            floatingActionButtonDownloaded.setOnClickListener(this);
            floatingActionButton.setOnClickListener(this);
            imageView.setOnClickListener(this);

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            int position = (int) itemView.getTag();
            FileClass fileClass = fileClassList.get(position);
            switch (view.getId()){
                case R.id.rvvideoDownloadMID:
                    if (Utils.isOnline(activity) && count){
                        Utils.showIntrestialG(activity,interesialAd);
                        count=false;
                    }

                    try {
                        Utils.downloadMediaItem(activity,new File(fileClass.getPathname()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.rvvideoShareMID:
                    Utils.shareVideo(activity,Uri.parse(fileClass.getPathname()));
                    break;

                case R.id.rvvideoID:
                    Intent intent = new Intent(activity,VideoActivity.class);
                    intent.putExtra("videouri",fileClass.getPathname());
                    intent.putExtra("boolvalue",true);
                    activity.startActivity(intent);

                    break;
            }
        }

    }
}
