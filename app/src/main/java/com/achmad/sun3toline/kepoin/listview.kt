/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 9:19 PM
 *
 */

package com.achmad.sun3toline.kepoin

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import com.achmad.sun3toline.kepoin.activity.KepoIn

/**
 * Created by Muslim Muhammad on 10/16/2016.
 */
class listview : KepoIn() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_kepo_in)
    }

}