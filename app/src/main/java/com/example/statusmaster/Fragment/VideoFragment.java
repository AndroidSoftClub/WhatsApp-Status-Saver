package com.example.statusmaster.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.R;
import com.example.statusmaster.RV.RVVideoAdapter;
import com.example.statusmaster.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    RecyclerView recyclerView;
    RVVideoAdapter rvVideoAdapter;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment_context,null);

        textView = view.findViewById(R.id.imagevideoFID);
        recyclerView = view.findViewById(R.id.rvvideoviewID);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);


        rvVideoAdapter = new RVVideoAdapter(getActivity(),Utils.getvideoList());
        recyclerView.setAdapter(rvVideoAdapter);
        rvVideoAdapter.notifyDataSetChanged();

        textView.setVisibility(rvVideoAdapter.fileClassList.size() <= 0 ? View.VISIBLE: View.GONE);

        return view;
    }

    @Override
    public void onDestroy() {
        if (rvVideoAdapter.interesialAd != null)
            rvVideoAdapter.interesialAd.destroy();
        super.onDestroy();
    }
}
