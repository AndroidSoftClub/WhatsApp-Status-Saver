package com.example.statusmaster.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.statusmaster.Activity.MainActivity;
import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.R;
import com.example.statusmaster.RV.RVImageAdapter;
import com.example.statusmaster.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment {

    RecyclerView recyclerView;
    RVImageAdapter imageRVAdapter;
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_frgment_context,null);


        textView = view.findViewById(R.id.tvimageFID);
        recyclerView = view.findViewById(R.id.rvImageFMainID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        imageRVAdapter = new RVImageAdapter(getActivity(),Utils.getImageList());
        recyclerView.setAdapter(imageRVAdapter);
        imageRVAdapter.notifyDataSetChanged();

        textView.setVisibility(imageRVAdapter.fileClassList.size() <= 0 ? View.VISIBLE : View.GONE);

        return view;

    }

    @Override
    public void onDestroy() {
        if (imageRVAdapter.interesialAd != null)
            imageRVAdapter.interesialAd.destroy();
        super.onDestroy();
    }
}

