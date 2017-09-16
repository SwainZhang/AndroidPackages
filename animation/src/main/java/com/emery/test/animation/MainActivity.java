package com.emery.test.animation;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mSupportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.activity_main, TransitionFragment
                .newInstance()).
        commit();

        this.bindService(new Intent(), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, BIND_AUTO_CREATE);

    }

    public void start(View view) {

        ViewGroup container = (ViewGroup) findViewById(R.id.activity_main);
        TransitionInflater transitionInflater = TransitionInflater.from(this);
        Transition transition = transitionInflater.inflateTransition(R.transition
                .slow_auto_transition);
        TransitionManager transitionManager = transitionInflater.inflateTransitionManager(
                R.transition.transition_manager, container);
        Scene sceneForLayout_before = Scene.getSceneForLayout(container, R.layout
                .fragment_transition_befor_scene, this);
        Scene sceneForLayout_after = Scene.getSceneForLayout(container, R.layout
                .fragment_transition_after_scene, this);
        transitionManager.setTransition(sceneForLayout_before, sceneForLayout_after, transition);
        transitionManager.beginDelayedTransition(container, transition);
        TransitionManager.go(sceneForLayout_after);
    }
}
