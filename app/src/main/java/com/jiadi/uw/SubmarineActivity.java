package com.jiadi.uw;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by 北 on 2017/6/24.
 */

public class SubmarineActivity extends Fragment {
    private ImageButton buttonCus1;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_submarine, container, false);
        bindView(view);
        return view;
    }

    private void bindView(View view) {
    }
}
