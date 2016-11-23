package com.example.muslimmuhammad.kepoin;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;

import com.example.muslimmuhammad.kepoin.activity.KepoIn;

/**
 * Created by Muslim Muhammad on 10/16/2016.
 */
public class listview extends KepoIn {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_kepo_in);

    }
}
