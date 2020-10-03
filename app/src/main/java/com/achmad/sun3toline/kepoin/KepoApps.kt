/*
 * *
 *  * Created by Achmad Fathullah on 10/3/20 9:21 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/3/20 12:03 AM
 *
 */

package com.achmad.sun3toline.kepoin

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by coldwarrior on 26/11/16.
 */
class KepoApps : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }
    }
}