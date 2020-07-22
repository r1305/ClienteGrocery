package com.freshfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.freshfastfood.R;

public class PagarenDelivery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagaren_delivery);



        WebView webView = (WebView) findViewById(R.id.webview_pagarendelivery);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        Fuente: https://www.iteramos.com/pregunta/25891/android-webview-lento

        webView.loadUrl("https://recursoscstore.com/payments/notificaciones/enviarNotificacion.php");



    }
}