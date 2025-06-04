package com.example.eventsnvsu

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("MyApplication", "onCreate called")
        FirebaseApp.initializeApp(this)
    }
}
