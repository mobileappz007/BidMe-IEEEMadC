package com.bidme.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bidme.R;
import com.xw.repo.BubbleSeekBar;

public class DemoFragment1 extends Fragment {
    float refValue;
    boolean flag;
    EditText topupValueET;

    public static DemoFragment1 newInstance() {
        return new DemoFragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_demo_1, container, false);

        final BubbleSeekBar bubbleSeekBar = view.findViewById(R.id.demo_1_seek_bar);
        bubbleSeekBar.setProgress(20);
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {


                if (progressFloat != refValue && flag) {

                    topupValueET.setText(progressFloat + "");
                }
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        return view;
    }
}
