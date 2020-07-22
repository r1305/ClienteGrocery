package com.freshfastfood;

import android.app.Application;

import com.google.android.gms.common.api.internal.GoogleApiManager;

public class MapActivity extends Application {
   // static final LatLng HAMBURG = new LatLng(53.558, 9.927);
  //  static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleApiManager map;
    @Override
    public void onCreate() {
        super.onCreate();

    }
}
