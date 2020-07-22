package com.freshfastfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.freshfastfood.R;
import com.freshfastfood.fragment.OrderSumrryFragment;
import com.freshfastfood.model.PaymentItem;
import com.freshfastfood.model.User;

import butterknife.BindView;

public class DepositoBancario extends AppCompatActivity {



    // int amount = 0;

    //  Bundle datos;
    // TextView PagarconYape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depositobancario);

        WebView webView = (WebView) findViewById(R.id.webview_depositobancario);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.loadUrl("https://recursoscstore.com/payments/notificaciones/enviarNotificacion.php");




        //  amount = getIntent().getIntExtra("amount", 0);


        //   datos = getIntent().getExtras();


        // String datosobtenidos =datos.getString("TOTAL");

        TextView pagopersonal = (TextView)findViewById(R.id.pagodeposito);
        //pagopersonal.setText(0);
        pagopersonal.setText(getIntent().getIntExtra("amount", 0)+"");





        // startPayment(String.valueOf(amount));




    }


}