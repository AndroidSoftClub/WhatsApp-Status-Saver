package com.example.statusmaster.RV;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.statusmaster.Activity.ImageActivity;
import com.example.statusmaster.Activity.VideoActivity;
import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.Fragment.SaveFragment;
import com.example.statusmaster.R;
import com.example.statusmaster.RVinterface.onImageClick;
import com.example.statusmaster.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.statusmaster.Utils.DIRECTORY_TO_SAVE_MEDIA_NOW;

public class RVSaveAdapter extends RecyclerView.Adapter<RVSaveAdapter.VH> {
    Activity activity;
    public List<FileClass> fileClassList = new ArrayList<>();
    public List<FileClass> selected_list = new ArrayList<>();
    onImageClick imageClick;
    boolean isChek=false;
    private FileClass fileClass;

    public RVSaveAdapter(Activity activity, List<FileClass> fileClasses,onImageClick imageClick) {
        this.activity = activity;
        this.fileClassList = fileClasses;
        this.imageClick = imageClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_save_context, null));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        FileClass fileClass =  fileClassList.get(position);

        holder.itemView.setTag(position);
        holder.imageViewclick.setLongClickable(!fileClass.isChek());
        holder.imageViewPlay.setVisibility(fileClass.getPathname().endsWith(".mp4") ? View.VISIBLE : View.GONE);
        holder.frameLayout.setVisibility(fileClass.isChek() ? View.VISIBLE : View.GONE);
        holder.floatingActionButtonDelete.setVisibility(fileClass.isChek() ? View.GONE : View.VISIBLE);
        holder.floatingActionButtonShare.setVisibility(fileClass.isChek() ? View.GONE : View.VISIBLE);

        Glide.with(activity)
                    .load(fileClass.getPathname())
                    .placeholder(Color.WHITE)
                    .thumbnail(0.1f)
                    .centerCrop()
                    .into(holder.imageViewclick);

    }

    @Override
    public int getItemCount() {
        return fileClassList == null ? 0 : fileClassList.size();
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener
            , View.OnLongClickListener {
        ImageView imageViewclick, imageViewPlay;
        RelativeLayout floatingActionButtonShare, floatingActionButtonDelete;
        FrameLayout frameLayout;
        public VH(@NonNull View itemView) {
            super(itemView);

            frameLayout = itemView.findViewById(R.id.framlaID);
            imageViewclick = itemView.findViewById(R.id.rvsaveclickID);
            imageViewPlay = itemView.findViewById(R.id.rvsavePlayId);
            floatingActionButtonDelete = itemView.findViewById(R.id.rvsavedeletedId);
            floatingActionButtonShare = itemView.findViewById(R.id.rvsharedownloadId);


            imageViewclick.setOnClickListener(this);
            imageViewclick.setOnLongClickListener(this);
            floatingActionButtonShare.setOnClickListener(this);
            floatingActionButtonDelete.setOnClickListener(this);
            frameLayout.setOnClickListener(this);

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {

            final int position = (int) itemView.getTag();

            fileClass = fileClassList.get(position);

            switch (view.getId()) {

                case R.id.framlaID:
                    fileClass.setChek(!fileClass.isChek());
                    selected_list.remove(fileClass);
                    notifyDataSetChanged();

                    break;
                case R.id.rvsaveclickID:

                    if (isChek){
                        fileClass.setChek(!fileClass.isChek());
                        selected_list.add(fileClass);
                        notifyDataSetChanged();
                    }
                    else{
                        if (fileClass.getPathname().endsWith(".mp4")) {
                            Intent intent = new Intent(activity, VideoActivity.class);
                            intent.putExtra("videouri", fileClass.getPathname());
                            activity.startActivity(intent);

                        } else {
                            Intent intent = new Intent(activity, ImageActivity.class);
                            intent.putExtra("pathname", fileClass.getPathname());
                            activity.startActivity(intent);
                        }
                    }
                    break;
                case R.id.rvsavedeletedId:

                    final File file = new File(fileClass.getPathname());

                    new AlertDialog.Builder(activity)
                            .setIcon(R.drawable.deletimagered)
                            .setTitle("Delete?")
                            .setMessage("Are You Sure Want to Delete this File(s)")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (file.exists())
                                        file.delete();

                                    fileClassList.remove(position);
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);

                                    dialogInterface.dismiss();
                                }
                            })
                            .setNeutralButton("No",null)
                            .create().show();


                    break;

                case R.id.rvsharedownloadId:
                    Utils.showToast(activity, "Share");


                    if (fileClass.getPathname().endsWith(".mp4")) {
                        Utils.shareVideo(activity, Uri.parse(fileClass.getPathname()));
                    } else {
                        Utils.shareImage(activity, Uri.parse(fileClass.getPathname()));
                    }
                    break;
            }
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onLongClick(View view) {

            int position_longclick = (int)itemView.getTag();
            FileClass fileClass = fileClassList.get(position_longclick);

            switch (view.getId()){
                case R.id.rvsaveclickID:
                    fileClass.setChek(!fileClass.isChek());
                    selected_list.add(fileClass);
                    imageClick.setAdapterposition(fileClass);
                    isChek=true;
                    notifyDataSetChanged();

                    break;
            }
            return true;
        }
    }



    public void deltedItem(){
        for (int i=0;i<selected_list.size();i++){
            fileClassList.remove(selected_list.get(i));
            File file = new File(selected_list.get(i).getPathname());
            file.getAbsoluteFile().delete();
        }

        refressAdapter();
    }

    public void refressAdapter(){
        isChek=false;
        selected_list.clear();
        for (int i=0;i<fileClassList.size();i++){
                fileClassList.get(i).setChek(false);
        }
        notifyDataSetChanged();
    }

}
