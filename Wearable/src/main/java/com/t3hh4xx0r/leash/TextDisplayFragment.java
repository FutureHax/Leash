package com.t3hh4xx0r.leash;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by r2doesinc on 7/3/14.
 */
public class TextDisplayFragment extends CardFragment {
    View root;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_open_prompt, container, false);
    }


}