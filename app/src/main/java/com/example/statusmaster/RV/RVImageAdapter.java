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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.example.statusmaster.Activity.ImageActivity;
import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.R;
import com.example.statusmaster.Utils;
import com.facebook.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RVImageAdapter extends RecyclerView.Adapter<RVImageAdapter.VH> {
    Activity activity;
    public List<FileClass> fileClassList = new ArrayList<>();
    public InterstitialAd interesialAd;
    private boolean count = true;

    public RVImageAdapter(Activity activity, List<FileClass> fileClassList) {
        this.activity = activity;
        this.fileClassList = fileClassList;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_image_context,null));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        holder.itemView.setTag(position);

        FileClass fileClass = fileClassList.get(position);

        if (fileClass.getPathname().endsWith(".png") || fileClass.getPathname().endsWith(".jpg")) {

            Glide.with(activity)
                    .load(fileClass.getPathname())
                    .thumbnail(0.1f)
                    .placeholder(Color.WHITE)
                    .centerCrop()
                    .into(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {
        return fileClassList == null ? 0 : fileClassList.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        LinearLayout floatingActionButtonShare,floatingActionButtonDownloaded;

        public VH(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.rvimageID);
            floatingActionButtonDownloaded =itemView.findViewById(R.id.rvimagedownloadedID);
            floatingActionButtonShare =itemView.findViewById(R.id.rvimageshareID);

            floatingActionButtonShare.setOnClickListener(this);

            floatingActionButtonDownloaded.setOnClickListener(this);
            imageView.setOnClickListener(this);

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            int position = (int) itemView.getTag();
            FileClass fileClass = fileClassList.get(position);

            switch (view.getId()){
                case R.id.rvimageID:
                    Intent intent = new Intent(activity, ImageActivity.class);
                    intent.putExtra("pathname",fileClass.getPathname());
                    intent.putExtra("boolvalue",true);
                    activity.startActivity(intent);
                    break;

                case R.id.rvimagedownloadedID:
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

                case R.id.rvimageshareID:
                    Utils.shareImage(activity,Uri.parse(fileClass.getPathname()));
                    Toast.makeText(activity, "Share", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
