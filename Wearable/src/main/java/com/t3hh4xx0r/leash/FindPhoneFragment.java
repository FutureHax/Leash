package com.t3hh4xx0r.leash;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by r2doesinc on 7/3/14.
 */
public class FindPhoneFragment extends Fragment {
    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_find_phone, container, false);


        root.findViewById(R.id.button_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iV = (ImageView) root.findViewById(R.id.open);
//                iV.setBackgroundResource(R.drawable.go_to_phone_animation);
                AnimationDrawable openAnim = (AnimationDrawable) iV.getBackground();
                root.setBackgroundColor(Color.parseColor("#D9000000"));
                root.findViewById(R.id.label).setAlpha(.15f);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        root.setBackgroundColor(Color.parseColor("#80000000"));
                        root.findViewById(R.id.label).setAlpha(1f);
                    }
                }, 2739);
//                openAnim.stop();
//                openAnim.start();

                ((LeashActivity) getActivity()).findPhone();
                getActivity().finish();

            }
        });
        return root;
    }

}