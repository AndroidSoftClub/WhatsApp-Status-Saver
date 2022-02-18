package com.example.statusmaster.Fragment;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.statusmaster.Data.FileClass;
import com.example.statusmaster.R;
import com.example.statusmaster.RV.RVSaveAdapter;
import com.example.statusmaster.RVinterface.onImageClick;
import com.example.statusmaster.Utils;

public class SaveFragment extends Fragment implements View.OnClickListener, onImageClick {

    RecyclerView recyclerView;
    RVSaveAdapter saveAdapter;
    TextView textView;
    LinearLayout linearLayoutSMain,linearLayoutdelete,linearLayoutcancle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_fragment_context, null);


        linearLayoutSMain = view.findViewById(R.id.ll_saveID);
        linearLayoutcancle = view.findViewById(R.id.llcancleID);
        linearLayoutdelete = view.findViewById(R.id.lldeleteID);
        textView = view.findViewById(R.id.imagesaveFID);
        recyclerView = view.findViewById(R.id.rvsaveviewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        linearLayoutdelete.setOnClickListener(this);
        linearLayoutcancle.setOnClickListener(this);


        saveAdapter = new RVSaveAdapter(getActivity(), Utils.getSaveList(),this);
        recyclerView.setAdapter(saveAdapter);
        saveAdapter.notifyDataSetChanged();

        textView.setVisibility(saveAdapter.fileClassList.size() <= 0 ? View.VISIBLE : View.GONE);

        return view;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lldeleteID:
                saveAdapter.deltedItem();
                linearLayoutSMain.setVisibility(View.GONE);
                saveAdapter.notifyDataSetChanged();
                textView.setVisibility(saveAdapter.fileClassList.size() <= 0 ? View.VISIBLE : View.GONE);

                break;

            case R.id.llcancleID:
                linearLayoutSMain.setVisibility(View.GONE);
                saveAdapter.refressAdapter();
                break;
        }
    }

    @Override
    public void setAdapterposition(FileClass fileClass) {
        linearLayoutSMain.setVisibility(fileClass.isChek() ? View.VISIBLE : View.GONE);
    }
}
