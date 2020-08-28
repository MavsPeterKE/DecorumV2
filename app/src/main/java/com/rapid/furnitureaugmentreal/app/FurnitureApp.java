package com.rapid.furnitureaugmentreal.app;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class FurnitureApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
