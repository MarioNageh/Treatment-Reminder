package com.marionageh.treatmentreminder.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marionageh.treatmentreminder.R;

import butterknife.ButterKnife;

public class NoDataFragment extends Fragment {

    public NoDataFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the no data layout
        View view = inflater.inflate(R.layout.no_data_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
