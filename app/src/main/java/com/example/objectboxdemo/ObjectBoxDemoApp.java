package com.example.objectboxdemo;

import android.app.Application;

import com.example.objectboxdemo.model.MyObjectBox;

import io.objectbox.BoxStore;


/**
 * Created by AsifMoinul on 12/25/2017.
 */

public class ObjectBoxDemoApp extends Application {

    private BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(ObjectBoxDemoApp.this).build();
    }

    public BoxStore getBoxStore() {
        return boxStore;
    }
}
