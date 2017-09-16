package com.emery.test.animation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MyPC on 2017/2/22.
 */

public class TransitionFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transition_befor_scene,null);
    }

    public static TransitionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TransitionFragment fragment = new TransitionFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
