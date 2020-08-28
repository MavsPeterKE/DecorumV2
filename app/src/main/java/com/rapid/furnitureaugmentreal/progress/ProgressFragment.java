package com.rapid.furnitureaugmentreal.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rapid.furnitureaugmentreal.R;


public class ProgressFragment extends DialogFragment {





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       getDialog().setTitle(R.string.app_name);
        return inflater.inflate(R.layout.layout_progress,container,false);
    }


}
