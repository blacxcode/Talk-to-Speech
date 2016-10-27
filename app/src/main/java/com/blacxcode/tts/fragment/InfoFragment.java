package com.blacxcode.tts.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blacxcode.tts.R;

/**
 * Created by blacXcode on 10/27/2016.
 */

public class InfoFragment extends BottomSheetDialogFragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info, container, false);
        return view;
    }
}